import random
import copy

# =========================================================
# 1. Original sLLF 구현 (논문의 Algorithm 1 & Eq 16, 17)
# =========================================================
def original_sllf_step(active_evs, total_capacity, max_rate=1.0):
    """
    Paper Implementation:
    Uses Bisection Search to find optimal parameter L(t).
    Rates are assigned based on Eq (16): r = [max_rate * (L - laxity + 1)] bounded.
    """
    if not active_evs:
        return

    # 목표 전력량 설정: 전체 용량 vs 실제 필요한 최대 충전량 중 작은 값
    # Eq (17)의 우변: min(P(t), sum(min(r_bar, e_i(t))))
    max_demand_sum = sum(min(max_rate, ev['energy_needed']) for ev in active_evs)
    target_power = min(total_capacity, max_demand_sum)
    
    # 이분 탐색 범위 설정 (Laxity 주변 값)
    laxities = [ev['laxity'] for ev in active_evs]
    low = min(laxities) - 2.0
    high = max(laxities) + 2.0
    
    # Bisection Search (50회 반복이면 충분히 수렴)
    final_L = low
    for _ in range(50):
        mid_L = (low + high) / 2
        
        current_sum = 0
        for ev in active_evs:
            # Eq (16): r_i(t) 계산
            raw_val = max_rate * (mid_L - ev['laxity'] + 1)
            # Projection [0, min(max_rate, energy)]
            upper_bound = min(max_rate, ev['energy_needed'])
            r_i = max(0.0, min(upper_bound, raw_val))
            current_sum += r_i
            
        if current_sum < target_power:
            low = mid_L # 전력이 더 필요함 -> L을 높여야 함 (rate 증가)
        else:
            high = mid_L # 전력을 줄여야 함 -> L을 낮춰야 함
            
    final_L = (low + high) / 2
    
    # 최종 결정된 L값으로 충전량 할당
    for ev in active_evs:
        raw_val = max_rate * (final_L - ev['laxity'] + 1)
        upper_bound = min(max_rate, ev['energy_needed'])
        ev['rate'] = max(0.0, min(upper_bound, raw_val))

# =========================================================
# 2. Simplified sLLF 구현 (Iterative Water-filling)
# =========================================================
def simplified_sllf_step(active_evs, total_capacity, max_rate=1.0):
    """
    Simplified Implementation:
    Iteratively fills power to the 'most urgent' EVs (lowest laxity).
    Handles caps (energy needed < max_rate) correctly to match Original sLLF.
    """
    # Laxity 기준 오름차순 정렬
    sorted_evs = sorted(active_evs, key=lambda x: x['laxity'])
    
    # 초기화 및 Step Cap(이번 턴 한도) 설정
    for ev in sorted_evs:
        ev['rate'] = 0.0
        ev['step_cap'] = min(max_rate, ev['energy_needed'])
        
    remaining = total_capacity

    # Water-filling Loop
    while remaining > 1e-6:
        # 현재 충전량을 반영한 '미래 Laxity' 계산
        futures = [ev['laxity'] - 1.0 + ev['rate'] for ev in sorted_evs]
        
        # 더 충전받을 수 있는(Not Capped) 차들 중 가장 급한 그룹 찾기
        candidates = [i for i, ev in enumerate(sorted_evs) 
                      if ev['rate'] < ev['step_cap'] - 1e-6]
        
        if not candidates:
            break # 모든 차가 한도까지 받음
            
        # 그중 가장 작은 미래 Laxity 값 찾기
        min_future = min([futures[i] for i in candidates])
        
        # 같은 높이(Laxity)에 있는 차들을 그룹핑
        target_indices = [i for i in candidates 
                          if abs(futures[i] - min_future) < 1e-6]
        
        # == 충전량(Step Size) 결정 ==
        # 1. 다음 Laxity 레벨과의 격차 (Gap)
        higher_vals = [v for v in futures if v > min_future + 1e-6]
        gap = (min(higher_vals) - min_future) if higher_vals else remaining
        
        # 2. 그룹 내 차들이 Cap에 도달하기까지 남은 여유분 중 최소값 (Headroom)
        headrooms = [sorted_evs[i]['step_cap'] - sorted_evs[i]['rate'] 
                     for i in target_indices]
        min_headroom = min(headrooms)
        
        # 3. 남은 전력을 N명에게 공평하게 나눴을 때의 양 (Share)
        power_share = remaining / len(target_indices)
        
        # 셋 중 가장 작은 값만큼 충전
        step = min(gap, min_headroom, power_share)
        
        # 적용
        for i in target_indices:
            sorted_evs[i]['rate'] += step
            
        remaining -= (step * len(target_indices))

# =========================================================
# 3. 시뮬레이션 및 검증 로직
# =========================================================
def run_simulation_scenario(seed, num_evs_range, max_time=30):
    random.seed(seed)
    
    # --- 시나리오 생성 ---
    num_evs = random.randint(*num_evs_range)
    # 총 용량을 좀 빡빡하게 설정 (전체 차량의 60%만 커버 가능)하여 경쟁 유도
    total_capacity = num_evs * 0.6
    
    base_evs = []
    for i in range(num_evs):
        energy = random.randint(3, 10)
        buffer = random.randint(0, 5)
        deadline = energy + buffer
        base_evs.append({
            'id': f'EV{i}', 'arrival': 0, 'deadline': deadline, 
            'energy_needed': float(energy)
        })

    # --- 두 가지 알고리즘 각각 실행 ---
    results = {}
    
    for algo_name, algo_func in [("Original", original_sllf_step), 
                                 ("Simplified", simplified_sllf_step)]:
        # 데이터 깊은 복사 (독립적 실행)
        evs = copy.deepcopy(base_evs)
        time_step = 0
        
        while time_step < max_time:
            # Active EV 필터링
            active_evs = []
            for ev in evs:
                if time_step < ev['deadline'] and ev['energy_needed'] > 1e-4:
                    # Laxity 업데이트
                    rem_time = ev['deadline'] - time_step
                    ev['laxity'] = rem_time - ev['energy_needed']
                    active_evs.append(ev)
            
            # 종료 조건 확인
            unfinished_future = [ev for ev in evs if ev['energy_needed'] > 1e-4 and ev['deadline'] > time_step]
            if not active_evs and not unfinished_future:
                break
            
            # 알고리즘 수행
            if active_evs:
                algo_func(active_evs, total_capacity)
            
            # 상태 업데이트
            for ev in active_evs:
                rate = ev.get('rate', 0.0)
                ev['energy_needed'] -= rate
            
            time_step += 1
            
        # 성공 여부 판별 (모든 차의 잔여 에너지가 0에 가까우면 성공)
        success = all(ev['energy_needed'] < 1e-4 for ev in evs)
        results[algo_name] = success
        
    return results

# =========================================================
# 4. 메인 실행 부 (100회 테스트)
# =========================================================
if __name__ == "__main__":
    TEST_COUNT = 100
    stats = {
        "Both Success": 0,
        "Both Fail": 0,
        "Mismatch": 0
    }
    
    print(f"=== {TEST_COUNT}회 무작위 시나리오 검증 시작 ===")
    
    for i in range(TEST_COUNT):
        # 시드값을 i로 고정하여 매번 다른 랜덤 시나리오 생성
        res = run_simulation_scenario(seed=i, num_evs_range=(5, 10))
        
        if res["Original"] == res["Simplified"]:
            if res["Original"]:
                stats["Both Success"] += 1
            else:
                stats["Both Fail"] += 1
        else:
            stats["Mismatch"] += 1
            print(f"[Mismatch Found] Seed {i}: Original={res['Original']}, Simplified={res['Simplified']}")

    print("\n=== 검증 결과 요약 ===")
    print(f"총 테스트 횟수 : {TEST_COUNT}")
    print(f"둘 다 성공     : {stats['Both Success']}회")
    print(f"둘 다 실패     : {stats['Both Fail']}회")
    print(f"결과 불일치    : {stats['Mismatch']}회")
    
    if stats['Mismatch'] == 0:
        print("\n[결론] 축하합니다! Simplified sLLF는 Original sLLF와 완벽하게 동일합니다.")
    else:
        print("\n[결론] 일부 케이스에서 차이가 발견되었습니다. 로직 점검이 필요합니다.")