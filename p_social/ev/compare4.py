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
OUTPUT_FIG = os.path.join(SAVE_DIR, 'tou_dynamic_capacity_simulation.png')
CSV_FILENAME = os.path.join(SAVE_DIR, 'acn_data_1week.csv')

# ---------------------------------------------------------
# 1. 설정 (Configuration)
# ---------------------------------------------------------
MAX_CHARGING_RATE = 6.6
TIME_INTERVAL = 5
TARGET_START_DATE = '2019-10-01'
TARGET_END_DATE = '2019-10-08'

# [핵심] TOU 기반 동적 용량 제한 설정 (kW)
# On-peak (12:00 ~ 18:00): 비싸니까 빡빡하게 제한 (20kW)
# Off-peak (나머지): 싸니까 널널하게 제한 (50kW)
ON_PEAK_LIMIT = 20.0
OFF_PEAK_LIMIT = 50.0
ON_PEAK_START_HOUR = 12
ON_PEAK_END_HOUR = 18

# ---------------------------------------------------------
# 2. 데이터 로드
# ---------------------------------------------------------
def get_ev_data(filename):
    try:
        if not os.path.exists(filename): return pd.DataFrame()
        df = pd.read_csv(filename)
        for col in ['connectionTime', 'disconnectTime']:
            df[col] = pd.to_datetime(df[col], utc=True)
        
        mask = (df['connectionTime'] >= TARGET_START_DATE) & (df['connectionTime'] <= f"{TARGET_END_DATE} 23:59:59")
        df = df.loc[mask].copy()
        df = df[df['kWhDelivered'] > 0]
        return df.sort_values(by='connectionTime')
    except:
        return pd.DataFrame()

# ---------------------------------------------------------
# 3. 시간대별 용량 제한 생성 함수
# ---------------------------------------------------------
def get_capacity_limit_profile(time_index):
    limits = []
    for t in time_index:
        hour = t.hour
        # 주말 제외 로직을 넣을 수도 있지만, 여기선 단순화하여 매일 적용
        if ON_PEAK_START_HOUR <= hour < ON_PEAK_END_HOUR:
            limits.append(ON_PEAK_LIMIT)
        else:
            limits.append(OFF_PEAK_LIMIT)
    return np.array(limits)

# ---------------------------------------------------------
# 4. 동적 제한 시뮬레이션 엔진
# ---------------------------------------------------------
def run_dynamic_simulation(ev_data, time_index, limit_profile, algorithm):
    t_len = len(time_index)
    dt_hours = TIME_INTERVAL / 60.0
    total_load = np.zeros(t_len)
    
    # EV 상태 초기화
    ev_states = []
    for i, row in ev_data.iterrows():
        ev_states.append({
            'arrival_idx': time_index.searchsorted(row['connectionTime']),
            'departure_idx': time_index.searchsorted(row['disconnectTime']),
            'energy_remaining': row['kWhDelivered'],
            'energy_requested': row['kWhDelivered'],
            'energy_delivered': 0.0,
            'max_rate': MAX_CHARGING_RATE
        })

    for t in range(t_len):
        # [중요] 현재 시간(t)의 용량 제한 가져오기
        current_limit = limit_profile[t]
        
        active_evs = [ev for ev in ev_states 
                      if ev['arrival_idx'] <= t < ev['departure_idx'] and ev['energy_remaining'] > 0.001]
        
        if not active_evs: continue
            
        # 정렬 기준
        if algorithm == 'FCFS':
            active_evs.sort(key=lambda x: x['arrival_idx'])
        elif algorithm == 'EDF':
            active_evs.sort(key=lambda x: x['departure_idx'])
            
        current_load = 0
        for ev in active_evs:
            if current_load >= current_limit: break
            
            available = current_limit - current_load
            req_power = ev['energy_remaining'] / dt_hours
            power = min(ev['max_rate'], available, req_power)
            
            current_load += power
            total_load[t] += power
            ev['energy_remaining'] -= (power * dt_hours)
            ev['energy_delivered'] += (power * dt_hours)
            if ev['energy_remaining'] < 0: ev['energy_remaining'] = 0

    # 결과 집계
    fully_charged_count = sum(1 for ev in ev_states if (ev['energy_delivered']/ev['energy_requested']) >= 0.99)
    
    return total_load, fully_charged_count, len(ev_states)

# ---------------------------------------------------------
# 5. 메인 실행
# ---------------------------------------------------------
if __name__ == "__main__":
    ev_df = get_ev_data(CSV_FILENAME)
    
    if not ev_df.empty:
        start = pd.to_datetime(TARGET_START_DATE, utc=True)
        end = pd.to_datetime(TARGET_END_DATE, utc=True) + pd.Timedelta(days=1)
        time_index = pd.date_range(start, end, freq=f'{TIME_INTERVAL}min')
        
        # 1. 동적 용량 프로파일 생성 (계단형)
        limit_profile = get_capacity_limit_profile(time_index)
        
        print(f"Simulation with Dynamic Limit: {OFF_PEAK_LIMIT}kW -> {ON_PEAK_LIMIT}kW (12-18h)")
        
        # 2. 시뮬레이션 실행
        load_fcfs, full_fcfs, total_evs = run_dynamic_simulation(ev_df, time_index, limit_profile, 'FCFS')
        load_edf, full_edf, _ = run_dynamic_simulation(ev_df, time_index, limit_profile, 'EDF')
        
        print(f"[Result] Fully Charged Rate")
        print(f"  FCFS: {full_fcfs}/{total_evs} ({(full_fcfs/total_evs)*100:.1f}%)")
        print(f"  EDF : {full_edf}/{total_evs} ({(full_edf/total_evs)*100:.1f}%)")

        # 3. 그래프 그리기
        plt.figure(figsize=(14, 8))
        
        # (Top) Load Profile vs Dynamic Limit
        plt.subplot(2, 1, 1)
        # Limit Line (빨간 계단선)
        plt.step(time_index, limit_profile, color='red', linestyle='--', label='Dynamic Capacity Limit (TOU-based)', where='post')
        plt.fill_between(time_index, limit_profile, color='red', alpha=0.05)
        
        plt.plot(time_index, load_fcfs, color='orange', alpha=0.6, label='FCFS Load')
        plt.plot(time_index, load_edf, color='blue', alpha=0.8, label='EDF Load')
        
        plt.title('Dynamic Capacity Management: Load Profile vs TOU Limit', fontsize=14)
        plt.ylabel('Power (kW)')
        plt.legend(loc='upper right')
        plt.grid(True, alpha=0.3)
        plt.gca().xaxis.set_major_formatter(mdates.DateFormatter('%m-%d %H'))
        
        # (Bottom) Satisfaction Bar Chart
        plt.subplot(2, 1, 2)
        labels = ['FCFS', 'Online EDF']
        rates = [(full_fcfs/total_evs)*100, (full_edf/total_evs)*100]
        
        bars = plt.bar(labels, rates, color=['orange', 'royalblue'], width=0.4)
        plt.ylabel('Fully Charged Rate (%)')
        plt.title('Service Satisfaction under Dynamic Constraints', fontsize=14)
        plt.ylim(0, 110)
        
        for bar in bars:
            height = bar.get_height()
            plt.text(bar.get_x() + bar.get_width()/2., height, f'{height:.1f}%', 
                     ha='center', va='bottom', fontsize=12, fontweight='bold')
            
        plt.tight_layout()
        
        if not os.path.exists(SAVE_DIR):
            try: os.makedirs(SAVE_DIR)
            except: pass
            
        plt.savefig(OUTPUT_FIG, dpi=300)
        print(f"[SUCCESS] 그래프 저장 완료: {OUTPUT_FIG}")
        # plt.show()
    else:
        print("[ERROR] 데이터가 없습니다.")