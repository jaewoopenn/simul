'''
Created on 2025. 11. 28.

@author: jaewoo
'''
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import datetime

SAVE_DIR = '/users/jaewoo/data/acn'
OUTPUT_FIG = SAVE_DIR + '/sensitivity_analysis.png'
# [수정] 새 데이터 파일명
CSV_FILENAME =  SAVE_DIR + '/202410DatasetEVOfficeParking_v0.csv'

# ---------------------------------------------------------
# 1. 설정 (Configuration)
# ---------------------------------------------------------
MAX_CHARGING_RATE = 6.6
TIME_INTERVAL = 15
TARGET_START_DATE = '2024-09-01'
TARGET_END_DATE = '2024-09-30'

# ---------------------------------------------------------
# 2. 데이터 로드 및 전처리
# ---------------------------------------------------------
def get_ev_data(filename):
    try:
        df = pd.read_csv(filename, sep=';')
        
        df.rename(columns={
            'start_datetime': 'connectionTime',
            'end_datetime': 'disconnectTime',
            'total_energy': 'kWhDelivered'
        }, inplace=True)
        
        for col in ['connectionTime', 'disconnectTime']:
            if col in df.columns:
                df[col] = pd.to_datetime(df[col])
        
        if df['kWhDelivered'].dtype == object:
             df['kWhDelivered'] = df['kWhDelivered'].astype(str).str.replace(',', '.').astype(float)

        mask = (df['connectionTime'] >= TARGET_START_DATE) & (df['connectionTime'] <= TARGET_END_DATE)
        df = df.loc[mask].copy()
        
        return df.sort_values(by='connectionTime')
    except Exception as e:
        print(f"데이터 로드 중 오류: {e}")
        return pd.DataFrame()

# ---------------------------------------------------------
# 3. EDF 시뮬레이션 엔진
# ---------------------------------------------------------
def run_edf_simulation(ev_data, time_index, grid_capacity_kw):
    t_len = len(time_index)
    dt_hours = TIME_INTERVAL / 60.0
    
    actual_peak = 0
    total_energy_requested = ev_data['kWhDelivered'].sum()
    
    # 상태 추적용 리스트 초기화
    ev_states = []
    for _, row in ev_data.iterrows():
        # 데이터 유효성 검사
        if pd.isna(row['connectionTime']) or pd.isna(row['disconnectTime']) or row['kWhDelivered'] <= 0:
            continue
            
        start_idx = time_index.searchsorted(row['connectionTime'])
        end_idx = time_index.searchsorted(row['disconnectTime'])
        
        # 유효한 시간 범위인지 확인
        if start_idx >= t_len or end_idx <= 0 or start_idx >= end_idx:
            continue

        ev_states.append({
            'arrival_idx': start_idx,
            'departure_idx': end_idx,
            'energy_needed': row['kWhDelivered'],
            'energy_received': 0.0,
            'max_rate': MAX_CHARGING_RATE
        })
        
    # Time-stepping
    for t in range(t_len):
        # 주차 중이고 충전 필요한 차량 찾기
        active_evs = [i for i, ev in enumerate(ev_states) 
                      if ev['arrival_idx'] <= t < ev['departure_idx'] and ev['energy_needed'] > 0.001]
        
        if not active_evs:
            continue
            
        # EDF 정렬 (마감 임박 순)
        active_evs.sort(key=lambda i: ev_states[i]['departure_idx'])
        
        current_load = 0
        for idx in active_evs:
            if current_load >= grid_capacity_kw:
                break
                
            available = grid_capacity_kw - current_load
            req_power = ev_states[idx]['energy_needed'] / dt_hours
            power = min(ev_states[idx]['max_rate'], available, req_power)
            
            current_load += power
            energy_step = power * dt_hours
            
            ev_states[idx]['energy_needed'] -= energy_step
            ev_states[idx]['energy_received'] += energy_step
            
        if current_load > actual_peak:
            actual_peak = current_load
            
    # 전체 만족도 계산
    total_delivered = sum(ev['energy_received'] for ev in ev_states)
    satisfaction_pct = (total_delivered / total_energy_requested) * 100 if total_energy_requested > 0 else 0
    
    return actual_peak, satisfaction_pct

# ---------------------------------------------------------
# 4. 민감도 분석 실행
# ---------------------------------------------------------
if __name__ == "__main__":
    ev_df = get_ev_data(CSV_FILENAME)
    
    if not ev_df.empty:
        # 시간 축 생성
        start = pd.to_datetime(TARGET_START_DATE)
        end = pd.to_datetime(TARGET_END_DATE) + pd.Timedelta(days=1)
        time_index = pd.date_range(start, end, freq=f'{TIME_INTERVAL}min')
        
        # 1. 기준점(Baseline Peak) 계산 (무제한 용량일 때)
        baseline_peak, _ = run_edf_simulation(ev_df, time_index, 10000)
        print(f"Baseline (Unlimited) Peak: {baseline_peak:.2f} kW")
        
        if baseline_peak == 0:
            print("데이터가 부족하여 시뮬레이션을 진행할 수 없습니다.")
            exit()

        # 2. 실험: 용량을 100% -> 30%로 줄여가며 테스트
        capacity_ratios = np.linspace(1.0, 0.3, 15)
        capacity_limits = baseline_peak * capacity_ratios
        
        results_peak = []
        results_satisfaction = []
        
        print("\nRunning Sensitivity Analysis...")
        for cap in capacity_limits:
            peak, sat = run_edf_simulation(ev_df, time_index, cap)
            results_peak.append(peak)
            results_satisfaction.append(sat)
            print(f"  Cap: {cap:.1f}kW -> Peak: {peak:.1f}kW, Sat: {sat:.1f}%")

        # 3. 그래프 그리기 (이중 축)
        fig, ax1 = plt.subplots(figsize=(10, 6))
        
        x_vals = capacity_ratios * 100
        
        # Y1: Actual Peak
        color = 'tab:blue'
        ax1.set_xlabel('Grid Capacity Constraint (% of Baseline Peak)', fontsize=12)
        ax1.set_ylabel('Actual Peak Load (kW)', color=color, fontsize=12)
        ax1.plot(x_vals, results_peak, color=color, marker='o', label='Actual Peak')
        ax1.tick_params(axis='y', labelcolor=color)
        ax1.grid(True, linestyle='--', alpha=0.5)
        
        # Y2: Satisfaction
        ax2 = ax1.twinx()
        color = 'tab:red'
        ax2.set_ylabel('User Satisfaction (% Energy Delivered)', color=color, fontsize=12)
        ax2.plot(x_vals, results_satisfaction, color=color, marker='s', linestyle='-', linewidth=2, label='Satisfaction')
        ax2.tick_params(axis='y', labelcolor=color)
        ax2.set_ylim(80, 105) 

        plt.title('Sensitivity Analysis: Impact of Capacity Limits on Peak & Satisfaction', fontsize=14)
        plt.gca().invert_xaxis() # X축 역순 (100% -> 30%)
        
        fig.tight_layout()
        plt.savefig(OUTPUT_FIG, dpi=300)
        print(f"\n그래프가 '{OUTPUT_FIG}'로 저장되었습니다.")
        plt.show()
    else:
        print("데이터프레임이 비어있습니다.")