import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import os

# ---------------------------------------------------------
# 1. 설정 (Configuration)
# ---------------------------------------------------------
SAVE_DIR = '/users/jaewoo/data/acn'
OUTPUT_FIG = os.path.join(SAVE_DIR, 'satisfaction_comparison_3_algo.png')
CSV_FILENAME = os.path.join(SAVE_DIR, 'acn_data_caltech_20190901_20191001.csv')

MAX_CHARGING_RATE = 6.6
TIME_INTERVAL = 2
TARGET_START_DATE = '2019-09-01'
TARGET_END_DATE = '2019-10-01'

# [핵심] 총량 제한 (kW) - 아주 빡빡하게 설정하여 알고리즘 간 차이를 유도
GRID_CAPACITY_LIMIT = 22.0 

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
# 3. 알고리즘 시뮬레이션
# ---------------------------------------------------------
def run_simulation(ev_data, time_index, capacity_limit, algorithm):
    t_len = len(time_index)
    dt_hours = TIME_INTERVAL / 60.0
    
    # EV 상태 초기화
    ev_states = []
    for i, row in ev_data.iterrows():
        ev_states.append({
            'arrival_idx': time_index.searchsorted(row['connectionTime']),
            'departure_idx': time_index.searchsorted(row['disconnectTime']),
            'energy_requested': row['kWhDelivered'],
            'energy_remaining': row['kWhDelivered'],
            'energy_delivered': 0.0,
            'max_rate': MAX_CHARGING_RATE
        })

    for t in range(t_len):
        # 현재 충전 가능한 차량들
        active_evs = [ev for ev in ev_states 
                      if ev['arrival_idx'] <= t < ev['departure_idx'] and ev['energy_remaining'] > 0.001]
        
        if not active_evs: continue
            
        # [수정] 알고리즘별 정렬 기준 설정
        if algorithm == 'FCFS':
            active_evs.sort(key=lambda x: x['arrival_idx'])
        elif algorithm == 'EDF':
            active_evs.sort(key=lambda x: x['departure_idx'])
        elif algorithm == 'LLF':
            # Laxity 계산: (현재~출차까지 남은 시간) - (남은 에너지를 채우는 데 필요한 시간)
            # Laxity가 작을수록 우선순위가 높음
            for ev in active_evs:
                remaining_time = (ev['departure_idx'] - t) * dt_hours
                required_time = ev['energy_remaining'] / ev['max_rate']
                ev['laxity'] = remaining_time - required_time
            active_evs.sort(key=lambda x: x['laxity'])
            
        current_load = 0
        for ev in active_evs:
            if current_load >= capacity_limit: break
            
            available = capacity_limit - current_load
            req_power = ev['energy_remaining'] / dt_hours
            power = min(ev['max_rate'], available, req_power)
            
            current_load += power
            ev['energy_remaining'] -= (power * dt_hours)
            ev['energy_delivered'] += (power * dt_hours)
            if ev['energy_remaining'] < 0: ev['energy_remaining'] = 0

    # 결과 집계
    satisfaction_list = []
    fully_charged_count = 0
    total_evs = 0
    
    for ev in ev_states:
        if ev['energy_requested'] > 0:
            total_evs += 1
            sat = min(100.0, (ev['energy_delivered'] / ev['energy_requested']) * 100)
            satisfaction_list.append(sat)
            if sat >= 99.0: fully_charged_count += 1
    
    return satisfaction_list, fully_charged_count, total_evs

# ---------------------------------------------------------
# 4. 메인 실행 및 시각화
# ---------------------------------------------------------
if __name__ == "__main__":
    ev_df = get_ev_data(CSV_FILENAME)
    
    if not ev_df.empty:
        start = pd.to_datetime(TARGET_START_DATE, utc=True)
        end = pd.to_datetime(TARGET_END_DATE, utc=True) + pd.Timedelta(days=1)
        time_index = pd.date_range(start, end, freq=f'{TIME_INTERVAL}min')
        
        print(f"Simulation with Grid Limit: {GRID_CAPACITY_LIMIT} kW")
        
        # 3가지 알고리즘 실행
        sat_fcfs, full_fcfs, total_fcfs = run_simulation(ev_df, time_index, GRID_CAPACITY_LIMIT, 'FCFS')
        sat_edf, full_edf, total_edf = run_simulation(ev_df, time_index, GRID_CAPACITY_LIMIT, 'EDF')
        sat_llf, full_llf, total_llf = run_simulation(ev_df, time_index, GRID_CAPACITY_LIMIT, 'LLF')
        
        print(f"[Result] Fully Charged Rate")
        print(f"  FCFS: {full_fcfs}/{total_fcfs} ({(full_fcfs/total_fcfs)*100:.1f}%)")
        print(f"  EDF : {full_edf}/{total_edf} ({(full_edf/total_edf)*100:.1f}%)")
        print(f"  LLF : {full_llf}/{total_llf} ({(full_llf/total_llf)*100:.1f}%)")

        # 그래프 그리기
        fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(15, 6))
        
        # 1. Box Plot (만족도 분포)
        data_to_plot = [sat_fcfs, sat_edf, sat_llf]
        labels = ['FCFS', 'EDF', 'LLF']
        colors = ['#ff9999', '#66b3ff', '#99ff99'] # 각각 연한 빨강, 파랑, 초록
        
        sns.boxplot(data=data_to_plot, ax=ax1, palette=colors)
        ax1.set_xticklabels(labels)
        ax1.set_title('Distribution of User Satisfaction\n(Energy Delivered / Requested)', fontsize=13)
        ax1.set_ylabel('Satisfaction (%)', fontsize=11)
        ax1.set_ylim(-5, 105)
        ax1.grid(True, axis='y', linestyle='--', alpha=0.5)
        
        # 2. Bar Chart (완충 비율)
        full_rates = [
            (full_fcfs/total_fcfs)*100, 
            (full_edf/total_edf)*100, 
            (full_llf/total_llf)*100
        ]
        bars = ax2.bar(labels, full_rates, color=colors, alpha=0.8, width=0.6)
        
        ax2.set_title('Service Completion Rate\n(% of Fully Charged Vehicles)', fontsize=13)
        ax2.set_ylabel('Fully Charged Rate (%)', fontsize=11)
        ax2.set_ylim(0, 110)
        ax2.grid(True, axis='y', linestyle='--', alpha=0.5)
        
        # 바 차트 위에 수치 표시
        for bar in bars:
            height = bar.get_height()
            ax2.text(bar.get_x() + bar.get_width()/2., height + 1,
                     f'{height:.1f}%', ha='center', va='bottom', fontsize=11, fontweight='bold')

        plt.tight_layout()
        
        if not os.path.exists(SAVE_DIR):
            try: os.makedirs(SAVE_DIR)
            except: pass
            
        plt.savefig(OUTPUT_FIG, dpi=300)
        print(f"[SUCCESS] 그래프 저장 완료: {OUTPUT_FIG}")
        plt.show()
    else:
        print("[ERROR] 데이터가 없습니다.")