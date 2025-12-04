import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import os

# EV8 문서 기준 Fig.6

# ---------------------------------------------------------
# 1. 설정 (Configuration) 
# ---------------------------------------------------------
SAVE_DIR = '/users/jaewoo/data/acn'
CSV_FILENAME = os.path.join(SAVE_DIR, 'acn_data_1week.csv')
OUTPUT_FIG = os.path.join(SAVE_DIR, 'fig6_sensitivity_analysis.png')

MAX_CHARGING_RATE = 6.6
TIME_INTERVAL = 5  # minutes
DT_HOURS = TIME_INTERVAL / 60.0

TARGET_START_DATE = '2019-10-01'
TARGET_END_DATE = '2019-10-08'

# ---------------------------------------------------------
# 2. 데이터 로드 및 전처리
# ---------------------------------------------------------
def get_ev_data(filename):
    if not os.path.exists(filename):
        print(f"[ERROR] 데이터 파일이 존재하지 않습니다: {filename}")
        return pd.DataFrame()

    df = pd.read_csv(filename)
    
    for col in ['connectionTime', 'disconnectTime']:
        df[col] = pd.to_datetime(df[col], utc=True)
    
    mask = (df['connectionTime'] >= TARGET_START_DATE) & (df['connectionTime'] <= f"{TARGET_END_DATE} 23:59:59")
    df = df.loc[mask].copy()
    
    df = df[df['kWhDelivered'] > 0]
    df['parking_duration'] = (df['disconnectTime'] - df['connectionTime']).dt.total_seconds() / 3600.0
    df = df[df['parking_duration'] > (5/60)] 

    return df.sort_values(by='connectionTime')

# ---------------------------------------------------------
# 3. 메트릭 계산 함수
# ---------------------------------------------------------
def calculate_metrics(evs):
    # 1. Service Completion Rate (SCR)
    success_count = 0
    ratios = []
    
    for e in evs:
        if e['energy_needed'] > 0:
            ratio = min(1.0, e['energy_delivered'] / e['energy_needed'])
            ratios.append(ratio)
            if ratio >= 0.95:
                success_count += 1
            
    scr = (success_count / len(evs)) * 100 if evs else 0
    
    # 2. Jain's Fairness Index
    if not ratios:
        fairness = 0
    else:
        ratios = np.array(ratios)
        sum_r = np.sum(ratios)
        sum_sq_r = np.sum(ratios ** 2)
        fairness = (sum_r ** 2) / (len(ratios) * sum_sq_r) if sum_sq_r > 0 else 0
        
    return scr, fairness

# ---------------------------------------------------------
# 4. 알고리즘 로직 (재사용)
# ---------------------------------------------------------
def solve_fcfs(active, capacity_limit):
    active.sort(key=lambda x: x['arrival_idx'])
    allocations = {e['id']: 0.0 for e in active}
    current_load = 0.0
    for e in active:
        p_req = min(e['max_rate'], (e['energy_needed'] - e['energy_delivered'])/DT_HOURS)
        p_alloc = min(p_req, capacity_limit - current_load)
        if p_alloc < 0: p_alloc = 0
        allocations[e['id']] = p_alloc
        current_load += p_alloc
        if current_load >= capacity_limit: break
    return allocations

def solve_llf(active, capacity_limit, current_time):
    for e in active:
        rem_time = (e['departure_idx'] - current_time) * DT_HOURS
        rem_charge_time = (e['energy_needed'] - e['energy_delivered']) / e['max_rate']
        e['laxity'] = rem_time - rem_charge_time
    active.sort(key=lambda x: x['laxity'])
    allocations = {e['id']: 0.0 for e in active}
    current_load = 0.0
    for e in active:
        p_req = min(e['max_rate'], (e['energy_needed'] - e['energy_delivered'])/DT_HOURS)
        p_alloc = min(p_req, capacity_limit - current_load)
        if p_alloc < 0: p_alloc = 0
        allocations[e['id']] = p_alloc
        current_load += p_alloc
        if current_load >= capacity_limit: break
    return allocations

def solve_sgo(active, capacity_limit, current_time):
    allocations = {e['id']: 0.0 for e in active}
    current_load = 0.0
    
    total_max = sum([e['max_rate'] for e in active])
    if total_max == 0: total_max = 0.001
    congestion = total_max / capacity_limit
    
    # Adaptive Rule
    if congestion > 2.0: p_min = 0.0
    elif congestion > 1.2: p_min = 0.5
    else: p_min = 1.0
    
    # Step 1
    for e in active:
        p_req = min(e['max_rate'], (e['energy_needed'] - e['energy_delivered'])/DT_HOURS)
        p_g = min(p_req, p_min)
        p_alloc = min(p_g, capacity_limit - current_load)
        if p_alloc < 0: p_alloc = 0
        allocations[e['id']] = p_alloc
        current_load += p_alloc
        
    # Step 2
    rem_cap = capacity_limit - current_load
    if rem_cap > 0.01:
        for e in active:
            rem_time = (e['departure_idx'] - current_time) * DT_HOURS
            rem_charge_time = (e['energy_needed'] - e['energy_delivered']) / e['max_rate']
            e['laxity'] = rem_time - rem_charge_time
        active.sort(key=lambda x: x['laxity'])
        for e in active:
            p_curr = allocations[e['id']]
            p_req = min(e['max_rate'], (e['energy_needed'] - e['energy_delivered'])/DT_HOURS)
            p_need = p_req - p_curr
            if p_need > 0:
                add = min(p_need, rem_cap)
                allocations[e['id']] += add
                current_load += add
                rem_cap -= add
                if rem_cap <= 0: break
    return allocations

# ---------------------------------------------------------
# 5. 시뮬레이션 엔진
# ---------------------------------------------------------
def run_simulation(ev_df, capacity_limit, algo_type):
    start = pd.to_datetime(TARGET_START_DATE, utc=True)
    end = pd.to_datetime(TARGET_END_DATE, utc=True) + pd.Timedelta(days=1)
    time_index = pd.date_range(start, end, freq=f'{TIME_INTERVAL}min')
    t_len = len(time_index)
    
    evs = []
    for _, row in ev_df.iterrows():
        evs.append({
            'id': _,
            'arrival_idx': time_index.searchsorted(row['connectionTime']),
            'departure_idx': time_index.searchsorted(row['disconnectTime']),
            'energy_needed': row['kWhDelivered'],
            'energy_delivered': 0.0,
            'max_rate': MAX_CHARGING_RATE
        })
    
    for t in range(t_len):
        active = [e for e in evs 
                  if e['arrival_idx'] <= t < e['departure_idx'] 
                  and (e['energy_needed'] - e['energy_delivered']) > 0.001]
        
        if not active: continue
        
        if algo_type == 'FCFS': allocations = solve_fcfs(active, capacity_limit)
        elif algo_type == 'LLF': allocations = solve_llf(active, capacity_limit, t)
        elif algo_type == 'SGO': allocations = solve_sgo(active, capacity_limit, t)
        else: allocations = {}

        for e in active:
            if e['id'] in allocations:
                e['energy_delivered'] += allocations[e['id']] * DT_HOURS

    return calculate_metrics(evs)

# ---------------------------------------------------------
# 6. 메인 실행 및 그래프
# ---------------------------------------------------------
if __name__ == "__main__":
    ev_df = get_ev_data(CSV_FILENAME)
    
    if not ev_df.empty:
        ESTIMATED_PEAK = 100.0 
        
        # 민감도 분석을 위해 촘촘한 시나리오 설정 (30% ~ 100%, 10% 단위)
        scenarios_pct = [20,30, 40, 50, 60, 70, 80]
        
        results = {
            'SGO': {'scr': [], 'fair': []},
            'LLF': {'scr': [], 'fair': []},
            'FCFS': {'scr': [], 'fair': []}
        }
        
        print(">>> Sensitivity Analysis Started...")
        for pct in scenarios_pct:
            limit = ESTIMATED_PEAK * (pct / 100.0)
            print(f"  - Simulating Limit: {pct}%")
            
            for algo in ['SGO', 'LLF', 'FCFS']:
                scr, fair = run_simulation(ev_df, limit, algo)
                results[algo]['scr'].append(scr)
                results[algo]['fair'].append(fair)
            
        # --- Plotting (Two Subplots) ---
        fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(14, 6))
        
        # Subplot 1: Efficiency (SCR)
        ax1.plot(scenarios_pct, results['SGO']['scr'], marker='o', label='SGO (Proposed)', color='#1976D2', linewidth=2)
        ax1.plot(scenarios_pct, results['LLF']['scr'], marker='s', label='Pure LLF', color='#4CAF50', linestyle='--')
        ax1.plot(scenarios_pct, results['FCFS']['scr'], marker='^', label='FCFS', color='#757575', linestyle=':')
        
        ax1.set_xlabel('Grid Capacity Limit (% of Peak)', fontsize=12, fontweight='bold')
        ax1.set_ylabel('Service Completion Rate (%)', fontsize=12, fontweight='bold')
        ax1.set_title('(a) Efficiency Trend (Resilience)', fontsize=14)
        ax1.grid(True, linestyle='--', alpha=0.5)
        ax1.legend()
        ax1.set_ylim(0, 105)
        
        # Subplot 2: Equity (Fairness Index)
        ax2.plot(scenarios_pct, results['SGO']['fair'], marker='o', label='SGO (Proposed)', color='#1976D2', linewidth=2)
        ax2.plot(scenarios_pct, results['LLF']['fair'], marker='s', label='Pure LLF', color='#4CAF50', linestyle='--')
        ax2.plot(scenarios_pct, results['FCFS']['fair'], marker='^', label='FCFS', color='#757575', linestyle=':')
        
        ax2.set_xlabel('Grid Capacity Limit (% of Peak)', fontsize=12, fontweight='bold')
        ax2.set_ylabel("Jain's Fairness Index", fontsize=12, fontweight='bold')
        ax2.set_title('(b) Fairness Trend (Equity)', fontsize=14)
        ax2.grid(True, linestyle='--', alpha=0.5)
        ax2.legend()
        ax2.set_ylim(0.4, 1.05)
        
        plt.suptitle('Fig 6. Impact of Capacity Constraints on Efficiency and Equity', fontsize=16, y=1.02)
        plt.tight_layout()
        
        plt.savefig(OUTPUT_FIG, dpi=300, bbox_inches='tight')
        print(f"[SUCCESS] 그래프 저장 완료: {OUTPUT_FIG}")
        plt.show()
    else:
        print("[ERROR] 데이터 없음")