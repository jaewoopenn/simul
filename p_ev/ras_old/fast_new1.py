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
    Robust Abstract Scheduler (RAS) 구현
    - 누적 제약 조건 분석(Cumulative Constraint Analysis)을 통한 필수 부하 계산
    - Feasibility Optimal & Work-Conserving
    """
    if not active_evs:
        return []

    # 1. 정렬: 데드라인 빠른 순 (EDF)
    #    Hall's Condition을 검사하기 위한 논리적 순서이자, 배분 우선순위입니다.
    #    active_evs는 원본 유지를 위해 건드리지 않고, 정렬된 복사본을 사용합니다.
    sorted_evs = sorted(active_evs, key=lambda x: x.deadline)
    
    cumulative_energy_needed = 0.0
    cumulative_max_rate_sum = 0.0
    system_must_energy = 0.0
    
    # 2. 누적 제약 분석 (병목 탐색)
    #    미래 구간에 대해 수요(Demand)와 공급(Supply)을 비교하여,
    #    미래로 미룰 수 없는 'Overflow(필수 부하)'를 계산합니다.
    for ev in sorted_evs:
        cumulative_energy_needed += ev.remaining
        cumulative_max_rate_sum += max_ev_power
        
        # 미래 가용 시간: (데드라인 - 현재시간) - 현재 타임스텝
        # 즉, '다음 스텝(내일)'부터 데드라인까지의 시간
        remaining_duration = ev.deadline - current_time
        future_time = max(0.0, remaining_duration - time_step)
        
        # 미래 가용 용량 (Energy) 계산
        # 핵심: 미래의 처리 능력은 '시스템 전체 용량(C)'과 '그룹 내 차량들의 입구 크기 합(Sum R_max)' 중
        # 더 작은 값(Min)에 의해 제한됩니다. (Intersection Capacity)
        future_cap_power = min(grid_capacity, cumulative_max_rate_sum)
        future_cap_energy = future_cap_power * future_time
        
        # Overflow: 지금 당장 처리하지 않으면 미래에 감당 불가능한 양
        overflow = cumulative_energy_needed - future_cap_energy
        
        # 시스템 전체의 최대 병목(Must Load) 갱신
        if overflow > system_must_energy:
            system_must_energy = overflow
            
    # 3. 배분 (Allocation) - Work-Conserving
    #    계산된 '필수량(Must)'을 보장하되, 
    #    남는 시스템 용량이 있다면 최대한 꽉 채워서(grid_capacity) 공급합니다.
    
    # 현재 스텝에서 공급해야 할 총 에너지 목표
    # (Must Load와 Grid Capacity 중 큰 값을 선택하여 Work-Conserving 달성)
    target_energy = max(system_must_energy, grid_capacity * time_step)
    
    # 단, 전체 필요량(Total Demand)을 초과하여 전력을 낭비하지 않도록 클램핑
    total_demand = sum(e.remaining for e in active_evs)
    target_energy = min(target_energy, total_demand)
    
    # 에너지 -> 전력(Power) 변환
    target_total_power = target_energy / time_step
    
    # 할당 로직: 급한 순서(EDF)대로 target_power를 소진
    allocations = {}
    remaining_power = target_total_power
    
    for ev in sorted_evs:
        if remaining_power <= EPSILON:
            allocations[ev.ev_id] = 0.0
            continue
            
        # 개별 차량이 받을 수 있는 최대 파워 산출
        # 1. 차량 물리적 한계 (max_ev_power)
        # 2. 남은 에너지 소진에 필요한 파워 (ev.remaining / time_step)
        max_rate_for_ev = min(max_ev_power, ev.remaining / time_step)
        
        # 할당: 남은 파워 내에서 가능한 만큼
        alloc = min(max_rate_for_ev, remaining_power)
        
        allocations[ev.ev_id] = alloc
        remaining_power -= alloc
        
    # 4. 결과 리스트 생성 (입력된 active_evs의 순서와 일치시켜 반환)
    result_powers = []
    for ev in active_evs:
        result_powers.append(allocations.get(ev.ev_id, 0.0))
        
    return result_powers

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