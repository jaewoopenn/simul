'''
Created on 2025. 11. 29.

@author: jaewoo
'''
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.dates as mdates
import datetime
import os

SAVE_DIR = '/users/jaewoo/data/acn'
OUTPUT_FIG = os.path.join(SAVE_DIR, 'load_profile_fcfs_vs_edf.png')
CSV_FILENAME = os.path.join(SAVE_DIR, 'acn_data_1week.csv')

# ---------------------------------------------------------
# 1. 설정 (Configuration)
# ---------------------------------------------------------
MAX_CHARGING_RATE = 6.6
TIME_INTERVAL = 5
TARGET_START_DATE = '2019-10-01'
TARGET_END_DATE = '2019-10-08'

# [핵심] 전력망 용량 제한 (kW)
# 데이터 로드 후 피크의 60%로 자동 설정됨 (초기값 30.0)
GRID_CAPACITY_LIMIT = 30.0

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
# 3. 알고리즘 구현
# ---------------------------------------------------------

# (A) Uncoordinated (Baseline) - Limit 무시 (잠재적 수요 확인용)
def calculate_uncoordinated(ev_data, time_index):
    total_load = pd.Series(0.0, index=time_index)
    
    for _, row in ev_data.iterrows():
        arrival = row['connectionTime']
        energy = row['kWhDelivered']
        duration = pd.Timedelta(hours=energy / MAX_CHARGING_RATE)
        finish = arrival + duration
        
        if arrival > time_index[-1] or finish < time_index[0]: continue
            
        mask = (total_load.index >= arrival) & (total_load.index < finish)
        total_load[mask] += MAX_CHARGING_RATE
        
    return total_load

# (B) Online EDF - Limit 준수 (제안 알고리즘)
def calculate_online_edf(ev_data, time_index, limit):
    t_len = len(time_index)
    dt_hours = TIME_INTERVAL / 60.0
    total_load = np.zeros(t_len)
    
    ev_states = []
    for _, row in ev_data.iterrows():
        ev_states.append({
            'arrival_idx': time_index.searchsorted(row['connectionTime']),
            'departure_idx': time_index.searchsorted(row['disconnectTime']),
            'remaining': row['kWhDelivered']
        })
        
    for t in range(t_len):
        # Active EVs 찾기
        active = [ev for ev in ev_states 
                  if ev['arrival_idx'] <= t < ev['departure_idx'] and ev['remaining'] > 0.001]
        if not active: continue
            
        # EDF Sort (마감 임박 순)
        active.sort(key=lambda x: x['departure_idx'])
        
        current_load = 0
        for ev in active:
            if current_load >= limit: break # 용량 초과 시 중단
            
            avail = limit - current_load
            req = ev['remaining'] / dt_hours
            power = min(MAX_CHARGING_RATE, avail, req)
            
            current_load += power
            total_load[t] += power
            ev['remaining'] -= power * dt_hours
            if ev['remaining'] < 0: ev['remaining'] = 0
            
    return pd.Series(total_load, index=time_index)

# ---------------------------------------------------------
# 4. 메인 실행 및 그래프
# ---------------------------------------------------------
if __name__ == "__main__":
    ev_df = get_ev_data(CSV_FILENAME)
    
    if not ev_df.empty:
        start = pd.to_datetime(TARGET_START_DATE, utc=True)
        end = pd.to_datetime(TARGET_END_DATE, utc=True) + pd.Timedelta(days=1)
        time_index = pd.date_range(start, end, freq=f'{TIME_INTERVAL}min')
        
        # 1. Baseline 계산 (Limit 무시한 잠재 수요)
        load_fcfs_potential = calculate_uncoordinated(ev_df, time_index)
        real_peak = load_fcfs_potential.max()
        
        # [자동 설정] 용량 제한을 실제 피크의 60%로 설정
        GRID_CAPACITY_LIMIT = real_peak * 0.6
        print(f"Simulation Limit: {GRID_CAPACITY_LIMIT:.2f} kW (Original Peak: {real_peak:.2f} kW)")
        
        # 2. EDF 계산 (Limit 준수)
        load_edf = calculate_online_edf(ev_df, time_index, GRID_CAPACITY_LIMIT)
        
        # 3. Plotting
        plt.figure(figsize=(12, 6))
        
        # Baseline (Potential Demand) - 회색 영역
        plt.fill_between(load_fcfs_potential.index, load_fcfs_potential.values, color='gray', alpha=0.3, label='Uncoordinated Potential Demand')
        
        # Capacity Limit - 빨간 점선 (Hard Constraint)
        plt.axhline(y=GRID_CAPACITY_LIMIT, color='red', linestyle='--', linewidth=2, label='Grid Capacity Limit')
        
        # Online EDF - 파란색 실선 (Actual Load)
        plt.plot(load_edf.index, load_edf.values, color='blue', linewidth=1.5, label='Online EDF (Proposed)')
        
        plt.title(f'Load Profile Comparison: Potential Demand vs. Managed Load ({TARGET_START_DATE} ~ {TARGET_END_DATE})', fontsize=14)
        plt.ylabel('Power Demand (kW)', fontsize=12)
        plt.xlabel('Date', fontsize=12)
        plt.legend(loc='upper right', frameon=True)
        plt.grid(True, alpha=0.3)
        plt.gca().xaxis.set_major_formatter(mdates.DateFormatter('%m-%d'))
        
        plt.tight_layout()
        
        if not os.path.exists(SAVE_DIR):
            try: os.makedirs(SAVE_DIR)
            except: pass
            
        plt.savefig(OUTPUT_FIG, dpi=300)
        print(f"[SUCCESS] 그래프 저장 완료: {OUTPUT_FIG}")
        plt.show()
    else:
        print("[ERROR] 데이터가 없습니다.")