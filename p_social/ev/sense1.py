import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import datetime


SAVE_DIR = '/users/jaewoo/data/acn'
OUTPUT_FIG=SAVE_DIR+'/sense.png'
CSV_FILENAME = SAVE_DIR+'/acn_data_caltech_20190901_20190907.csv' # 업로드된 파일명

# ---------------------------------------------------------
# 1. 설정 (Configuration)
# ---------------------------------------------------------
MAX_CHARGING_RATE = 6.6
TIME_INTERVAL = 5

# ---------------------------------------------------------
# 2. 데이터 로드 및 전처리 (기존과 동일)
# ---------------------------------------------------------
def get_ev_data():
    try:
        df = pd.read_csv(CSV_FILENAME)
        for col in ['connectionTime', 'disconnectTime', 'doneChargingTime']:
            if col in df.columns:
                df[col] = pd.to_datetime(df[col], utc=True)
        return df.sort_values(by='connectionTime')
    except:
        return pd.DataFrame()

# ---------------------------------------------------------
# 3. EDF 시뮬레이션 엔진 (입력: 용량 제한 -> 출력: 실제 피크, 만족도)
# ---------------------------------------------------------
def run_edf_simulation(ev_data, time_index, grid_capacity_kw):
    t_len = len(time_index)
    dt_hours = TIME_INTERVAL / 60.0
    
    # 결과 저장 변수
    actual_peak = 0
    total_energy_requested = ev_data['kWhDelivered'].sum()
    total_energy_delivered = 0
    
    # 상태 추적용 리스트
    ev_states = []
    for _, row in ev_data.iterrows():
        ev_states.append({
            'arrival_idx': time_index.searchsorted(row['connectionTime']),
            'departure_idx': time_index.searchsorted(row['disconnectTime']),
            'energy_needed': row['kWhDelivered'], # 초기 요구량
            'energy_received': 0.0,
            'max_rate': MAX_CHARGING_RATE
        })
        
    # Time-stepping
    for t in range(t_len):
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
            # 이번 턴에 필요한 전력 (완충까지 남은 양 / 시간) vs 최대속도 vs 망 여유분
            req_power = ev_states[idx]['energy_needed'] / dt_hours
            power = min(ev_states[idx]['max_rate'], available, req_power)
            
            current_load += power
            energy_step = power * dt_hours
            
            ev_states[idx]['energy_needed'] -= energy_step
            ev_states[idx]['energy_received'] += energy_step
            
        # 피크 갱신
        if current_load > actual_peak:
            actual_peak = current_load
            
    # 전체 만족도 계산 (총 공급량 / 총 요구량 * 100)
    total_delivered = sum(ev['energy_received'] for ev in ev_states)
    satisfaction_pct = (total_delivered / total_energy_requested) * 100
    
    return actual_peak, satisfaction_pct

# ---------------------------------------------------------
# 4. 민감도 분석 실행 (Main Loop)
# ---------------------------------------------------------
if __name__ == "__main__":
    ev_df = get_ev_data()
    
    if not ev_df.empty:
        # 시간 축 생성
        start = ev_df['connectionTime'].min().floor('D')
        end = ev_df['disconnectTime'].max().ceil('D')
        time_index = pd.date_range(start, end, freq=f'{TIME_INTERVAL}min')
        
        # 1. 기준점(Baseline) 계산: 무제한일 때의 피크 (ASAP Peak)
        # 용량을 매우 크게 주어 ASAP와 동일한 상황 연출
        baseline_peak, _ = run_edf_simulation(ev_df, time_index, 10000)
        print(f"Baseline (Unlimited) Peak: {baseline_peak:.2f} kW")
        
        # 2. 실험 시나리오: 용량을 Baseline의 100% -> 30%까지 줄여봄
        capacity_ratios = np.linspace(1.0, 0.2, 15) # 15개 구간 테스트
        capacity_limits = baseline_peak * capacity_ratios
        
        results_peak = []
        results_satisfaction = []
        
        print("\nRunning Sensitivity Analysis...")
        for cap in capacity_limits:
            peak, sat = run_edf_simulation(ev_df, time_index, cap)
            results_peak.append(peak)
            results_satisfaction.append(sat)
            print(f"  Cap: {cap:.1f}kW -> Peak: {peak:.1f}kW, Sat: {sat:.1f}%")

        # ---------------------------------------------------------
        # 5. 그래프 그리기 (Dual Axis)
        # ---------------------------------------------------------
        fig, ax1 = plt.subplots(figsize=(10, 6))
        
        # X축: Capacity Limit
        # (그래프 가독성을 위해 X축을 역순(큰 용량 -> 작은 용량)으로 하거나 비율로 표시)
        x_vals = capacity_ratios * 100 # % 단위로 변환
        
        # Y1: Actual Peak Load (Bar Chart or Line)
        color = 'tab:blue'
        ax1.set_xlabel('Grid Capacity Constraint (% of Baseline Peak)', fontsize=12)
        ax1.set_ylabel('Actual Peak Load (kW)', color=color, fontsize=12)
        ax1.plot(x_vals, results_peak, color=color, marker='o', label='Actual Peak')
        ax1.tick_params(axis='y', labelcolor=color)
        ax1.grid(True, linestyle='--', alpha=0.5)
        
        # Reference Line (y=x) : 제한 용량을 완벽히 따르는지
        # ax1.plot(x_vals, capacity_limits, 'k--', alpha=0.3, label='Limit Line')

        # Y2: Satisfaction (Line Chart)
        ax2 = ax1.twinx()  # instantiate a second axes that shares the same x-axis
        color = 'tab:red'
        ax2.set_ylabel('User Satisfaction (% Energy Delivered)', color=color, fontsize=12)
        ax2.plot(x_vals, results_satisfaction, color=color, marker='s', linestyle='-', linewidth=2, label='Satisfaction')
        ax2.tick_params(axis='y', labelcolor=color)
        ax2.set_ylim(70, 105) # 만족도는 보통 높게 나오므로 스케일 조정

        # Title & Layout
        plt.title('Sensitivity Analysis: Impact of Capacity Limits on Peak & Satisfaction', fontsize=14)
        plt.gca().invert_xaxis() # X축을 100% -> 30% 순서로 뒤집기 (오른쪽이 널널함, 왼쪽이 빡빡함)
        
        fig.tight_layout()
        plt.savefig(OUTPUT_FIG, dpi=300)
        print(f"\n그래프가 {OUTPUT_FIG}로 저장되었습니다.")
        plt.show()