import numpy as np
from scipy.optimize import linprog
import random

def check_offline_feasibility(T, P_limit, evs):
    """
    Offline Optimal (LP) 가능 여부 확인
    """
    num_evs = len(evs)
    c = np.zeros(num_evs * T) 
    
    # 1. Power Limit
    A_ub = []
    b_ub = []
    for t in range(T):
        row = np.zeros(num_evs * T)
        for i in range(num_evs):
            row[i * T + t] = 1
        A_ub.append(row)
        b_ub.append(P_limit)
        
    # 2. Energy Demand
    A_eq = []
    b_eq = []
    for i, ev in enumerate(evs):
        row = np.zeros(num_evs * T)
        for t in range(T):
            row[i * T + t] = 1
        A_eq.append(row)
        b_eq.append(ev['e'])
        
    # 3. Bounds
    bounds = []
    for i, ev in enumerate(evs):
        for t in range(T):
            if ev['a'] <= t < ev['d']:
                bounds.append((0, ev['r_bar']))
            else:
                bounds.append((0, 0))

    res = linprog(c, A_ub=A_ub, b_ub=b_ub, A_eq=A_eq, b_eq=b_eq, bounds=bounds, method='highs')
    return res.success

def find_minimal_feasible_capacity(T, evs):
    """
    Offline Optimal이 가능한 최소 P를 이분 탐색으로 찾음
    """
    low = 0.0
    high = len(evs) * 1.0 
    optimal_p = high
    
    # 정밀한 P값을 찾기 위해 반복
    for _ in range(25): 
        mid = (low + high) / 2
        if check_offline_feasibility(T, mid, evs):
            optimal_p = mid
            high = mid
        else:
            low = mid
            
    return optimal_p

def solve_sLLF(T, P_limit, evs, verbose=False):
    """ sLLF 시뮬레이션 """
    # 정밀도 보정을 위해 float 변환
    live_evs = [{'id': i, 'e_rem': float(ev['e']), **ev} for i, ev in enumerate(evs)]
    
    if verbose:
        print(f"\n--- [Verification] Running sLLF with P={P_limit:.5f} ---")

    for t in range(T):
        active_indices = [i for i, ev in enumerate(live_evs) if ev['a'] <= t < ev['d'] and ev['e_rem'] > 1e-6]
        
        if not active_indices:
            continue
            
        # Laxity 계산
        laxities = {}
        for i in active_indices:
            ev = live_evs[i]
            l_i = (ev['d'] - t) - (ev['e_rem'] / ev['r_bar'])
            laxities[i] = l_i
            if l_i < -1e-4: 
                if verbose: print(f"[sLLF Fail] EV {i} Negative Laxity at t={t}")
                return False

        # Threshold L 계산
        total_demand_limit = sum([min(live_evs[i]['r_bar'], live_evs[i]['e_rem']) for i in active_indices])
        target_power = min(P_limit, total_demand_limit)
        
        L_min, L_max = -50.0, 50.0
        best_L = L_min
        
        for _ in range(50):
            L = (L_min + L_max) / 2
            current_sum = sum([
                max(0, min(ev['r_bar'] * (L - laxities[i] + 1), min(ev['r_bar'], ev['e_rem'])))
                for i in active_indices
            ])
            if current_sum > target_power:
                L_max = L
            else:
                L_min = L
                best_L = L
        
        # 충전 실행
        for i in active_indices:
            ev = live_evs[i]
            r_val = max(0, min(ev['r_bar'] * (best_L - laxities[i] + 1), min(ev['r_bar'], ev['e_rem'])))
            live_evs[i]['e_rem'] -= r_val
            if verbose:
                print(f"t={t} | EV{i}: Charged {r_val:.3f}, Rem {live_evs[i]['e_rem']:.3f}, Laxity {laxities[i]:.3f}")

    failures = [i for i, ev in enumerate(live_evs) if ev['e_rem'] > 1e-4]
    if verbose and failures:
        print(f"[sLLF Result] FAILED EVs: {failures}")
    elif verbose:
        print(f"[sLLF Result] SUCCESS")
        
    return len(failures) == 0

def generate_random_scenario():
    """ 랜덤 시나리오 (t=0 도착 고정) """
    num_evs = random.randint(3, 5) # 차량 수
    T_horizon = random.randint(4, 8) # 시간 범위
    evs = []
    
    for i in range(num_evs):
        a = 0 
        d = random.randint(2, T_horizon) 
        r_bar = 1.0
        
        # 물리적 한계 내에서 타이트하게 수요 생성
        max_possible = (d - a) * r_bar
        min_demand = max_possible * 0.4 
        e = random.uniform(min_demand, max_possible)
        
        evs.append({'a': a, 'd': d, 'e': e, 'r_bar': r_bar})
        
    return T_horizon, evs

# --- 메인 루프 ---
print("Searching for sLLF failure cases (checking Offline Feasibility)...")
attempt = 0

while True:
    attempt += 1
    T, evs = generate_random_scenario()
    
    # 1. Offline Optimal이 가능한 최소 P 찾기
    min_feasible_P = find_minimal_feasible_capacity(T, evs)
    
    # P가 1.0 이하면 sLLF도 최적이므로 스킵 (멀티프로세서 상황 유도)
    if min_feasible_P <= 1.01:
        continue

    # 2. sLLF 시도
    success = solve_sLLF(T, min_feasible_P, evs, verbose=False)
    
    # 3. 실패 발견 시 상세 검증
    if not success:
        print(f"\n{'='*20} FOUND FAILURE CASE (Attempt {attempt}) {'='*20}")
        print(f"Scenario: T={T}, Optimal Capacity P={min_feasible_P:.5f}")
        for i, ev in enumerate(evs):
            print(f"  EV {i}: Deadline={ev['d']}, Demand={ev['e']:.3f}")

        # [검증 1] sLLF 상세 로그 출력
        solve_sLLF(T, min_feasible_P, evs, verbose=True)
        
        # [검증 2] Offline Optimal 재확인
        print(f"\n--- [Verification] Check Offline Optimal Feasibility ---")
        is_offline_possible = check_offline_feasibility(T, min_feasible_P, evs)
        
        if is_offline_possible:
            print(">>> Offline Optimal Result: ✅ SUCCESS (Feasible)")
            print(">>> Conclusion: Offline can schedule this, but sLLF failed.")
        else:
            print(">>> Offline Optimal Result: ❌ IMPOSSIBLE (Something is wrong with calculation)")
            
        break
        
    if attempt % 100 == 0:
        print(f"... scanned {attempt} scenarios ...")