import os
import pickle
import copy
import time
import math
import matplotlib.pyplot as plt
from dataclasses import dataclass
from typing import List
from multiprocessing import Pool, cpu_count
from collections import deque

# ---------------------------------------------------------
# 1. 설정 및 상수
# ---------------------------------------------------------
DATA_SAVE_PATH = "/Users/jaewoo/data/ev/cab/data"

TRIAL_NUM = 200
TIME_STEP = 1
EPSILON = 1e-6

TOTAL_STATION_POWER = 2.72
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
# [NEW] Max Flow Algorithm (Dinic's Algorithm)
# ---------------------------------------------------------
class Dinic:
    def __init__(self, n):
        self.n = n
        self.graph = [[] for _ in range(n)]
        self.level = []

    def add_edge(self, u, v, capacity):
        # forward edge: [v, capacity, index_of_reverse_edge]
        self.graph[u].append([v, capacity, len(self.graph[v])])
        # backward edge: [u, 0, index_of_forward_edge]
        self.graph[v].append([u, 0, len(self.graph[u]) - 1])

    def bfs(self, s, t):
        self.level = [-1] * self.n
        self.level[s] = 0
        queue = deque([s])
        while queue:
            u = queue.popleft()
            for v, cap, rev_idx in self.graph[u]:
                if cap > EPSILON and self.level[v] < 0:
                    self.level[v] = self.level[u] + 1
                    queue.append(v)
        return self.level[t] >= 0

    def dfs(self, u, t, flow, ptr):
        if u == t or flow < EPSILON:
            return flow
        for i in range(ptr[u], len(self.graph[u])):
            ptr[u] = i
            v, cap, rev_idx = self.graph[u][i]
            if self.level[v] == self.level[u] + 1 and cap > EPSILON:
                pushed = self.dfs(v, t, min(flow, cap), ptr)
                if pushed > EPSILON:
                    self.graph[u][i][1] -= pushed
                    self.graph[v][rev_idx][1] += pushed
                    return pushed
        return 0

    def max_flow(self, s, t):
        max_f = 0
        while self.bfs(s, t):
            ptr = [0] * self.n
            while True:
                pushed = self.dfs(s, t, float('inf'), ptr)
                if pushed < EPSILON:
                    break
                max_f += pushed
        return max_f

def solve_offline_optimal(ev_requests: List[EVRequest], total_power, max_ev_power) -> bool:
    """
    Constructs a flow network based on time intervals to determine schedulability.
    Nodes: Source(0), Sink(1), EVs(2..N+1), Intervals(N+2..N+M+1)
    """
    if not ev_requests:
        return True

    # 1. Collect all critical time points (Arrivals and Deadlines)
    time_points = set()
    total_energy_needed = 0.0
    
    for ev in ev_requests:
        time_points.add(ev.arrival)
        time_points.add(ev.deadline)
        total_energy_needed += ev.required_energy
    
    sorted_points = sorted(list(time_points))
    intervals = []
    for i in range(len(sorted_points) - 1):
        start = sorted_points[i]
        end = sorted_points[i+1]
        if end > start:
            intervals.append((start, end, end - start)) # (start, end, duration)
            
    num_evs = len(ev_requests)
    num_intervals = len(intervals)
    
    # Node Indices
    source = 0
    sink = 1
    ev_start_idx = 2
    interval_start_idx = 2 + num_evs
    total_nodes = 2 + num_evs + num_intervals
    
    dinic = Dinic(total_nodes)
    
    # 2. Build Graph
    
    # Edge: Source -> EVs
    for i, ev in enumerate(ev_requests):
        dinic.add_edge(source, ev_start_idx + i, ev.required_energy)
        
    # Edge: Intervals -> Sink
    for j, (start, end, duration) in enumerate(intervals):
        # Grid limit for this interval
        grid_cap = total_power * duration
        dinic.add_edge(interval_start_idx + j, sink, grid_cap)
        
    # Edge: EVs -> Intervals
    for i, ev in enumerate(ev_requests):
        for j, (start, end, duration) in enumerate(intervals):
            # If EV is present during this interval
            # [arrival, deadline) overlaps with [start, end)
            # Since intervals are derived from these points, strict inclusion check works
            if ev.arrival <= start and ev.deadline >= end:
                # Max energy EV can take in this interval
                ev_cap = max_ev_power * duration
                dinic.add_edge(ev_start_idx + i, interval_start_idx + j, ev_cap)
                
    # 3. Calculate Max Flow
    max_flow_val = dinic.max_flow(source, sink)
    
    # 4. Check Feasibility
    # If max flow equals total required energy, it's possible to schedule all.
    return abs(max_flow_val - total_energy_needed) < 1e-4

# ---------------------------------------------------------
# 2. 기존 알고리즘 로직 (sLLF, NewAlgo 등 유지)
# ---------------------------------------------------------

def calculate_sllf_power(current_time, active_evs, grid_capacity, max_ev_power, time_step):
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

    if not current_laxities:
        min_lax, max_lax = -100, 100
    else:
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
    count = len(active_evs)
    if count == 0: return []
    
    allocation = [0.0] * count
    remaining_grid = grid_capacity
    
    ev_status = []
    
    for i, ev in enumerate(active_evs):
        phys_limit = min(max_ev_power, ev.remaining / time_step)
        
        time_to_deadline = float(ev.deadline) - current_time
        total_slots = math.ceil(time_to_deadline / time_step)
        future_slots = max(0, total_slots - 1)
        
        max_future_charge = future_slots * max_ev_power
        mandatory_energy = max(0.0, ev.remaining - max_future_charge)
        mandatory_power = mandatory_energy / time_step
        
        charge_time_needed = ev.remaining / max_ev_power
        laxity = time_to_deadline - charge_time_needed
        
        ev_status.append({
            'idx': i,
            'phys_limit': phys_limit,
            'mandatory': mandatory_power,
            'laxity': laxity,
            'deadline': ev.deadline
        })

    ev_status.sort(key=lambda x: (x['laxity'], x['deadline']))
    
    for st in ev_status:
        idx = st['idx']
        if remaining_grid <= EPSILON: break
            
        needed = min(st['mandatory'], st['phys_limit'])
        if needed > EPSILON:
            actual = min(needed, remaining_grid)
            allocation[idx] += actual
            remaining_grid -= actual

    residual_caps = []
    current_rems_after_s1 = []
    time_dists = []
    has_capacity = False
    
    for i in range(count):
        ev = active_evs[i]
        curr_rem = ev.remaining - (allocation[i] * time_step)
        current_rems_after_s1.append(curr_rem)
        
        time_dist = float(ev.deadline) - current_time
        time_dists.append(time_dist)
        
        phys_limit = min(max_ev_power, ev.remaining / time_step)
        cap = max(0.0, phys_limit - allocation[i])
        residual_caps.append(cap)
        
        if cap > EPSILON: has_capacity = True

    if remaining_grid > EPSILON and has_capacity:
        low_L = -100.0
        high_L = 100.0
        best_extra = [0.0] * count
        
        for _ in range(30):
            mid_L = (low_L + high_L) / 2.0
            prop_total = 0.0
            curr_prop = []
            
            for i in range(count):
                target_rem = max_ev_power * (time_dists[i] - mid_L)
                energy_needed = current_rems_after_s1[i] - target_rem
                power_needed = energy_needed / time_step
                p = max(0.0, min(power_needed, residual_caps[i]))
                curr_prop.append(p)
                prop_total += p
            
            if prop_total > remaining_grid:
                high_L = mid_L
            else:
                low_L = mid_L
                best_extra = curr_prop
        
        for i in range(count):
            if best_extra[i] > EPSILON:
                allocation[i] += best_extra[i]
                remaining_grid -= best_extra[i]

    if remaining_grid > EPSILON:
        urgency = []
        for i in range(count):
            phys_limit = min(max_ev_power, active_evs[i].remaining / time_step)
            room = phys_limit - allocation[i]
            if room > EPSILON:
                curr_rem = active_evs[i].remaining - (allocation[i] * time_step)
                l = (float(active_evs[i].deadline) - current_time) - (curr_rem / max_ev_power)
                urgency.append((l, i))
        
        urgency.sort(key=lambda x: x[0])
        
        for _, idx in urgency:
            if remaining_grid <= EPSILON: break
            phys_limit = min(max_ev_power, active_evs[idx].remaining / time_step)
            room = phys_limit - allocation[idx]
            add = min(remaining_grid, room)
            
            allocation[idx] += add
            remaining_grid -= add
            
    return allocation


# ---------------------------------------------------------
# 3. 시뮬레이션 엔진
# ---------------------------------------------------------
def run_simulation(ev_set: List[EVRequest], algorithm: str) -> bool:
    # [NEW] Offline Optimal Handling
    if algorithm == 'OfflineOpt':
        return solve_offline_optimal(ev_set, TOTAL_STATION_POWER, MAX_EV_POWER)

    evs = copy.deepcopy(ev_set)
    current_time = 0.0
    finished_cnt = 0
    total_evs = len(evs)
    max_deadline = max(e.deadline for e in evs) if evs else 0

    while finished_cnt < total_evs:
        ready_queue = [
            e for e in evs 
            if e.arrival <= current_time + EPSILON and e.remaining > EPSILON
        ]
        
        for e in ready_queue:
            if current_time >= float(e.deadline) - EPSILON:
                return False 

        allocated_powers = []
        if algorithm == 'sLLF':
            allocated_powers = calculate_sllf_power(current_time, ready_queue, TOTAL_STATION_POWER, MAX_EV_POWER, TIME_STEP)
        elif algorithm == 'NEW_ALGO':
            allocated_powers = calculate_new_algo_power(current_time, ready_queue, TOTAL_STATION_POWER, MAX_EV_POWER, TIME_STEP)
        else:
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

        for i, ev in enumerate(ready_queue):
            charged = min(ev.remaining, allocated_powers[i] * TIME_STEP)
            ev.remaining -= charged
            if ev.remaining <= EPSILON: ev.remaining = 0.0

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
    # [NEW] 'OfflineOpt' added to the list
    for algo in ['OfflineOpt', 'EDF', 'LLF', 'NEW_ALGO', 'sLLF']:
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
        # exit(1) # 테스트를 위해 주석처리 (데이터 없을 시 에러 방지)
        pass

    congestion_levels = list(range(STRESS_START, STRESS_START + STRESS_NUM))
    all_tasks = []
    
    print(f"Loading data from '{DATA_SAVE_PATH}' and preparing tasks...")

    # Data loading logic (same as before)
    for level in congestion_levels:
        filename = f"ev_level_{level}.pkl"
        full_path = os.path.join(DATA_SAVE_PATH, filename)
        try:
            with open(full_path, 'rb') as f:
                level_dataset = pickle.load(f)
                if not isinstance(level_dataset, list): continue
                for ev_set in level_dataset:
                    all_tasks.append((level, ev_set))
        except FileNotFoundError:
            # print(f"Warning: {filename} not found.")
            pass
        except Exception as e:
            print(f"Error reading {filename}: {e}")

    # Dummy Data for standalone testing if no file found (Optional)
    if not all_tasks:
        print("No data found, creating dummy tasks for demonstration.")
        for level in congestion_levels:
            for _ in range(10): # 10 dummy sets per level
                dummy_evs = [
                    EVRequest(0, 0, 5.0, 10, 5.0),
                    EVRequest(1, 2, 3.0, 8, 3.0),
                    EVRequest(2, 5, 2.0, 12, 2.0)
                ]
                all_tasks.append((level, dummy_evs))

    print(f"Total Tasks Created: {len(all_tasks)}")
    print(f"Starting Simulation on {cpu_count()} Cores...")

    # [NEW] Added OfflineOpt to results map
    algo_list = ['OfflineOpt', 'EDF', 'LLF', 'NEW_ALGO', 'sLLF']
    results_map = {level: {k: 0 for k in algo_list} for level in congestion_levels}
    trial_counts = {level: 0 for level in congestion_levels}

    with Pool(processes=cpu_count()) as pool:
        raw_results = pool.map(worker_task_data, all_tasks)
        
    for level, res in raw_results:
        trial_counts[level] += 1
        for algo, score in res.items():
            results_map[level][algo] += score

    ratios = {k: [] for k in algo_list}
    print(f"\n[Simulation Results - Success Rate]")
    
    for level in congestion_levels:
        count = trial_counts[level]
        if count == 0: 
            for k in ratios: ratios[k].append(0.0)
            continue
        print(f"Level {level:2d} (n={count}): ", end="")
        for algo in algo_list:
            rate = results_map[level][algo] / count
            ratios[algo].append(rate)
            print(f"{algo}={rate:.3f} ", end="")
        print("")

    print(f"\nTotal Execution Time: {time.time() - start_time:.2f} seconds")

    # [NEW] Plotting Update
    plt.figure(figsize=(12, 6))
    
    # Offline Optimal (Theoretical Max) - Black dashed line usually represents upper bound
    plt.plot(congestion_levels, ratios['OfflineOpt'], marker='x', label='Offline Optimal', linestyle='--', color='black', linewidth=2)
    
    plt.plot(congestion_levels, ratios['EDF'], marker='o', label='EDF', linestyle=':', color='gray', alpha=0.5)
    plt.plot(congestion_levels, ratios['LLF'], marker='s', label='LLF', linestyle='--', color='blue', alpha=0.5)
    plt.plot(congestion_levels, ratios['NEW_ALGO'], marker='^', label='NEW_ALGO', linestyle='-', color='green', linewidth=2)
    plt.plot(congestion_levels, ratios['sLLF'], marker='*', label='sLLF', linestyle='-', color='red', linewidth=2)

    plt.title('Algorithm Performance Comparison (with Offline Optimal)', fontsize=14)
    plt.xlabel('Congestion Level (Stress)', fontsize=12)
    plt.ylabel('Success Rate', fontsize=12)
    plt.grid(True, linestyle=':', alpha=0.7)
    plt.legend(fontsize=12)
    plt.xticks(congestion_levels)
    plt.ylim(-0.05, 1.05)
    plt.show()