import random
import time
import copy
import math

# 1. Original sLLF (Bisection Search, O(N * K))
def original_sllf_step(active_evs, total_capacity, max_rate=1.0):
    if not active_evs: return

    # 이론상 최대 전력 요구량 확인
    max_possible = sum(min(max_rate, ev['energy_needed']) for ev in active_evs)
    target_power = min(total_capacity, max_possible)
    
    # 이분 탐색 범위
    laxities = [ev['laxity'] for ev in active_evs]
    low = min(laxities) - 2.0
    high = max(laxities) + 2.0
    
    # 50회 반복 (정밀도 보장)
    final_L = low
    for _ in range(50):
        mid_L = (low + high) / 2
        current_sum = 0
        for ev in active_evs:
            # r = [L - (l_i - 1)] bounded by [0, cap]
            raw = max_rate * (mid_L - ev['laxity'] + 1) # max_rate=1.0 가정
            cap = min(max_rate, ev['energy_needed'])
            current_sum += max(0.0, min(cap, raw))
            
        if current_sum < target_power:
            low = mid_L
        else:
            high = mid_L
    
    final_L = (low + high) / 2
    
    # 결과 적용
    for ev in active_evs:
        raw = max_rate * (final_L - ev['laxity'] + 1)
        cap = min(max_rate, ev['energy_needed'])
        ev['rate'] = max(0.0, min(cap, raw))

# 2. Optimized sLLF (Slope Sweeping, O(N log N))
def optimized_sllf_step(active_evs, total_capacity, max_rate=1.0):
    if not active_evs: return

    # 이벤트 생성: (위치 L, 기울기 변화량)
    events = []
    for ev in active_evs:
        # 충전 시작점 (L = l_i - 1)
        start = ev['laxity'] - 1.0
        # 충전 한계점 (L = l_i - 1 + cap)
        cap = min(max_rate, ev['energy_needed'])
        end = start + cap
        
        events.append((start, 1))
        events.append((end, -1))
    
    # 정렬 (핵심 부하)
    events.sort(key=lambda x: x[0])
    
    # 스위핑
    current_slope = 0
    current_power = 0.0
    prev_L = events[0][0]
    final_L = events[-1][0] # 기본값 (전력이 남을 경우)
    
    for L, slope_change in events:
        dist = L - prev_L
        added_power = current_slope * dist
        
        # 목표 용량을 초과하는 순간 포착
        if current_power + added_power > total_capacity + 1e-9:
            needed = total_capacity - current_power
            if current_slope > 0:
                final_L = prev_L + (needed / current_slope)
            else:
                final_L = prev_L # Should not happen if capacity > 0
            break
            
        current_power += added_power
        current_slope += slope_change
        prev_L = L
        
    # 결과 적용 (O(N))
    for ev in active_evs:
        cap = min(max_rate, ev['energy_needed'])
        raw = final_L - (ev['laxity'] - 1.0)
        ev['rate'] = max(0.0, min(cap, raw))

# --- 테스트 실행기 ---
def run_benchmark():
    # 1. 정확성 검증 (100회 랜덤)
    print("=== 1. 정확성 검증 (Correctness Check) ===")
    mismatches = 0
    for i in range(100):
        # 랜덤 데이터 생성
        num_evs = random.randint(5, 20)
        capacity = num_evs * 0.5
        evs = [{'id': k, 'laxity': random.uniform(0, 10), 'energy_needed': random.uniform(0.5, 5.0)} for k in range(num_evs)]
        
        evs_orig = copy.deepcopy(evs)
        evs_opt = copy.deepcopy(evs)
        
        original_sllf_step(evs_orig, capacity)
        optimized_sllf_step(evs_opt, capacity)
        
        # 결과 비교
        for e1, e2 in zip(evs_orig, evs_opt):
            if abs(e1['rate'] - e2['rate']) > 1e-5:
                mismatches += 1
                break
    
    if mismatches == 0:
        print(">> PASS: 두 알고리즘의 결과가 완벽하게 일치합니다.")
    else:
        print(f">> FAIL: {mismatches}건의 불일치 발생.")

    # 2. 성능 벤치마크 (대규모 데이터)
    print("\n=== 2. 속도 벤치마크 (Performance Benchmark) ===")
    sizes = [10, 100, 1000, 10000]
    print(f"{'EV Count':<10} | {'Original (sec)':<15} | {'Optimized (sec)':<15} | {'Speedup':<10}")
    print("-" * 60)
    
    for n in sizes:
        # 데이터 준비
        evs = [{'id': k, 'laxity': random.uniform(0, 10), 'energy_needed': random.uniform(0.5, 5.0)} for k in range(n)]
        capacity = n * 0.5
        
        evs1 = copy.deepcopy(evs)
        evs2 = copy.deepcopy(evs)
        
        # Original 측정
        start = time.time()
        original_sllf_step(evs1, capacity)
        dur_orig = time.time() - start
        
        # Optimized 측정
        start = time.time()
        optimized_sllf_step(evs2, capacity)
        dur_opt = time.time() - start
        
        speedup = dur_orig / dur_opt if dur_opt > 0 else 0.0
        print(f"{n:<10} | {dur_orig:.5f}         | {dur_opt:.5f}         | {speedup:.1f}x")

run_benchmark()