'''
Created on 2025. 11. 29.

@author: jaewoo
'''
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.dates as mdates
import os

SAVE_DIR = '/users/jaewoo/data/acn'
OUTPUT_FIG = os.path.join(SAVE_DIR, 'psychological_safety.png')
CSV_FILENAME = os.path.join(SAVE_DIR, 'acn_data_1week.csv')

# ---------------------------------------------------------
# 1. 설정 (Configuration)
# ---------------------------------------------------------
MAX_CHARGING_RATE = 6.6
MIN_GUARANTEE_POWER = 1.0  # 최소 보장 전력 (1kW - 심리적 안정선)
TIME_INTERVAL = 5
GRID_CAPACITY_LIMIT = 20.0 # 빡빡한 용량 제한

# [수정] 분석 기간 (LA 로컬 시간 기준)
# UTC 변환 후 필터링을 위해 시간대 정보가 없는 문자열 사용
TARGET_START_DATE = '2019-10-01'
TARGET_END_DATE = '2019-10-08'

# [수정] 혼잡한 특정 날짜 (LA 로컬 시간 기준)
ZOOM_DATE = '2019-10-02' 

# ---------------------------------------------------------
# 2. 데이터 로드 (Timezone 처리 추가)
# ---------------------------------------------------------
def get_ev_data(filename):
    try:
        if not os.path.exists(filename): return pd.DataFrame()
        df = pd.read_csv(filename)
        
        # 날짜 컬럼 변환 및 타임존 변경 (UTC -> LA)
        time_cols = ['connectionTime', 'disconnectTime']
        for col in time_cols:
            # 1. UTC로 파싱
            df[col] = pd.to_datetime(df[col], utc=True)
            # 2. LA 시간으로 변환
            df[col] = df[col].dt.tz_convert('America/Los_Angeles')
            
        return df.sort_values(by='connectionTime')
    except:
        return pd.DataFrame()

# ---------------------------------------------------------
# 3. 알고리즘: Standard EDF vs Fair EDF (Min Guarantee)
# ---------------------------------------------------------
def run_simulation(ev_data, time_index, capacity_limit, algorithm):
    t_len = len(time_index)
    dt_hours = TIME_INTERVAL / 60.0
    
    # 결과 저장용: 시간별 "충전 중인(>0kW) 차량 수"
    active_charging_count = np.zeros(t_len)
    
    ev_states = []
    for i, row in ev_data.iterrows():
        # searchsorted는 타임존을 고려하여 인덱스를 찾음
        ev_states.append({
            'arrival_idx': time_index.searchsorted(row['connectionTime']),
            'departure_idx': time_index.searchsorted(row['disconnectTime']),
            'energy_remaining': row['kWhDelivered'],
            'max_rate': MAX_CHARGING_RATE
        })

    for t in range(t_len):
        # 현재 연결된 차들 (에너지가 남은)
        connected_evs = [ev for ev in ev_states 
                         if ev['arrival_idx'] <= t < ev['departure_idx'] and ev['energy_remaining'] > 0.001]
        
        if not connected_evs: continue
            
        # 정렬 (EDF)
        connected_evs.sort(key=lambda x: x['departure_idx'])
        
        current_load = 0
        charging_evs_this_step = 0
        
        # --- A. Fair-EDF (Minimum Guarantee First) ---
        if algorithm == 'FAIR_EDF':
            # 1단계: 최소 전력 분배
            n_cars = len(connected_evs)
            guaranteed_power = min(MIN_GUARANTEE_POWER, capacity_limit / n_cars)
            
            # 1차 할당
            for ev in connected_evs:
                power = min(ev['max_rate'], guaranteed_power)
                ev['temp_power'] = power
                current_load += power
                
            # 2단계: 남는 용량으로 급한 차 추가 할당
            remaining_capacity = capacity_limit - current_load
            
            for ev in connected_evs:
                if remaining_capacity <= 0.001: break
                room = ev['max_rate'] - ev['temp_power']
                if room > 0:
                    add_power = min(room, remaining_capacity)
                    ev['temp_power'] += add_power
                    current_load += add_power
                    remaining_capacity -= add_power
            
            # 최종 업데이트
            for ev in connected_evs:
                power = ev['temp_power']
                if power > 0.001: charging_evs_this_step += 1
                ev['energy_remaining'] -= power * dt_hours
                if ev['energy_remaining'] < 0: ev['energy_remaining'] = 0

        # --- B. Standard EDF (Winner Takes All) ---
        elif algorithm == 'EDF':
            for ev in connected_evs:
                if current_load >= capacity_limit: break
                
                available = capacity_limit - current_load
                req_power_rate = ev['energy_remaining'] / dt_hours
                power = min(ev['max_rate'], available, req_power_rate)
                
                if power > 0.1: 
                    charging_evs_this_step += 1
                
                current_load += power
                ev['energy_remaining'] -= power * dt_hours
                if ev['energy_remaining'] < 0: ev['energy_remaining'] = 0
                
        active_charging_count[t] = charging_evs_this_step

    return active_charging_count

# ---------------------------------------------------------
# 4. 메인 실행
# ---------------------------------------------------------
if __name__ == "__main__":
    ev_df = get_ev_data(CSV_FILENAME)
    
    if not ev_df.empty:
        # [수정] 시간 축 생성 시 Timezone 지정 ('America/Los_Angeles')
        start = pd.Timestamp(TARGET_START_DATE).tz_localize('America/Los_Angeles')
        end = pd.Timestamp(TARGET_END_DATE).tz_localize('America/Los_Angeles') + pd.Timedelta(days=1)
        
        time_index = pd.date_range(start, end, freq=f'{TIME_INTERVAL}min')
        
        # 시뮬레이션
        count_edf = run_simulation(ev_df, time_index, GRID_CAPACITY_LIMIT, 'EDF')
        count_fair = run_simulation(ev_df, time_index, GRID_CAPACITY_LIMIT, 'FAIR_EDF')
        
        # [줌인] 특정 날짜(ZOOM_DATE) 데이터만 잘라내기
        zoom_start = pd.Timestamp(ZOOM_DATE).tz_localize('America/Los_Angeles')
        zoom_end = zoom_start + pd.Timedelta(days=1)
        
        mask = (time_index >= zoom_start) & (time_index < zoom_end)
        zoom_time = time_index[mask]
        zoom_edf = count_edf[mask]
        zoom_fair = count_fair[mask]
        
        # 그래프 그리기
        plt.figure(figsize=(12, 6))
        
        plt.fill_between(zoom_time, zoom_fair, color='green', alpha=0.3, label='Fair-EDF (Min. Guarantee)')
        plt.plot(zoom_time, zoom_fair, color='green', linewidth=2)
        
        plt.step(zoom_time, zoom_edf, color='red', linestyle='--', linewidth=2, label='Standard EDF (Winner-takes-all)', where='mid')
        
        plt.title(f'Psychological Safety Visualization: Number of Actively Charging EVs ({ZOOM_DATE})', fontsize=14)
        plt.ylabel('Number of EVs Receiving Power (>0 kW)', fontsize=12)
        plt.xlabel('Time of Day (Los Angeles Time)', fontsize=12)
        plt.legend(loc='upper right', fontsize=11)
        plt.grid(True, alpha=0.3)
        
        # X축 포맷팅 (로컬 시간 기준)
        plt.gca().xaxis.set_major_formatter(mdates.DateFormatter('%H:%M', tz=zoom_time.tz))
        
        # 주석
        if len(zoom_fair) > 0:
            max_idx = np.argmax(zoom_fair)
            diff = zoom_fair[max_idx] - zoom_edf[max_idx]
            if diff > 0:
                plt.annotate(f"Psychological Safety Gap\n(+{int(diff)} users connected)", 
                             xy=(zoom_time[max_idx], zoom_fair[max_idx]), 
                             xytext=(zoom_time[max_idx], zoom_fair[max_idx]+3),
                             arrowprops=dict(facecolor='black', shrink=0.05),
                             ha='center', fontsize=10, fontweight='bold')

        plt.tight_layout()
        
        if not os.path.exists(SAVE_DIR):
            try: os.makedirs(SAVE_DIR)
            except: pass
            
        plt.savefig(OUTPUT_FIG, dpi=300)
        print(f"[SUCCESS] 그래프 저장 완료: {OUTPUT_FIG}")
        plt.show()
    else:
        print("데이터 없음")