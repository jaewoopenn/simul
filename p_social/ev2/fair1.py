import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import os


# EV8 문서 기준 Fig.5


# ---------------------------------------------------------
# 1. 설정 (Configuration)
# ---------------------------------------------------------
SAVE_DIR = '/users/jaewoo/data/acn'
CSV_FILENAME = os.path.join(SAVE_DIR, 'acn_data_1week.csv')
OUTPUT_FIG = os.path.join(SAVE_DIR, 'fig5_fairness_real_data.png')

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
# 3. Metric: Jain's Fairness Index
# ---------------------------------------------------------
def calculate_jains_index(evs):
    """
    Jain's Fairness Index 계산
    지표: 각 차량의 에너지 충족률 (Delivered / Needed)
    범위: 1/n (최악) ~ 1.0 (최상, 모두 동일한 비율로 받음)
    """
    ratios = []
    for e in evs:
        if e['energy_needed'] > 0:
            # 1.0을 초과하는 경우(과충전)는 1.0으로 클리핑 (공정성 왜곡 방지)
            r = min(1.0, e['energy_delivered'] / e['energy_needed'])
            ratios.append(r)
    
    if not ratios:
        return 0.0
    
    ratios = np.array(ratios)
    sum_r = np.sum(ratios)
    sum_sq_r = np.sum(ratios ** 2)
    
    if sum_sq_r == 0:
        return 0.0
        
    j_index = (sum_r ** 2) / (len(ratios) * sum_sq_r)
    return j_index

# ---------------------------------------------------------
# 4. 알고리즘 로직 (간소화)
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
    
    # [Step 1] Adaptive Min Guarantee
    total_max = sum([e['max_rate'] for e in active])
    if total_max == 0: total_max = 0.001
    congestion = total_max / capacity_limit
    
    # Adaptive Rule (Fairness를 위해 너무 0으로 낮추지는 않음)
    if congestion > 2.0: p_min = 0.2     # 최소한의 연결 유지 (0.2kW)
    elif congestion > 1.2: p_min = 1.0
    else: p_min = 1.5
    
    for e in active:
        p_req = min(e['max_rate'], (e['energy_needed'] - e['energy_delivered'])/DT_HOURS)
        p_g = min(p_req, p_min)
        p_alloc = min(p_g, capacity_limit - current_load)
        if p_alloc < 0: p_alloc = 0
        allocations[e['id']] = p_alloc
        current_load += p_alloc
        
    # [Step 2] Surplus (LLF)
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
def run_simulation_fairness(ev_df, capacity_limit, algo_type):
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
        
        if algo_type == 'FCFS':
            allocations = solve_fcfs(active, capacity_limit)
        elif algo_type == 'LLF':
            allocations = solve_llf(active, capacity_limit, t)
        elif algo_type == 'SGO':
            allocations = solve_sgo(active, capacity_limit, t)
        else:
            allocations = {}

        for e in active:
            if e['id'] in allocations:
                e['energy_delivered'] += allocations[e['id']] * DT_HOURS

    # 결과 리턴: Jain's Fairness Index
    return calculate_jains_index(evs)

# ---------------------------------------------------------
# 6. 메인 실행 및 그래프
# ---------------------------------------------------------
if __name__ == "__main__":
    ev_df = get_ev_data(CSV_FILENAME)
    
    if not ev_df.empty:
        ESTIMATED_PEAK = 100.0 
        scenarios_pct = [20, 30, 50, 70]
        results = {'SGO': [], 'LLF': [], 'FCFS': []}
        
        print(">>> Fairness Analysis Simulation Start...")
        for pct in scenarios_pct:
            limit = ESTIMATED_PEAK * (pct / 100.0)
            print(f"  - Limit: {pct}%")
            
            results['SGO'].append(run_simulation_fairness(ev_df, limit, 'SGO'))
            results['LLF'].append(run_simulation_fairness(ev_df, limit, 'LLF'))
            results['FCFS'].append(run_simulation_fairness(ev_df, limit, 'FCFS'))
            
        # --- Plotting ---
        x = np.arange(len(scenarios_pct))
        width = 0.25
        
        fig, ax = plt.subplots(figsize=(10, 6))
        
        # 색상 및 스타일 설정
        bars1 = ax.bar(x - width, results['SGO'], width, label='Proposed SGO', color='#1976D2', edgecolor='black', zorder=3)
        bars2 = ax.bar(x, results['LLF'], width, label='Pure LLF', color='#4CAF50', alpha=0.8, zorder=2)
        bars3 = ax.bar(x + width, results['FCFS'], width, label='FCFS (Baseline)', color='#BDBDBD', zorder=2)
        
        ax.set_xlabel('Grid Capacity Constraint (% of Peak Demand)', fontsize=12, fontweight='bold')
        ax.set_ylabel("Jain's Fairness Index", fontsize=12, fontweight='bold')
        ax.set_title("Fig 5. Service Fairness Index Comparison (Equity)", fontsize=14, pad=20)
        ax.set_xticks(x)
        ax.set_xticklabels([f'{s}%' for s in scenarios_pct], fontsize=11)
        ax.set_ylim(0, 1.1)
        
        ax.legend(loc='lower right', fontsize=10, frameon=True)
        ax.grid(axis='y', linestyle='--', alpha=0.5)
        
        # 30% 구간 SGO vs LLF 비교 주석
        idx = 0
        sgo_val = results['SGO'][idx]
        llf_val = results['LLF'][idx]
        
        if sgo_val > llf_val + 0.05:
            ax.annotate(f'SGO maintains\nHigh Equity', 
                        xy=(x[idx] - width, sgo_val), 
                        xytext=(x[idx] - width, sgo_val + 0.15),
                        ha='center', fontsize=10, fontweight='bold', color='#0D47A1',
                        arrowprops=dict(arrowstyle='->', connectionstyle="arc3", color='#0D47A1'))
            
            ax.annotate(f'LLF drops\n(Starvation)', 
                        xy=(x[idx], llf_val), 
                        xytext=(x[idx], llf_val + 0.25),
                        ha='center', fontsize=10, color='#2E7D32',
                        arrowprops=dict(arrowstyle='->', connectionstyle="arc3", color='#2E7D32'))

        plt.tight_layout()
        plt.savefig(OUTPUT_FIG, dpi=300)
        print(f"[SUCCESS] 그래프 저장 완료: {OUTPUT_FIG}")
        plt.show()
    else:
        print("[ERROR] 데이터 없음")