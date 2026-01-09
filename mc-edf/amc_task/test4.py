import json
import math
import matplotlib.pyplot as plt
import os
import itertools

PATH='/users/jaewoo/data/amc'
DATAFILE=PATH+'/data/task_data.json'
FIGFILE=PATH+'/survival_k_result.png'


# =========================================================
# 1. Helper Functions & Classes
# =========================================================

def ceil_div(a, b):
    return (a + b - 1) // b

class Task:
    def __init__(self, task_data):
        self.id = task_data['id']
        self.c_lo = int(task_data['c_lo'])
        self.c_hi = int(task_data['c_hi'])
        self.d = int(task_data['d'])
        self.t = int(task_data['t'])
        self.criticality = task_data['criticality']
        self.u_lo = self.c_lo / self.t
        
    def __repr__(self):
        return f"T{self.id}({self.criticality})"

# =========================================================
# 2. RTA Logic with K-Overruns
# =========================================================

def get_response_time_lo(task, hp_tasks):
    """Normal Mode RTA"""
    w = task.c_lo
    while True:
        interference = 0
        for h_task in hp_tasks:
            interference += ceil_div(w, h_task.t) * h_task.c_lo
        w_new = task.c_lo + interference
        if w_new > task.d: return float('inf')
        if w_new == w: return w
        w = w_new

def m_function(task_k, s, t):
    """AMC Eq(11): Jobs executing fully in HI mode"""
    numerator = t - s - (task_k.t - task_k.d)
    term_a = ceil_div(numerator, task_k.t) + 1
    term_b = ceil_div(t, task_k.t)
    return max(0, min(term_a, term_b))

def check_schedulability_k_overruns(task, hp_tasks, shed_tasks, k_limit):
    """
    Checks if 'task' is schedulable assuming ANY combination of 'k_limit' 
    HI tasks in hp_tasks can overrun simultaneously.
    """
    # 1. Normal Mode
    r_lo = get_response_time_lo(task, hp_tasks)
    if r_lo > task.d: return False
    
    # If LO task is shed, it is safe
    if task.criticality == 'LO' and task in shed_tasks:
        return True
    
    # 2. Overrun Scenarios
    # Identify all potential overrunners (HI tasks with priority >= task)
    hi_tasks = [t for t in hp_tasks if t.criticality == 'HI']
    if task.criticality == 'HI':
        hi_tasks.append(task)
        
    num_hi = len(hi_tasks)
    
    # Optimization: If total HI tasks <= k_limit, check only one scenario (All Overrun)
    actual_k = min(num_hi, k_limit)
    if actual_k == 0: return True 
    
    # Iterate all combinations of 'actual_k' overrunners
    # If num_hi is large, combinations can be many. 
    # But usually num_hi is small (<10).
    
    for overrunner_combo in itertools.combinations(hi_tasks, actual_k):
        overrunners_set = set(overrunner_combo)
        
        # Calculate S-points only from Shedding tasks
        s_points = {0}
        for l_shed in shed_tasks:
            curr = l_shed.t
            while curr < r_lo:
                s_points.add(curr)
                curr += l_shed.t
        s_list = sorted(list(s_points))
        
        max_r_combo = 0
        
        for s in s_list:
            # Base Execution
            base_c = task.c_lo
            if task.criticality == 'HI':
                if task in overrunners_set:
                    base_c = task.c_hi
                else:
                    base_c = task.c_lo 
            
            w = base_c
            while True:
                interference = 0
                
                # HI Interference
                for h in hp_tasks:
                    if h.criticality == 'HI':
                        if h in overrunners_set:
                            # Overrunning -> Mixed behavior
                            m_val = m_function(h, s, w)
                            term_hi = m_val * h.c_hi
                            term_lo = (ceil_div(w, h.t) - m_val) * h.c_lo
                            interference += term_hi + term_lo
                        else:
                            # Not overrunning -> Normal behavior
                            interference += ceil_div(w, h.t) * h.c_lo
                            
                # LO Interference
                hp_lo = [t for t in hp_tasks if t.criticality == 'LO']
                for l in hp_lo:
                    if l in shed_tasks: # Drop at s
                        num_jobs = (s // l.t) + 1
                        interference += num_jobs * l.c_lo
                    else: # Survive
                        interference += ceil_div(w, l.t) * l.c_lo
                
                w_new = base_c + interference
                
                if w_new > task.d: 
                    max_r_combo = float('inf')
                    break
                
                if w_new == w: 
                    max_r_combo = max(max_r_combo, w_new)
                    break
                w = w_new
            
            if max_r_combo > task.d: break 
        
        if max_r_combo > task.d: return False 
            
    return True 

# =========================================================
# 3. Solver
# =========================================================

def run_opa_k(task_set, shed_tasks, k_limit):
    unassigned = list(task_set)
    for _ in range(len(task_set)):
        candidate_found = False
        for i, task in enumerate(unassigned):
            hp_tasks = unassigned[:i] + unassigned[i+1:]
            if check_schedulability_k_overruns(task, hp_tasks, shed_tasks, k_limit):
                unassigned.pop(i)
                candidate_found = True
                break
        if not candidate_found: return False
    return True

def calculate_survival_for_k(task_set, k_limit):
    lo_tasks = [t for t in task_set if t.criticality == 'LO']
    if not lo_tasks: return 1.0
    
    # Sort: Heavy (High U) tasks first to shed them first
    lo_tasks.sort(key=lambda x: x.u_lo, reverse=True)
    total_lo_u = sum(t.u_lo for t in lo_tasks)
    if total_lo_u == 0: return 1.0
    
    # Try shedding 0, 1, 2... tasks
    for cnt in range(len(lo_tasks) + 1):
        curr_shed = set(lo_tasks[:cnt])
        
        if run_opa_k(task_set, curr_shed, k_limit):
            curr_survive = lo_tasks[cnt:]
            surviving_u = sum(t.u_lo for t in curr_survive)
            return surviving_u / total_lo_u
            
    return 0.0

# =========================================================
# 4. Main Experiment (Updated for X-Axis: Utilization)
# =========================================================

def main():
    if not os.path.exists(DATAFILE):
        print("Error: task_data.json not found.")
        return

    with open(DATAFILE, 'r') as f:
        data = json.load(f)
    
    util_points = data['util_points']
    tasksets_data = data['tasksets']
    
    # K values to analyze
    k_values = [1, 2, 3, 4, 5]
    
    # Data structure: k -> list of survival rates corresponding to sorted util_points
    results = {k: [] for k in k_values}
    
    print(f"Running Robustness Analysis (X=Util, Lines=K)...")
    
    # Sort utilization points
    sorted_u = sorted([float(k) for k in tasksets_data.keys()])
    
    for u in sorted_u:
        # Match key string
        u_str = next((k for k in tasksets_data.keys() if abs(float(k)-u) < 0.001), None)
        if not u_str: continue

        sets = tasksets_data[u_str]
        print(f"Analyzing U={u}...", end=" ")
        
        for k in k_values:
            accum_rate = 0.0
            for ts_data in sets:
                task_set = [Task(t) for t in ts_data]
                rate = calculate_survival_for_k(task_set, k)
                accum_rate += rate
            
            avg_rate = (accum_rate / len(sets)) * 100
            results[k].append(avg_rate)
            
        print(f"Done.")

    # Plot
    plt.figure(figsize=(10, 6))
    
    colors = ['blue', 'green', 'orange', 'red', 'purple']
    markers = ['o', 's', '^', 'D', 'x']
    
    for idx, k in enumerate(k_values):
        plt.plot(sorted_u, results[k], 
                 marker=markers[idx], 
                 color=colors[idx], 
                 label=f'K={k} (Tolerated Overruns)')
    
    plt.xlabel('System Utilization')
    plt.ylabel('LO Task Survival Rate (%)')
    plt.title('Impact of System Load and Robustness Level (K) on Availability')
    plt.grid(True)
    plt.ylim(0, 105)
    plt.legend()
    
    output_file =FIGFILE
    plt.savefig(output_file)
    print(f"Graph saved to {output_file}")

if __name__ == "__main__":
    main()