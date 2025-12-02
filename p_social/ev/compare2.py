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
OUTPUT_FIG = os.path.join(SAVE_DIR, 'algorithm_comparison_all.png')
CSV_FILENAME = os.path.join(SAVE_DIR, 'acn_data_1week.csv')

# ---------------------------------------------------------
# 1. 설정 (Configuration)
# ---------------------------------------------------------
MAX_CHARGING_RATE = 6.6
TIME_INTERVAL = 5
TARGET_START_DATE = '2019-10-01'
TARGET_END_DATE = '2019-10-08'

# [핵심] 전력망 용량 제한 (kW)
# FCFS 피크의 40~50% 수준으로 설정하여 차이를 극대화
GRID_CAPACITY_LIMIT = 20.0 

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
# 3. 알고리즘 시뮬레이션 엔진
# ---------------------------------------------------------

# (A) Online Heuristics (FCFS / EDF)
def run_online_simulation(ev_data, time_index, capacity_limit, algorithm='FCFS'):
    t_len = len(time_index)
    dt_hours = TIME_INTERVAL / 60.0
    total_load = np.zeros(t_len)
    
    # EV 상태 초기화
    ev_states = []
    for i, row in ev_data.iterrows():
        ev_states.append({
            'arrival_idx': time_index.searchsorted(row['connectionTime']),
            'departure_idx': time_index.searchsorted(row['disconnectTime']),
            'energy_needed': row['kWhDelivered'],
            'energy_delivered': 0.0,
            'max_rate': MAX_CHARGING_RATE
        })
        
    for t in range(t_len):
        active_evs = [ev for ev in ev_states 
                      if ev['arrival_idx'] <= t < ev['departure_idx'] and ev['energy_needed'] > 0.001]
        
        if not active_evs: continue
            
        # 정렬 기준
        if algorithm == 'FCFS':
            active_evs.sort(key=lambda x: x['arrival_idx'])
        elif algorithm == 'EDF':
            active_evs.sort(key=lambda x: x['departure_idx'])
            
        current_load = 0
        for ev in active_evs:
            if current_load >= capacity_limit: break
            
            available = capacity_limit - current_load
            req_power = ev['energy_needed'] / dt_hours
            power = min(ev['max_rate'], available, req_power)
            
            current_load += power
            total_load[t] += power
            ev['energy_needed'] -= (power * dt_hours)
            ev['energy_delivered'] += (power * dt_hours)
            if ev['energy_needed'] < 0: ev['energy_needed'] = 0

    # 결과 집계
    total_delivered = sum(ev['energy_delivered'] for ev in ev_states)
    total_requested = sum(ev['energy_delivered'] + ev['energy_needed'] for ev in ev_states)
    fully_charged = sum(1 for ev in ev_states if ev['energy_needed'] < 0.1)
    
    return {
        'load': total_load,
        'delivered': total_delivered,
        'requested': total_requested,
        'full_count': fully_charged,
        'total_evs': len(ev_states)
    }

# (B) Offline Optimal (Valley Filling with Capacity Constraint)
def run_offline_optimal(ev_data, time_index, capacity_limit):
    # 기존 Valley Filling에 "Capacity Limit"을 Hard Constraint로 추가한 버전
    # Iterative Water-filling 방식을 쓰되, 상한선을 capacity_limit으로 자름
    
    t_len = len(time_index)
    total_load = np.zeros(t_len)
    dt_hours = TIME_INTERVAL / 60.0
    
    ev_profiles = []
    for _, row in ev_data.iterrows():
        start = max(0, time_index.searchsorted(row['connectionTime']))
        end = min(t_len, time_index.searchsorted(row['disconnectTime']))
        if start >= end: continue
        
        req_energy = row['kWhDelivered']
        # 초기화: 균등 분배 (단, 용량 제한 고려 안 함 -> 반복문에서 해결)
        profile = np.full(end - start, 0.0) 
        ev_profiles.append({'start': start, 'end': end, 'profile': profile, 'energy': req_energy})

    # Iterative Optimization
    for _ in range(20): # 반복 횟수
        for ev in ev_profiles:
            s, e = ev['start'], ev['end']
            total_load[s:e] -= ev['profile'] # 나를 뺌
            background = total_load[s:e]
            
            # Water filling with TWO upper bounds:
            # 1. EV Max Rate (6.6kW)
            # 2. Grid Remaining Capacity (Limit - Background Load)
            
            # 이진 탐색으로 수위(Water Level) 찾기
            low, high = np.min(background), np.max(background) + MAX_CHARGING_RATE
            best_profile = ev['profile']
            
            for _ in range(10):
                mid = (low + high) / 2
                # 충전 가능 높이 = 물높이(mid) - 배경높이
                # 단, 충전속도제한과 전력망용량제한을 동시에 만족해야 함
                
                # 제약조건 1: 0 <= 파워 <= Max Rate
                # 제약조건 2: 파워 <= Grid Limit - Background
                max_grid_power = np.maximum(0, capacity_limit - background)
                effective_max = np.minimum(MAX_CHARGING_RATE, max_grid_power)
                
                proposed = np.clip(mid - background, 0, effective_max)
                energy = np.sum(proposed) * dt_hours
                
                if energy < ev['energy']:
                    low = mid
                else:
                    high = mid
                    best_profile = proposed
            
            ev['profile'] = best_profile
            total_load[s:e] += best_profile # 나를 다시 더함

    # 결과 집계
    total_delivered = sum(np.sum(ev['profile']) * dt_hours for ev in ev_profiles)
    # Offline Optimal은 개별 차량 만족도보다는 전체 시스템 효율을 보므로 fully_charged는 근사치일 수 있음
    fully_charged = sum(1 for ev in ev_profiles if np.sum(ev['profile']) * dt_hours >= ev['energy'] - 0.1)
    
    return {
        'load': total_load,
        'delivered': total_delivered,
        'full_count': fully_charged,
        'total_evs': len(ev_profiles) # 유효 데이터 기준
    }

# ---------------------------------------------------------
# 4. 메인 실행
# ---------------------------------------------------------
if __name__ == "__main__":
    ev_df = get_ev_data(CSV_FILENAME)
    
    if not ev_df.empty:
        start = pd.to_datetime(TARGET_START_DATE, utc=True)
        end = pd.to_datetime(TARGET_END_DATE, utc=True) + pd.Timedelta(days=1)
        time_index = pd.date_range(start, end, freq=f'{TIME_INTERVAL}min')
        
        print(f"Simulation with Grid Limit: {GRID_CAPACITY_LIMIT} kW")
        
        # 1. Run Simulations
        res_fcfs = run_online_simulation(ev_df, time_index, GRID_CAPACITY_LIMIT, 'FCFS')
        res_edf = run_online_simulation(ev_df, time_index, GRID_CAPACITY_LIMIT, 'EDF')
        res_opt = run_offline_optimal(ev_df, time_index, GRID_CAPACITY_LIMIT)
        
        # 2. Print Results
        print(f"[FCFS] Delivered: {res_fcfs['delivered']:.1f} kWh, Full: {res_fcfs['full_count']}/{res_fcfs['total_evs']}")
        print(f"[EDF ] Delivered: {res_edf['delivered']:.1f} kWh, Full: {res_edf['full_count']}/{res_edf['total_evs']}")
        print(f"[OPT ] Delivered: {res_opt['delivered']:.1f} kWh, Full: {res_opt['full_count']}/{res_opt['total_evs']}")
        
        # 3. Plotting
        fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(12, 10))
        
        # (Top) Load Profile Comparison
        ax1.plot(time_index, res_fcfs['load'], label='FCFS (Uncoordinated)', color='orange', alpha=0.6)
        ax1.plot(time_index, res_edf['load'], label='Online EDF', color='blue', alpha=0.8)
        ax1.plot(time_index, res_opt['load'], label='Offline Optimal', color='green', linestyle=':', linewidth=2)
        
        ax1.axhline(y=GRID_CAPACITY_LIMIT, color='red', linestyle='--', label='Grid Capacity Limit')
        
        ax1.set_ylabel('Power (kW)', fontsize=12)
        ax1.set_title(f'Load Profile Comparison under Capacity Limit ({GRID_CAPACITY_LIMIT} kW)', fontsize=14)
        ax1.legend(loc='upper right')
        ax1.xaxis.set_major_formatter(mdates.DateFormatter('%m-%d'))
        ax1.grid(True, alpha=0.3)
        
        # (Bottom) Performance Metrics Bar Chart
        labels = ['FCFS', 'Online EDF', 'Offline Optimal']
        delivered_pct = [
            (res_fcfs['delivered'] / res_fcfs['requested']) * 100,
            (res_edf['delivered'] / res_edf['requested']) * 100,
            (res_opt['delivered'] / res_fcfs['requested']) * 100 # OPT도 동일 요청량 기준
        ]
        full_pct = [
            (res_fcfs['full_count'] / res_fcfs['total_evs']) * 100,
            (res_edf['full_count'] / res_edf['total_evs']) * 100,
            (res_opt['full_count'] / res_opt['total_evs']) * 100
        ]
        
        x = np.arange(len(labels))
        width = 0.35
        
        rects1 = ax2.bar(x - width/2, delivered_pct, width, label='Energy Delivered (%)', color='lightgray')
        rects2 = ax2.bar(x + width/2, full_pct, width, label='Fully Charged Vehicles (%)', color=['orange', 'royalblue', 'forestgreen'])
        
        ax2.set_ylabel('Percentage (%)', fontsize=12)
        ax2.set_title('Service Quality Comparison', fontsize=14)
        ax2.set_xticks(x)
        ax2.set_xticklabels(labels, fontsize=11)
        ax2.legend()
        ax2.set_ylim(0, 115)
        
        # Value Labels
        for rect in rects1 + rects2:
            height = rect.get_height()
            ax2.annotate(f'{height:.1f}%', xy=(rect.get_x() + rect.get_width() / 2, height),
                        xytext=(0, 3), textcoords="offset points", ha='center', va='bottom', fontsize=10)

        plt.tight_layout()
        
        if not os.path.exists(SAVE_DIR):
            try: os.makedirs(SAVE_DIR)
            except: pass
            
        plt.savefig(OUTPUT_FIG, dpi=300)
        print(f"[SUCCESS] 그래프 저장 완료: {OUTPUT_FIG}")
        # plt.show()
    else:
        print("[ERROR] 데이터가 없습니다.")