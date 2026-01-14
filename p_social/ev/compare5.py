import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import os


# ---------------------------------------------------------
# 1. 설정 (Configuration)
# ---------------------------------------------------------
SAVE_DIR = '/users/jaewoo/data/acn'
OUTPUT_FIG = os.path.join(SAVE_DIR, 'satisfaction_comparison_3_algo.png')
CSV_FILENAME = os.path.join(SAVE_DIR, 'acn_data_1week.csv')

MAX_CHARGING_RATE = 6.6
TIME_INTERVAL = 5
TARGET_START_DATE = '2019-10-01'
TARGET_END_DATE = '2019-10-08'

# [핵심] 총량 제한 (kW) - 아주 빡빡하게 설정하여 차이를 극대화
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
# 4. 메인 실행 및 시각화 (평균 만족도 추가 버전)
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
        
        # 평균 만족도 계산
        avg_sat = {
            'FCFS': sum(sat_fcfs) / len(sat_fcfs) if sat_fcfs else 0,
            'EDF': sum(sat_edf) / len(sat_edf) if sat_edf else 0,
            'LLF': sum(sat_llf) / len(sat_llf) if sat_llf else 0
        }

        print(f"\n[Result 1] Fully Charged Rate (완충 성공률)")
        print(f"  FCFS: {full_fcfs}/{total_fcfs} ({(full_fcfs/total_fcfs)*100:.1f}%)")
        print(f"  EDF : {full_edf}/{total_edf} ({(full_edf/total_edf)*100:.1f}%)")
        print(f"  LLF : {full_llf}/{total_llf} ({(full_llf/total_llf)*100:.1f}%)")

        print(f"\n[Result 2] Average Satisfaction (평균 만족도)")
        print(f"  FCFS: {avg_sat['FCFS']:.1f}%")
        print(f"  EDF : {avg_sat['EDF']:.1f}%")
        print(f"  LLF : {avg_sat['LLF']:.1f}%")

        # 그래프 그리기 (3개 차트로 확장)
        fig, (ax1, ax2, ax3) = plt.subplots(1, 3, figsize=(18, 6))
        labels = ['FCFS', 'EDF', 'LLF']
        colors = ['#ff9999', '#66b3ff', '#99ff99']

        # 1. Box Plot (분포)
        sns.boxplot(data=[sat_fcfs, sat_edf, sat_llf], ax=ax1, palette=colors)
        ax1.set_xticklabels(labels)
        ax1.set_title('Satisfaction Distribution', fontsize=13)
        ax1.set_ylabel('Satisfaction (%)')
        ax1.set_ylim(-5, 105)

        # 2. Bar Chart (완충 성공률)
        full_rates = [(full_fcfs/total_fcfs)*100, (full_edf/total_edf)*100, (full_llf/total_llf)*100]
        bars2 = ax2.bar(labels, full_rates, color=colors, alpha=0.8)
        ax2.set_title('Service Completion Rate (%)', fontsize=13)
        ax2.set_ylim(0, 110)
        for bar in bars2:
            ax2.text(bar.get_x() + bar.get_width()/2., bar.get_height() + 1, f'{bar.get_height():.1f}%', ha='center', fontweight='bold')

        # 3. Bar Chart (평균 만족도) - NEW!
        avg_rates = [avg_sat['FCFS'], avg_sat['EDF'], avg_sat['LLF']]
        bars3 = ax3.bar(labels, avg_rates, color=colors, alpha=0.8)
        ax3.set_title('Average Satisfaction (%)', fontsize=13)
        ax3.set_ylim(0, 110)
        for bar in bars3:
            ax3.text(bar.get_x() + bar.get_width()/2., bar.get_height() + 1, f'{bar.get_height():.1f}%', ha='center', fontweight='bold')

        plt.tight_layout()
        plt.show()