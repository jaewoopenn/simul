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

OUTPUT_FIG = os.path.join(SAVE_DIR, 'phase_constrained_profile.png')
CSV_FILENAME = os.path.join(SAVE_DIR, 'acn_data_1week.csv')

# ---------------------------------------------------------
# 1. 설정 (Configuration)
# ---------------------------------------------------------
MAX_CHARGING_RATE = 6.6
TIME_INTERVAL = 5
TARGET_START_DATE = '2019-10-01'
TARGET_END_DATE = '2019-10-08'

PHASES = ['A', 'B', 'C']
PHASE_CAPACITY_LIMIT = 10.0

# ---------------------------------------------------------
# 2. 데이터 로드 및 전처리
# ---------------------------------------------------------
def get_ev_data(filename):
    try:
        df = pd.read_csv(filename)
        
        time_cols = ['connectionTime', 'disconnectTime']
        for col in time_cols:
            if col in df.columns:
                df[col] = pd.to_datetime(df[col], utc=True)
        
        mask = (df['connectionTime'] >= TARGET_START_DATE) & (df['connectionTime'] <= f"{TARGET_END_DATE} 23:59:59")
        df = df.loc[mask].copy()
        
        df = df[df['kWhDelivered'] > 0]
        
        return df.sort_values(by='connectionTime')
    except Exception as e:
        print(f"[ERROR] 데이터 로드 오류: {e}")
        return pd.DataFrame()

# ---------------------------------------------------------
# 3. 가상 인프라 매핑 (Round-Robin Phase Allocation)
# ---------------------------------------------------------
def assign_phases_to_stations(df):
    stations = sorted(df['stationID'].unique())
    station_phase_map = {}
    for i, station in enumerate(stations):
        phase = PHASES[i % 3] 
        station_phase_map[station] = phase
    return station_phase_map

# ---------------------------------------------------------
# 4. 알고리즘 구현
# ---------------------------------------------------------

# (A) Uncoordinated (ASAP)
def calculate_uncoordinated(ev_data, time_index):
    total_load = pd.Series(0.0, index=time_index)
    
    for _, row in ev_data.iterrows():
        arrival = row['connectionTime']
        energy = row['kWhDelivered']
        
        duration_hours = energy / MAX_CHARGING_RATE
        finish = arrival + pd.Timedelta(hours=duration_hours)
        
        if arrival > time_index[-1] or finish < time_index[0]:
            continue
            
        mask = (total_load.index >= arrival) & (total_load.index < finish)
        total_load[mask] += MAX_CHARGING_RATE
        
    return total_load

# (B) Offline Optimal (Valley Filling - Simplified)
def calculate_offline_optimal(ev_data, time_index):
    t_len = len(time_index)
    total_load_arr = np.zeros(t_len)
    
    energy_per_slot = MAX_CHARGING_RATE * (TIME_INTERVAL / 60.0)
    
    ev_profiles = []
    
    for _, row in ev_data.iterrows():
        start_idx = time_index.searchsorted(row['connectionTime'])
        end_idx = time_index.searchsorted(row['disconnectTime'])
        
        start_idx = max(0, start_idx)
        end_idx = min(t_len, end_idx)
        
        if start_idx >= end_idx: continue
        
        req_energy = row['kWhDelivered']
        duration_slots = end_idx - start_idx
        
        avg_power = min(MAX_CHARGING_RATE, req_energy / (duration_slots * (TIME_INTERVAL/60.0)))
        profile = np.full(duration_slots, avg_power)
        
        ev_profiles.append({
            'start': start_idx, 'end': end_idx,
            'profile': profile, 'energy': req_energy
        })
        total_load_arr[start_idx:end_idx] += profile

    # Iterative Water-filling
    iterations = 15
    for it in range(iterations):
        for ev in ev_profiles:
            s, e = ev['start'], ev['end']
            old_profile = ev['profile']
            
            total_load_arr[s:e] -= old_profile
            background_load = total_load_arr[s:e]
            
            target_energy = ev['energy']
            
            low, high = np.min(background_load), np.max(background_load) + MAX_CHARGING_RATE
            best_profile = old_profile
            
            for _ in range(10):
                mid = (low + high) / 2
                proposed_power = np.clip(mid - background_load, 0, MAX_CHARGING_RATE)
                proposed_energy = np.sum(proposed_power) * (TIME_INTERVAL/60.0)
                
                if proposed_energy < target_energy:
                    low = mid
                else:
                    high = mid
                    best_profile = proposed_power
            
            ev['profile'] = best_profile
            total_load_arr[s:e] += best_profile
            
    return pd.Series(total_load_arr, index=time_index)

# (C) Phase-Aware EDF
def calculate_phase_aware_edf(ev_data, time_index, phase_capacity_limit, station_map):
    t_len = len(time_index)
    dt_hours = TIME_INTERVAL / 60.0
    
    loads = {'Total': np.zeros(t_len), 'A': np.zeros(t_len), 'B': np.zeros(t_len), 'C': np.zeros(t_len)}
    
    ev_states = []
    for _, row in ev_data.iterrows():
        sid = row['stationID']
        if sid not in station_map: continue
        
        start_idx = max(0, time_index.searchsorted(row['connectionTime']))
        end_idx = min(t_len, time_index.searchsorted(row['disconnectTime']))
        
        if start_idx >= end_idx: continue

        ev_states.append({
            'arrival_idx': start_idx,
            'departure_idx': end_idx,
            'remaining_energy': row['kWhDelivered'],
            'max_rate': MAX_CHARGING_RATE,
            'phase': station_map[sid]
        })
        
    for t in range(t_len):
        active_evs = [ev for ev in ev_states 
                      if ev['arrival_idx'] <= t < ev['departure_idx'] and ev['remaining_energy'] > 0.001]
        
        if not active_evs: continue
            
        active_evs.sort(key=lambda x: x['departure_idx']) 
        
        current_phase_load = {'A': 0.0, 'B': 0.0, 'C': 0.0}
        
        for ev in active_evs:
            p = ev['phase']
            if current_phase_load[p] >= phase_capacity_limit: continue
            
            available = phase_capacity_limit - current_phase_load[p]
            req_power = ev['remaining_energy'] / dt_hours
            power = min(ev['max_rate'], available, req_power)
            
            current_phase_load[p] += power
            ev['remaining_energy'] -= (power * dt_hours)
            if ev['remaining_energy'] < 0: ev['remaining_energy'] = 0
            
        loads['A'][t] = current_phase_load['A']
        loads['B'][t] = current_phase_load['B']
        loads['C'][t] = current_phase_load['C']
        loads['Total'][t] = sum(current_phase_load.values())

    return loads

# ---------------------------------------------------------
# 5. 메인 실행
# ---------------------------------------------------------
if __name__ == "__main__":
    ev_df = get_ev_data(CSV_FILENAME)
    
    if not ev_df.empty:
        start = pd.to_datetime(TARGET_START_DATE, utc=True)
        end = pd.to_datetime(TARGET_END_DATE, utc=True) + pd.Timedelta(days=1)
        time_index = pd.date_range(start, end, freq=f'{TIME_INTERVAL}min')
        
        # 1. Baseline 계산 (ASAP)
        load_asap = calculate_uncoordinated(ev_df, time_index)
        asap_peak = load_asap.max()
        print(f"[Result] ASAP Peak: {asap_peak:.2f} kW")
        
        # 2. Benchmark 계산 (Offline Optimal)
        load_opt = calculate_offline_optimal(ev_df, time_index)
        opt_peak = load_opt.max()
        print(f"[Result] Optimal Peak: {opt_peak:.2f} kW")
        
        # 3. Phase 할당 및 용량 제한 설정
        station_map = assign_phases_to_stations(ev_df)
        
        PHASE_CAPACITY_LIMIT = (asap_peak * 0.6) / 3.0
        
        # 4. Proposed 계산 (EDF)
        results_edf = calculate_phase_aware_edf(ev_df, time_index, PHASE_CAPACITY_LIMIT, station_map)
        edf_peak = max(results_edf['Total'])
        print(f"[Result] EDF Peak: {edf_peak:.2f} kW (Limit: {PHASE_CAPACITY_LIMIT*3:.1f} kW)")
        
        # 5. 그래프 그리기
        plt.figure(figsize=(14, 10))
        
        # (Top) Total Load Comparison
        plt.subplot(2, 1, 1)
        plt.fill_between(load_asap.index, load_asap.values, color='gray', alpha=0.2, label='Uncoordinated (ASAP)')
        plt.plot(load_opt.index, load_opt.values, color='green', linestyle=':', linewidth=2, label='Offline Optimal')
        plt.plot(time_index, results_edf['Total'], color='blue', linewidth=2, label='Online EDF (Proposed)')
        
        plt.axhline(y=PHASE_CAPACITY_LIMIT * 3, color='red', linestyle='--', label='Grid Capacity Limit')
        
        plt.title(f'Total Load Profile Comparison ({TARGET_START_DATE} ~ {TARGET_END_DATE})', fontsize=14)
        plt.ylabel('Total Power (kW)', fontsize=12)
        plt.legend(loc='upper right')
        plt.grid(True, alpha=0.3)
        plt.gca().xaxis.set_major_formatter(mdates.DateFormatter('%m-%d'))
        
        # (Bottom) Phase Balance (EDF Only)
        plt.subplot(2, 1, 2)
        colors = {'A': 'tab:blue', 'B': 'tab:orange', 'C': 'tab:green'}
        for p in PHASES:
            plt.plot(time_index, results_edf[p], label=f'Phase {p}', color=colors[p], alpha=0.7, linewidth=1.5)
            
        plt.axhline(y=PHASE_CAPACITY_LIMIT, color='red', linestyle='--', label='Phase Limit')
        plt.title('Load Balance per Phase (EDF)', fontsize=14)
        plt.ylabel('Phase Power (kW)', fontsize=12)
        plt.xlabel('Date', fontsize=12)
        plt.legend(loc='upper right')
        plt.grid(True, alpha=0.3)
        plt.gca().xaxis.set_major_formatter(mdates.DateFormatter('%m-%d %H:%M'))
        
        plt.tight_layout()
        plt.savefig(OUTPUT_FIG, dpi=300)
        print(f"[SUCCESS] 그래프 저장 완료: {OUTPUT_FIG}")
    else:
        print("[ERROR] 데이터가 없습니다.")