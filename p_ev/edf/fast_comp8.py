import os
import pickle
import copy
import time
import matplotlib.pyplot as plt
from dataclasses import dataclass
from typing import List
from multiprocessing import Pool, cpu_count

# ---------------------------------------------------------
# 1. 설정 및 상수
# ---------------------------------------------------------
DATA_SAVE_PATH = "/Users/jaewoo/data/ev/cab/data"  # 실제 데이터 경로에 맞게 수정 필요

TRIAL_NUM = 100
TIME_STEP = 1
EPSILON = 1e-6

TOTAL_STATION_POWER = 4
MAX_EV_POWER = 1.0

STRESS_START = 0
STRESS_NUM = 10

@dataclass
class EVRequest:
    ev_id: int
    arrival: int
    required_energy: float
    deadline: int
    remaining: float

    def __repr__(self):
        return f"EV{self.ev_id}(A={self.arrival}, Rem={self.remaining:.2f}, D={self.deadline})"

# ---------------------------------------------------------
# 2. 알고리즘 로직
# ---------------------------------------------------------

def calculate_sllf_power(current_time, active_evs, grid_capacity, max_ev_power, time_step):
    """
    기존 sLLF: 이진 탐색(Binary Search)을 사용하여 L값을 근사함
    """
    count = len(active_evs)
    if count == 0: return []

    current_laxities = []
    phys_limits = []

    for ev in active_evs:
        remaining_time = ev.deadline - current_time
        time_to_charge = ev.remaining / max_ev_power
        l_t = remaining_time - time_to_charge
        current_laxities.append(l_t)
        p_limit = min(max_ev_power, ev.remaining / time_step)
        phys_limits.append(p_limit)

    if sum(phys_limits) <= grid_capacity + EPSILON:
        return phys_limits

    def get_total_power_for_target_L(target_L):
        total_p = 0.0
        allocs = []
        for i in range(count):
            req_p = (max_ev_power / time_step) * (target_L - current_laxities[i] + time_step)
            req_p = max(0.0, min(req_p, phys_limits[i]))
            allocs.append(req_p)
            total_p += req_p
        return total_p, allocs

    min_lax = min(current_laxities)
    max_lax = max(current_laxities)
    
    low_L = min_lax - time_step * 5.0 
    high_L = max_lax + time_step * 5.0

    best_allocations = [0.0] * count
    
    for _ in range(30):
        mid_L = (low_L + high_L) / 2.0
        p_sum, p_allocs = get_total_power_for_target_L(mid_L)
        
        if p_sum > grid_capacity:
            high_L = mid_L
        else:
            low_L = mid_L
            best_allocations = p_allocs

    if sum(best_allocations) > grid_capacity + EPSILON:
        scale = grid_capacity / sum(best_allocations)
        best_allocations = [p * scale for p in best_allocations]
        
    return best_allocations

def calculate_new_algo_power(current_time, active_evs, grid_capacity, max_ev_power, time_step):
    """
    [수정된 NEW_ALGO: Optimized Sweeping sLLF]
    이벤트 스위핑 방식을 통해 정확한 Laxity Threshold를 계산 (Binary Search 제거)
    """
    count = len(active_evs)
    if count == 0: return []

    # 1. EV별 데이터 전처리
    ev_data = []
    for i, ev in enumerate(active_evs):
        # Laxity 계산: (남은시간 - 충전필요시간)
        remaining_time = ev.deadline - current_time
        time_to_charge = ev.remaining / max_ev_power
        l_i = remaining_time - time_to_charge
        
        # 현재 타임슬롯에서 물리적으로 가능한 최대 충전량(Cap)
        # Cap = Min(최대파워, 남은에너지를 이번 틱에 다 채우는데 필요한 파워)
        p_cap = min(max_ev_power, ev.remaining / time_step)
        
        ev_data.append({
            'original_idx': i,
            'laxity': l_i,
            'cap': p_cap
        })

    # 2. 이벤트 포인트 생성 (O(N))
    # 공식: rate = L - (l_i - 1) 
    # -> rate > 0 이 시작되는 L = l_i - 1
    # -> rate가 Cap에 도달하는 L = l_i - 1 + Cap
    events = []
    for item in ev_data:
        start_point = item['laxity'] - 1.0
        end_point = start_point + item['cap']
        
        # (L값, 기울기 변화량)
        events.append((start_point, 1.0))   # 기울기 +1
        events.append((end_point, -1.0))  # 기울기 -1

    # 3. 이벤트 정렬 (O(N log N))
    events.sort(key=lambda x: x[0])

    # 4. 스위핑 (Sweeping) (O(N))
    current_slope = 0.0
    current_power_sum = 0.0
    prev_L = events[0][0]
    final_L = events[-1][0] + 1.0 # 초기값: 충분히 큰 값

    found_solution = False

    for L, slope_change in events:
        delta_L = L - prev_L
        added_power = current_slope * delta_L
        
        # 이번 구간을 이동했을 때 용량을 초과하는지 확인
        if current_power_sum + added_power > grid_capacity + EPSILON:
            # 초과한다면, 정확한 L 위치 역산
            remaining_capacity = grid_capacity - current_power_sum
            if current_slope > EPSILON:
                final_L = prev_L + (remaining_capacity / current_slope)
            else:
                final_L = prev_L # 이론상 오차 범위 내
            found_solution = True
            break
        
        # 용량 내라면 상태 업데이트 후 다음 이벤트로 이동
        current_power_sum += added_power
        prev_L = L
        current_slope += slope_change
    
    # 용량 제한에 걸리지 않고 모든 이벤트를 통과한 경우 (전력이 남는 경우)
    if not found_solution:
        # 마지막 지점 이후로는 기울기가 0이므로 전력합이 더 늘어나지 않음.
        # 즉, 모든 EV가 Max Cap으로 충전해도 전력이 남는 상황.
        # final_L을 마지막 이벤트 지점(혹은 그 이상)으로 설정하여 max cap이 선택되게 함.
        final_L = events[-1][0] + 1.0

    # 5. 최종 할당량 계산 (O(N))
    allocations = [0.0] * count
    for item in ev_data:
        # r = [final_L - (l_i - 1)]_0^cap
        raw_rate = final_L - (item['laxity'] - 1.0)
        rate = max(0.0, min(item['cap'], raw_rate))
        allocations[item['original_idx']] = rate

    # 혹시 모를 부동소수점 오차로 인한 총합 초과 보정 (Scaling)
    total_alloc = sum(allocations)
    if total_alloc > grid_capacity + EPSILON:
        scale = grid_capacity / total_alloc
        allocations = [p * scale for p in allocations]

    return allocations

# ---------------------------------------------------------
# 3. 시뮬레이션 엔진
# ---------------------------------------------------------
def run_simulation(ev_set: List[EVRequest], algorithm: str) -> bool:
    evs = copy.deepcopy(ev_set)
    current_time = 0.0
    finished_cnt = 0
    total_evs = len(evs)
    
    max_deadline = max(e.deadline for e in evs) if evs else 0

    while finished_cnt < total_evs:
        # 1. 큐 구성
        ready_queue = [
            e for e in evs 
            if e.arrival <= current_time + EPSILON and e.remaining > EPSILON
        ]
        
        # 2. 데드라인 Miss 판정
        for e in ready_queue:
            if current_time >= float(e.deadline) - EPSILON:
                return False 

        # 3. 전력 할당 계산
        allocated_powers = []
        if algorithm == 'sLLF':
            allocated_powers = calculate_sllf_power(current_time, ready_queue, TOTAL_STATION_POWER, MAX_EV_POWER, TIME_STEP)
        elif algorithm == 'NEW_ALGO':
            allocated_powers = calculate_new_algo_power(current_time, ready_queue, TOTAL_STATION_POWER, MAX_EV_POWER, TIME_STEP)
        else:
            # Baseline Algorithms
            if algorithm == 'EDF':
                ready_queue.sort(key=lambda x: (x.deadline, x.ev_id))
            elif algorithm == 'LLF':
                ready_queue.sort(key=lambda x: ((x.deadline - current_time) - (x.remaining/MAX_EV_POWER), x.ev_id))
            elif algorithm == 'FCFS':
                ready_queue.sort(key=lambda x: (x.arrival, x.ev_id))
            
            allocated_powers = [0.0] * len(ready_queue)
            current_used = 0.0
            for i, ev in enumerate(ready_queue):
                available = TOTAL_STATION_POWER - current_used
                rate = min(MAX_EV_POWER, available)
                rate = max(0.0, rate)
                allocated_powers[i] = rate
                current_used += rate

        # 4. 충전 수행
        for i, ev in enumerate(ready_queue):
            charged = min(ev.remaining, allocated_powers[i] * TIME_STEP)
            ev.remaining -= charged
            if ev.remaining <= EPSILON: ev.remaining = 0.0

        # 5. 상태 업데이트
        finished_cnt = sum(1 for e in evs if e.remaining <= EPSILON)
        current_time += TIME_STEP
        
        if current_time > max_deadline + 20.0: return False

    return True

# ---------------------------------------------------------
# 4. 병렬 작업 워커
# ---------------------------------------------------------
def worker_task_data(args):
    level, ev_requests = args
    result_vector = {}
    for algo in ['EDF', 'LLF', 'NEW_ALGO', 'sLLF']:
        success = run_simulation(ev_requests, algo)
        result_vector[algo] = 1 if success else 0
    return level, result_vector

# ---------------------------------------------------------
# 5. 메인 실행
# ---------------------------------------------------------
if __name__ == '__main__':
    start_time = time.time()
    
    if not os.path.exists(DATA_SAVE_PATH):
        print(f"Error: Data directory '{DATA_SAVE_PATH}' not found.")
        # exit(1) # 테스트를 위해 주석 처리하거나 경로 수정 필요

    congestion_levels = list(range(STRESS_START, STRESS_START + STRESS_NUM))
    all_tasks = []
    
    print(f"Loading data from '{DATA_SAVE_PATH}' and preparing tasks...")

    # 데이터 로딩 로직 (경로 에러 방지를 위해 예외처리 강화)
    for level in congestion_levels:
        filename = f"ev_level_{level}.pkl"
        full_path = os.path.join(DATA_SAVE_PATH, filename)
        if os.path.exists(full_path):
            try:
                with open(full_path, 'rb') as f:
                    level_dataset = pickle.load(f)
                    if isinstance(level_dataset, list):
                        for ev_set in level_dataset:
                            all_tasks.append((level, ev_set))
            except Exception as e:
                print(f"Error reading {filename}: {e}")
    
    # 데이터가 없을 경우 임시 데이터 생성 (코드 실행 테스트용)
    if not all_tasks:
        print("Warning: No data loaded. Generating dummy data for test...")
        import random
        for level in congestion_levels:
            for _ in range(10): # 레벨당 10개 트라이얼
                dummy_evs = []
                for i in range(5 + level): # 레벨이 높을수록 EV 많음
                    arr = random.randint(0, 10)
                    req = random.uniform(2.0, 5.0)
                    dead = arr + int(req/MAX_EV_POWER) + random.randint(2, 10)
                    dummy_evs.append(EVRequest(i, arr, req, dead, req))
                all_tasks.append((level, dummy_evs))

    print(f"Total Tasks Created: {len(all_tasks)}")
    print(f"Starting Simulation on {cpu_count()} Cores...")

    results_map = {level: {k: 0 for k in ['EDF', 'LLF', 'NEW_ALGO', 'sLLF']} for level in congestion_levels}
    trial_counts = {level: 0 for level in congestion_levels}

    with Pool(processes=cpu_count()) as pool:
        raw_results = pool.map(worker_task_data, all_tasks)
        
    for level, res in raw_results:
        trial_counts[level] += 1
        for algo, score in res.items():
            results_map[level][algo] += score

    ratios = {k: [] for k in ['EDF', 'LLF', 'NEW_ALGO', 'sLLF']}
    print(f"\n[Simulation Results - Success Rate]")
    
    for level in congestion_levels:
        count = trial_counts[level]
        if count == 0: 
            for k in ratios: ratios[k].append(0.0)
            continue
        print(f"Level {level:2d} (n={count}): ", end="")
        for algo in ['EDF', 'LLF', 'sLLF', 'NEW_ALGO']:
            rate = results_map[level][algo] / count
            ratios[algo].append(rate)
            print(f"{algo}={rate:.2f} ", end="")
        print("")

    print(f"\nTotal Execution Time: {time.time() - start_time:.2f} seconds")

    plt.figure(figsize=(12, 6))
    plt.plot(congestion_levels, ratios['EDF'], marker='o', label='EDF', linestyle=':', color='gray', alpha=0.5)
    plt.plot(congestion_levels, ratios['LLF'], marker='s', label='LLF', linestyle='--', color='blue', alpha=0.5)
    plt.plot(congestion_levels, ratios['NEW_ALGO'], marker='^', label='NEW_ALGO (Sweeping)', linestyle='-', color='green', linewidth=2)
    plt.plot(congestion_levels, ratios['sLLF'], marker='*', label='sLLF (Binary)', linestyle='-', color='red', linewidth=2)

    plt.title('Algorithm Performance Comparison', fontsize=14)
    plt.xlabel('Congestion Level (Stress)', fontsize=12)
    plt.ylabel('Success Rate', fontsize=12)
    plt.grid(True, linestyle=':', alpha=0.7)
    plt.legend(fontsize=12)
    plt.xticks(congestion_levels)
    plt.ylim(-0.05, 1.05)
    plt.show()