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
OUTPUT_FIG = os.path.join(SAVE_DIR, 'fcfs_vs_edf_satisfaction.png')
CSV_FILENAME = os.path.join(SAVE_DIR, 'acn_data_1week.csv')

# ---------------------------------------------------------
# 1. 설정 (Configuration)
# ---------------------------------------------------------
MAX_CHARGING_RATE = 6.6
TIME_INTERVAL = 5
TARGET_START_DATE = '2019-10-01'
TARGET_END_DATE = '2019-10-08'

# [핵심] 전력망 용량을 아주 빡빡하게 설정 (ASAP 피크의 40% 수준)
# 그래야 두 알고리즘의 차이가 극명하게 드러남
HARD_CAPACITY_LIMIT = 20.0 # kW

# ---------------------------------------------------------
# 2. 데이터 로드 및 전처리
# ---------------------------------------------------------
def get_ev_data(filename):
    try:
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
# 3. 시뮬레이션 엔진 (공통)
# ---------------------------------------------------------
def run_simulation(ev_data, time_index, capacity_limit, algorithm='FCFS'):
    t_len = len(time_index)
    dt_hours = TIME_INTERVAL / 60.0
    
    total_load = np.zeros(t_len)
    
    # EV 상태 초기화
    ev_states = []
    for i, row in ev_data.iterrows():
        ev_states.append({
            'id': i,
            'arrival_idx': time_index.searchsorted(row['connectionTime']),
            'departure_idx': time_index.searchsorted(row['disconnectTime']),
            'energy_needed': row['kWhDelivered'], # 초기 요구량
            'energy_delivered': 0.0,
            'max_rate': MAX_CHARGING_RATE
        })
        
    # Time-stepping
    for t in range(t_len):
        # 1. 현재 주차 중이고 충전이 필요한 차
        active_evs = [ev for ev in ev_states 
                      if ev['arrival_idx'] <= t < ev['departure_idx'] and ev['energy_needed'] > 0.001]
        
        if not active_evs: continue
            
        # [알고리즘 차이점] 정렬 기준
        if algorithm == 'FCFS':
            # 먼저 온 순서 (Arrival Index)
            active_evs.sort(key=lambda x: x['arrival_idx'])
        elif algorithm == 'EDF':
            # 빨리 떠날 순서 (Departure Index)
            active_evs.sort(key=lambda x: x['departure_idx'])
            
        # 2. 전력 할당
        current_load = 0
        for ev in active_evs:
            if current_load >= capacity_limit:
                break # 용량 초과 시 나머지 차량 대기
            
            available = capacity_limit - current_load
            req_power = ev['energy_needed'] / dt_hours
            power = min(ev['max_rate'], available, req_power)
            
            # 상태 업데이트
            current_load += power
            total_load[t] += power
            
            ev['energy_needed'] -= (power * dt_hours)
            ev['energy_delivered'] += (power * dt_hours)
            if ev['energy_needed'] < 0: ev['energy_needed'] = 0

    # 결과 집계: 총 전달된 에너지, 미충전량(Unmet Demand)
    total_delivered = sum(ev['energy_delivered'] for ev in ev_states)
    total_requested = sum(ev['energy_delivered'] + ev['energy_needed'] for ev in ev_states)
    
    # 각 차량별 만족도 (100% 충전 여부)
    fully_charged_count = sum(1 for ev in ev_states if ev['energy_needed'] < 0.1)
    
    return {
        'load_profile': total_load,
        'total_delivered': total_delivered,
        'satisfaction_rate': (total_delivered / total_requested) * 100,
        'fully_charged_count': fully_charged_count,
        'total_evs': len(ev_states)
    }

# ---------------------------------------------------------
# 4. 메인 실행 및 비교 그래프
# ---------------------------------------------------------
if __name__ == "__main__":
    ev_df = get_ev_data(CSV_FILENAME)
    
    if not ev_df.empty:
        start = pd.to_datetime(TARGET_START_DATE, utc=True)
        end = pd.to_datetime(TARGET_END_DATE, utc=True) + pd.Timedelta(days=1)
        time_index = pd.date_range(start, end, freq=f'{TIME_INTERVAL}min')
        
        # 용량 제한 설정 (데이터 피크의 40% 정도로 강제 설정)
        # 우선 Uncoordinated(No Limit)를 돌려 피크 확인
        # (간단히 계산)
        # HARD_CAPACITY_LIMIT = 20.0 # 위에서 설정함
        
        print(f"Simulation with Grid Limit: {HARD_CAPACITY_LIMIT} kW")
        
        # 1. FCFS 실행
        res_fcfs = run_simulation(ev_df, time_index, HARD_CAPACITY_LIMIT, 'FCFS')
        print(f"[FCFS] Delivered: {res_fcfs['total_delivered']:.1f} kWh, Fully Charged: {res_fcfs['fully_charged_count']}/{res_fcfs['total_evs']}")
        
        # 2. EDF 실행
        res_edf = run_simulation(ev_df, time_index, HARD_CAPACITY_LIMIT, 'EDF')
        print(f"[EDF]  Delivered: {res_edf['total_delivered']:.1f} kWh, Fully Charged: {res_edf['fully_charged_count']}/{res_edf['total_evs']}")
        
        # 3. 그래프 그리기
        fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(10, 10))
        
        # 부하 프로파일 (Load Profile)
        ax1.plot(time_index, res_fcfs['load_profile'], label='FCFS (Arrival-based)', color='orange', alpha=0.7)
        ax1.plot(time_index, res_edf['load_profile'], label='EDF (Deadline-based)', color='blue', alpha=0.7, linestyle='--')
        ax1.axhline(y=HARD_CAPACITY_LIMIT, color='red', linestyle=':', label='Capacity Limit')
        ax1.set_ylabel('Power (kW)')
        ax1.set_title('Load Profile under Constraint')
        ax1.legend()
        ax1.xaxis.set_major_formatter(mdates.DateFormatter('%m-%d'))
        ax1.grid(True, alpha=0.3)
        
        # 만족도 비교 (Bar Chart)
        labels = ['FCFS', 'EDF']
        satisfaction = [res_fcfs['satisfaction_rate'], res_edf['satisfaction_rate']]
        full_charge_ratios = [res_fcfs['fully_charged_count']/res_fcfs['total_evs']*100, 
                              res_edf['fully_charged_count']/res_edf['total_evs']*100]
        
        x = np.arange(len(labels))
        width = 0.35
        
        rects1 = ax2.bar(x - width/2, satisfaction, width, label='Energy Met (%)', color='lightgray')
        rects2 = ax2.bar(x + width/2, full_charge_ratios, width, label='Fully Charged Vehicles (%)', color='green')
        
        ax2.set_ylabel('Percentage (%)')
        ax2.set_title('Service Quality Comparison')
        ax2.set_xticks(x)
        ax2.set_xticklabels(labels)
        ax2.legend()
        ax2.set_ylim(0, 110)
        
        # 값 표시
        for rect in rects1 + rects2:
            height = rect.get_height()
            ax2.annotate(f'{height:.1f}%', xy=(rect.get_x() + rect.get_width() / 2, height),
                        xytext=(0, 3), textcoords="offset points", ha='center', va='bottom')

        plt.tight_layout()
        plt.savefig(OUTPUT_FIG, dpi=300)
        print(f"그래프 저장 완료: {OUTPUT_FIG}")
    else:
        print("데이터 없음")