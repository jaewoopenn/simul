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

def calculate_new_algo_power(current_time, active_evs, grid_capacity, max_ev_power, time_step):
    """
    Event-based Backward Analysis (Staircase Method)
    - Grid(배열)를 사용하지 않고, 데드라인 기반의 '구간(Segment)' 수식 계산으로 최적화함.
    - 시간 복잡도: O(T) -> O(N^2) (T가 클 때 압도적으로 유리, N은 보통 작음)
    - 공간 복잡도: O(T) -> O(N)
    - 결과: Grid 방식과 수학적으로 100% 동일한 Must Load 산출 (Exact Analytic Solution)
    """
    if not active_evs:
        return []

    # -----------------------------------------------------
    # 1. 구간(Segment) 생성 (Time Discretization by Events)
    # -----------------------------------------------------
    # 모든 EV의 데드라인을 수집하여 정렬 (중복 제거)
    # 현재 시간(current_time)은 시작점, 데드라인은 끝점들이 됨.
    # 예: Current=0, D=[10, 30] -> Segments: [0, 10], [10, 30]
    
    deadlines = sorted(list(set(ev.deadline for ev in active_evs)))
    
    # 현재 시간이 데드라인보다 뒤에 있는 경우 필터링 (이미 지난 데드라인 방어)
    deadlines = [d for d in deadlines if d > current_time]
    
    # Segment 정의: [start_time, end_time, remaining_capacity]
    segments = []
    start_t = current_time
    for d in deadlines:
        duration = d - start_t
        # 각 구간의 총 가용 에너지 = (시간 길이) * (시스템 용량)
        cap_energy = grid_capacity * duration
        segments.append({
            "start": start_t,
            "end": d,
            "capacity": cap_energy
        })
        start_t = d

    # -----------------------------------------------------
    # 2. 역방향 채우기 (Analytic Backward Filling)
    # -----------------------------------------------------
    # Must Load 저장소
    individual_must_loads = {ev.ev_id: 0.0 for ev in active_evs}
    
    # 데드라인이 늦은 차부터 처리 (충돌 최소화 & 정확한 Spillover 계산)
    # 정렬된 복사본 사용
    sorted_evs = sorted(active_evs, key=lambda x: x.deadline, reverse=True)
    
    for ev in sorted_evs:
        energy_needed = ev.remaining
        
        # 자신의 데드라인보다 앞선 구간들만 탐색 (역순 탐색: 늦은 구간 -> 빠른 구간)
        # 내 데드라인 안에 포함되는 구간들을 뒤에서부터 훑음
        for seg in reversed(segments):
            if energy_needed <= EPSILON:
                break
            
            # 이 구간이 내 데드라인 이후라면 패스 (넣을 수 없음)
            if seg["start"] >= ev.deadline:
                continue
            
            # 이 구간에 넣을 수 있는 나의 물리적 한계
            # 1. 구간의 남은 시스템 용량 (seg["capacity"])
            # 2. 나의 최대 충전 속도 한계 (duration * max_power)
            duration = seg["end"] - seg["start"]
            my_phys_limit = max_ev_power * duration
            
            # 실제로 채울 양
            fill = min(energy_needed, seg["capacity"], my_phys_limit)
            
            if fill > EPSILON:
                seg["capacity"] -= fill
                energy_needed -= fill
                
                # 핵심: 이 구간의 시작점이 '현재 시간(current_time)'이라면
                # 이 양은 '지금 당장' 처리해야 하는 Must Load에 기여함
                # (첫 번째 세그먼트가 항상 [current_time, d_min] 이므로)
                if abs(seg["start"] - current_time) < EPSILON:
                    # 주의: Must Load는 '일률(Power)'이 아니라 '에너지(Energy)' 관점에서
                    # 현재 타임스텝(time_step) 내에 처리해야 할 양임.
                    # 하지만 여기서는 'Must Rate'를 구해야 하므로, 
                    # 이 Spillover가 첫 번째 TimeStep(1칸) 안에 다 들어가야 하는지 확인 필요.
                    
                    # 더 정확한 로직: 
                    # Spillover된 에너지는 t=0 시점에 몰린 부하임.
                    # 이를 Power로 환산: Energy / time_step
                    # 단, 이 Energy는 첫 번째 구간(길이 duration) 전체에 걸친 것일 수 있음.
                    # 우리는 "지금 당장(time_step)" 필요한 Power를 원함.
                    
                    # 세그먼트 방식의 미세 조정:
                    # 첫 번째 세그먼트의 길이가 time_step보다 길 수 있음 (예: [0, 10]).
                    # 하지만 Backward Filling의 특성상, 가장 앞쪽(0)으로 밀려난 에너지는
                    # '가장 급한' 에너지임. 따라서 이를 time_step으로 나눠서 즉시 처리해야 함.
                    
                    # *수정*: 여기서 구해진 energy_needed 잔여분이 아님. 
                    # fill 자체가 t=0 근처 구간에 할당된 것임.
                    # 엄밀히 말해 't=0~step' 사이의 Must Load를 구하려면
                    # 첫 세그먼트를 [current, current+step]으로 쪼개는 게 가장 정확함.
                    pass 

    # -----------------------------------------------------
    # 2.1 (개선) 세그먼트 미세화 전략
    # -----------------------------------------------------
    # 위 로직에서 첫 세그먼트가 길면 Must Load가 희석될 수 있음.
    # 정확한 '이번 턴(time_step)'의 부하를 위해 첫 구간을 강제로 time_step 만큼 자릅니다.
    
    segments = []
    # 첫 구간: [current, current + time_step]
    segments.append({"start": current_time, "end": current_time + time_step, "capacity": grid_capacity * time_step})
    
    # 나머지 구간들: 데드라인 기반
    deadlines = sorted(list(set(ev.deadline for ev in active_evs)))
    deadlines = [d for d in deadlines if d > current_time + time_step] # 첫 스텝 이후만
    
    start_t = current_time + time_step
    for d in deadlines:
        duration = d - start_t
        if duration > EPSILON:
            segments.append({"start": start_t, "end": d, "capacity": grid_capacity * duration})
            start_t = d
            
    # 다시 채우기 (Logic 동일)
    individual_must_loads = {ev.ev_id: 0.0 for ev in active_evs}
    
    for ev in sorted_evs:
        energy_needed = ev.remaining
        for seg in reversed(segments):
            if energy_needed <= EPSILON: break
            if seg["start"] >= ev.deadline: continue
            
            duration = seg["end"] - seg["start"]
            my_phys_limit = max_ev_power * duration
            fill = min(energy_needed, seg["capacity"], my_phys_limit)
            
            if fill > EPSILON:
                seg["capacity"] -= fill
                energy_needed -= fill
                
                # 첫 번째 세그먼트(Current Step)에 할당된 양만 Must Load로 인정
                if abs(seg["start"] - current_time) < EPSILON:
                    individual_must_loads[ev.ev_id] += fill # Energy

    # -----------------------------------------------------
    # 3. 배분 (Allocation) - 2-Pass Work-Conserving
    # -----------------------------------------------------
    allocations = {}
    
    # Step 1: Must Load (Power로 변환)
    for ev in active_evs:
        must_energy = individual_must_loads.get(ev.ev_id, 0.0)
        allocations[ev.ev_id] = must_energy / time_step # Power = Energy / TimeStep

    # Step 2: Surplus 배분 (EDF)
    current_total_power = sum(allocations.values())
    surplus_power = max(0.0, grid_capacity - current_total_power)
    
    if surplus_power > EPSILON:
        # 마감 빠른 순 보너스
        for ev in sorted(active_evs, key=lambda x: x.deadline):
            if surplus_power <= EPSILON: break
            
            current_p = allocations[ev.ev_id]
            max_p = min(max_ev_power, ev.remaining / time_step)
            room = max(0.0, max_p - current_p)
            
            if room > EPSILON:
                bonus = min(surplus_power, room)
                allocations[ev.ev_id] += bonus
                surplus_power -= bonus

    return [allocations.get(ev.ev_id, 0.0) for ev in active_evs]
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
    plt.plot(congestion_levels, ratios['NEW_ALGO'], marker='^', label='NEW_ALGO', linestyle='-', color='green', linewidth=2)
    plt.plot(congestion_levels, ratios['sLLF'], marker='*', label='sLLF (Binary)', linestyle='-', color='red', linewidth=2)

    plt.title('Algorithm Performance Comparison', fontsize=14)
    plt.xlabel('Congestion Level (Stress)', fontsize=12)
    plt.ylabel('Success Rate', fontsize=12)
    plt.grid(True, linestyle=':', alpha=0.7)
    plt.legend(fontsize=12)
    plt.xticks(congestion_levels)
    plt.ylim(-0.05, 1.05)
    plt.show()