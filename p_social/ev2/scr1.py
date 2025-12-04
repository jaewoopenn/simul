import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import os
import copy

# EV8 문서 기준 Fig.4


# ---------------------------------------------------------
# 1. 설정 (Configuration)
# ---------------------------------------------------------
SAVE_DIR = '/users/jaewoo/data/acn'
CSV_FILENAME = os.path.join(SAVE_DIR, 'acn_data_1week.csv')
OUTPUT_FIG = os.path.join(SAVE_DIR, 'fig4_efficiency_real_data.png')

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
# 3. 알고리즘별 로직 함수 (Modularized)
# ---------------------------------------------------------

def solve_opt_oracle(active, capacity_limit, current_time):
    """
    [Offline Optimal Proxy] EDF with Intelligent Shedding (True Upper Bound Logic for SCR)
    - 목표: Service Completion Rate(완료 갯수) 극대화
    - 전략:
      1. 기본적으로 '마감 시간(EDF)' 순서로 처리하여 급한 차를 살림.
      2. 하지만 용량이 부족하면, 현재 목록에서 '에너지가 가장 많이 필요한(Heavy)' 차량을 제거.
         -> 이유: 무거운 차 1대를 버려서 가벼운 차 여러 대를 살리는 것이 SCR(갯수)엔 이득임.
    """
    # 1. 물리적 가망성 체크 (기본 필터링)
    candidates = []
    for e in active:
        remaining = e['energy_needed'] - e['energy_delivered']
        if remaining <= 0.001:
            # 이미 완료된 차량은 유지 (전력 소모 없음)
            candidates.append(e)
            continue
            
        rem_time_hours = (e['departure_idx'] - current_time) * DT_HOURS
        max_possible_charge = rem_time_hours * e['max_rate']
        
        # 물리적으로 불가능하면 제외
        if remaining > max_possible_charge + 0.001:
            continue
        
        candidates.append(e)
    
    # 2. 과부하 해결 (Iterative Shedding)
    while True:
        total_demand = 0.0
        # 현재 후보들에게 필요한 전력(Max Rate) 계산
        for e in candidates:
            remaining = e['energy_needed'] - e['energy_delivered']
            if remaining <= 0.001: continue
            
            # 빨리 보내기 위해 Max Rate 요구
            p_req = min(e['max_rate'], remaining / DT_HOURS)
            total_demand += p_req
            
        if total_demand <= capacity_limit + 0.001:
            break
            
        # 용량 초과! -> 누구를 버릴까? 
        # 전략: "가장 에너지 많이 먹는 놈"을 버린다 (Maximize Count)
        # 단, 이미 완료된 차는 버리면 안됨.
        
        # (남은 에너지)가 가장 큰 차량 찾기
        candidates.sort(key=lambda x: (x['energy_needed'] - x['energy_delivered']), reverse=True)
        
        # 완료 안 된 차 중에서 가장 무거운 놈 제거
        popped = False
        for i, cand in enumerate(candidates):
            rem = cand['energy_needed'] - cand['energy_delivered']
            if rem > 0.001:
                candidates.pop(i)
                popped = True
                break
        
        if not popped: break # 제거할 차가 없으면 중단 (희귀 케이스)

    # 3. 최종 할당 (생존자들에게 배분)
    # 정렬은 EDF로 하는 것이 일반적이나, 여기선 이미 Capacity 안으로 들어왔으므로 순서 상관없음
    # 하지만 시뮬레이션 일관성을 위해 EDF 정렬
    candidates.sort(key=lambda x: x['departure_idx'])
    
    allocations = {e['id']: 0.0 for e in active} # 전체 0으로 초기화
    current_load = 0.0
    
    # 후보 리스트에 있는 차들에게만 전력 할당
    candidate_ids = set(c['id'] for c in candidates)
    
    for e in active:
        if e['id'] not in candidate_ids:
            continue
            
        remaining = e['energy_needed'] - e['energy_delivered']
        if remaining <= 0.001: continue
        
        p_req = min(e['max_rate'], remaining / DT_HOURS)
        
        # 위에서 검증했으므로 capacity_limit 내에 들어옴
        allocations[e['id']] = p_req
        current_load += p_req
        
    return allocations


def solve_fcfs(active, capacity_limit):
    """
    [Baseline] FCFS (First-Come, First-Served)
    - 도착 시간 순서대로 할당.
    """
    # 도착 시간 오름차순
    active.sort(key=lambda x: x['arrival_idx'])
    
    allocations = {e['id']: 0.0 for e in active}
    current_load = 0.0
    
    for e in active:
        # FCFS는 '공평하게'가 아니라 '먼저 온 사람'에게 자원을 줌
        # 하지만 여기서도 빨리 내보내기 위해 Max Rate를 쓰는게 맞나?
        # 보통 FCFS는 Demand Response 없이 그냥 Max로 꽂음.
        remaining = e['energy_needed'] - e['energy_delivered']
        if remaining <= 0: continue

        p_req = min(e['max_rate'], remaining / DT_HOURS)
        p_alloc = min(p_req, capacity_limit - current_load)
        if p_alloc < 0: p_alloc = 0
        
        allocations[e['id']] = p_alloc
        current_load += p_alloc
        if current_load >= capacity_limit: break
        
    return allocations

def solve_llf(active, capacity_limit, current_time):
    """
    [Competitor] Pure LLF (Least Laxity First)
    - 여유 시간(Laxity)이 적은 순서대로 할당.
    """
    # Laxity 계산
    for e in active:
        rem_time = (e['departure_idx'] - current_time) * DT_HOURS
        rem_charge_time = (e['energy_needed'] - e['energy_delivered']) / e['max_rate']
        e['laxity'] = rem_time - rem_charge_time
    
    # Laxity 오름차순 (긴급한 순)
    active.sort(key=lambda x: x['laxity'])
    
    allocations = {e['id']: 0.0 for e in active}
    current_load = 0.0
    
    for e in active:
        remaining = e['energy_needed'] - e['energy_delivered']
        if remaining <= 0: continue
        
        # LLF도 마감을 맞추기 위해 필요한 최소량이 아니라,
        # 긴급한 차부터 '최대한' 줘서 Laxity를 확보하는게 유리함
        p_req = min(e['max_rate'], remaining / DT_HOURS)
        
        p_alloc = min(p_req, capacity_limit - current_load)
        if p_alloc < 0: p_alloc = 0
        
        allocations[e['id']] = p_alloc
        current_load += p_alloc
        if current_load >= capacity_limit: break
        
    return allocations

def solve_sgo(active, capacity_limit, current_time):
    """
    [Proposed] SGO Framework (Adaptive Hybrid)
    - Step 1: 혼잡도에 따른 Adaptive Minimum Guarantee
    - Step 2: Urgency(Laxity) 기반 Surplus Allocation
    """
    allocations = {e['id']: 0.0 for e in active}
    current_load = 0.0
    
    # --- [Step 1] Adaptive Min Guarantee ---
    total_max_demand = sum([e['max_rate'] for e in active])
    if total_max_demand == 0: total_max_demand = 0.001
    
    congestion_ratio = total_max_demand / capacity_limit
    
    # Adaptive Rule
    p_min_base = 1.0 
    if congestion_ratio > 2.0:
        p_min_adaptive = 0.0  # 극심한 혼잡
    elif congestion_ratio > 1.2:
        p_min_adaptive = 0.5  # 혼잡
    else:
        p_min_adaptive = p_min_base # 여유
    
    # 1단계 할당
    for e in active:
        remaining = e['energy_needed'] - e['energy_delivered']
        if remaining <= 0: continue

        p_req = min(e['max_rate'], remaining / DT_HOURS)
        p_g = min(p_req, p_min_adaptive)
        
        p_alloc = min(p_g, capacity_limit - current_load)
        if p_alloc < 0: p_alloc = 0
        
        allocations[e['id']] = p_alloc
        current_load += p_alloc

    # --- [Step 2] Surplus Allocation (Urgency-based) ---
    remaining_cap = capacity_limit - current_load
    
    if remaining_cap > 0.01:
        # Laxity 계산
        for e in active:
            rem_time = (e['departure_idx'] - current_time) * DT_HOURS
            rem_charge_time = (e['energy_needed'] - e['energy_delivered']) / e['max_rate']
            e['laxity'] = rem_time - rem_charge_time
        
        # 긴급한 순 정렬
        active.sort(key=lambda x: x['laxity'])
        
        for e in active:
            p_curr = allocations[e['id']]
            remaining = e['energy_needed'] - e['energy_delivered']
            if remaining <= 0: continue

            p_req = min(e['max_rate'], remaining / DT_HOURS)
            p_needed = p_req - p_curr
            
            if p_needed > 0:
                p_add = min(p_needed, remaining_cap)
                allocations[e['id']] += p_add
                current_load += p_add
                remaining_cap -= p_add
                if remaining_cap <= 0: break
                
    return allocations

# ---------------------------------------------------------
# 4. 통합 시뮬레이션 엔진
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
    
    # --- Main Time Loop ---
    for t in range(t_len):
        active = [e for e in evs 
                  if e['arrival_idx'] <= t < e['departure_idx'] 
                  and (e['energy_needed'] - e['energy_delivered']) > 0.001]
        
        if not active: continue
        
        # 알고리즘 선택 및 실행
        if algo_type == 'OPT':
            allocations = solve_opt_oracle(active, capacity_limit, t) # Updated Logic
        elif algo_type == 'FCFS':
            allocations = solve_fcfs(active, capacity_limit)
        elif algo_type == 'LLF':
            allocations = solve_llf(active, capacity_limit, t)
        elif algo_type == 'SGO':
            allocations = solve_sgo(active, capacity_limit, t)
        else:
            allocations = {}

        # 상태 업데이트 (Energy Accumulation)
        for e in active:
            if e['id'] in allocations:
                p = allocations[e['id']]
                e['energy_delivered'] += p * DT_HOURS

    # --- Metric Calculation (SCR) ---
    success_count = 0
    total_energy = 0
    for e in evs:
        total_energy += e['energy_delivered']
        if e['energy_needed'] > 0:
            if (e['energy_delivered'] / e['energy_needed']) >= 0.95:
                success_count += 1
            
    scr = (success_count / len(evs)) * 100 if evs else 0
    return scr, total_energy

# ---------------------------------------------------------
# 5. 메인 실행
# ---------------------------------------------------------
if __name__ == "__main__":
    print(">>> 데이터 로딩 중...")
    ev_df = get_ev_data(CSV_FILENAME)
    
    if not ev_df.empty:
        print(f">>> 데이터 로드 완료: {len(ev_df)} sessions")
        
        # 실제 데이터 규모에 맞는 Peak 설정
        ESTIMATED_PEAK = 100.0 
        print(f">>> Estimated Peak set to: {ESTIMATED_PEAK} kW")
        
        scenarios_pct = [25, 35, 50, 70]
        # results_scr = {'OPT': [], 'SGO': [], 'LLF': [], 'FCFS': []}
        results_scr = {'SGO': [], 'LLF': [], 'FCFS': []}
        
        print(">>> 시나리오 시뮬레이션 시작 (Metric: Service Completion Rate)...")
        for pct in scenarios_pct:
            limit = ESTIMATED_PEAK * (pct / 100.0)
            print(f"  - Simulating Grid Limit: {pct}% ({limit:.1f} kW)")
            
            # 4가지 알고리즘 실행
            # results_scr['OPT'].append(run_simulation(ev_df, limit, 'OPT')[0])
            results_scr['SGO'].append(run_simulation(ev_df, limit, 'SGO')[0])
            results_scr['LLF'].append(run_simulation(ev_df, limit, 'LLF')[0])
            results_scr['FCFS'].append(run_simulation(ev_df, limit, 'FCFS')[0])
            
        # -----------------------------------------------------
        # 그래프 그리기
        # -----------------------------------------------------
        x = np.arange(len(scenarios_pct))
        width = 0.2
        
        fig, ax = plt.subplots(figsize=(10, 6))
        
        # ax.bar(x - 1.5*width, results_scr['OPT'], width, label='Offline Optimal (SRPT)', color='black', alpha=0.2, hatch='//') 
        ax.bar(x - 0.5*width, results_scr['SGO'], width, label='Proposed SGO (Adaptive)', color='#1976D2', edgecolor='black', zorder=3)
        ax.bar(x + 0.5*width, results_scr['LLF'], width, label='Pure LLF', color='#4CAF50', alpha=0.8, zorder=2)
        ax.bar(x + 1.5*width, results_scr['FCFS'], width, label='FCFS (Baseline)', color='#BDBDBD', zorder=2)
        
        ax.set_xlabel('Grid Capacity Constraint (% of Peak Demand)', fontsize=12, fontweight='bold')
        ax.set_ylabel('Service Completion Rate (% of Fully Charged)', fontsize=12, fontweight='bold')
        ax.set_title('Fig 4. Service Completion Rate Comparison (Real Data)', fontsize=14, pad=20)
        ax.set_xticks(x)
        ax.set_xticklabels([f'{s}%' for s in scenarios_pct], fontsize=11)
        ax.set_ylim(0, 110)
        
        ax.legend(loc='lower right', fontsize=10, frameon=True)
        ax.grid(axis='y', linestyle='--', alpha=0.5)
        
        idx = 0
        sgo_val = results_scr['SGO'][idx]
        fcfs_val = results_scr['FCFS'][idx]
        diff = sgo_val - fcfs_val
        
        if diff > 0:
            ax.annotate(f'+{diff:.1f}%p\nImprovement', 
                        xy=(x[idx] - 0.5*width, sgo_val), 
                        xytext=(x[idx] - 0.5*width, sgo_val + 15),
                        ha='center', fontsize=11, fontweight='bold', color='#D32F2F',
                        arrowprops=dict(arrowstyle='->', lw=1.5, color='#D32F2F'))
        
        plt.tight_layout()
        plt.savefig(OUTPUT_FIG, dpi=300)
        print(f"[SUCCESS] 그래프 저장 완료: {OUTPUT_FIG}")
        plt.show()

    else:
        print("[ERROR] 데이터 프레임이 비어있습니다. CSV 파일을 확인해주세요.")