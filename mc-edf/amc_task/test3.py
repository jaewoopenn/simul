import json
import math
import matplotlib.pyplot as plt
import os
import numpy as np


PATH='/users/jaewoo/data/amc'
DATAFILE=PATH+'/data/task_data.json'
FIGFILE=PATH+'/survival_result.png'
# =========================================================
# 1. Helper Functions & Classes (Same as v3)
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
        return f"T{self.id}({self.criticality}, U={self.u_lo:.3f})"

# =========================================================
# 2. Analysis Logic (Same as v3)
# =========================================================

def get_response_time_lo(task, hp_tasks):
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
    numerator = t - s - (task_k.t - task_k.d)
    term_a = ceil_div(numerator, task_k.t) + 1
    term_b = ceil_div(t, task_k.t)
    return max(0, min(term_a, term_b))

def check_schedulability_proposed(task, hp_tasks, shed_tasks, survive_tasks):
    # 1. Steady State LO
    r_lo = get_response_time_lo(task, hp_tasks)
    if r_lo > task.d: return False
    
    # If LO task is shed, it is safe by definition
    if task.criticality == 'LO' and task in shed_tasks:
        return True
    
    # 2. Isolated Overrun Scenarios
    overrunners = [t for t in hp_tasks if t.criticality == 'HI']
    if task.criticality == 'HI':
        overrunners.append(task)
        
    if not overrunners: return True
        
    for u in overrunners:
        # S points from SHEDDING tasks only
        s_points = {0}
        for l_shed in shed_tasks:
            curr = l_shed.t
            while curr < r_lo:
                s_points.add(curr)
                curr += l_shed.t
        s_list = sorted(list(s_points))
        
        max_r_u = 0
        for s in s_list:
            base_c = task.c_hi if (task == u) else (task.c_lo if task.criticality=='HI' else task.c_lo) 
            
            w = base_c
            while True:
                interference = 0
                for h in hp_tasks:
                    if h.criticality == 'HI':
                        if h == u: 
                            m_val = m_function(h, s, w)
                            term_hi = m_val * h.c_hi
                            term_lo = (ceil_div(w, h.t) - m_val) * h.c_lo
                            interference += term_hi + term_lo
                        else:
                            interference += ceil_div(w, h.t) * h.c_lo
                
                hp_lo = [t for t in hp_tasks if t.criticality == 'LO']
                for l in hp_lo:
                    if l in shed_tasks:
                        num_jobs = (s // l.t) + 1
                        interference += num_jobs * l.c_lo
                    else:
                        interference += ceil_div(w, l.t) * l.c_lo
                
                w_new = base_c + interference
                if w_new > task.d: 
                    max_r_u = float('inf')
                    break
                if w_new == w: 
                    max_r_u = max(max_r_u, w_new)
                    break
                w = w_new
            if max_r_u > task.d: break 
        if max_r_u > task.d: return False
            
    return True

# =========================================================
# 3. Solver & Stats Collection
# =========================================================

def run_opa(task_set, shed_tasks, survive_tasks):
    unassigned = list(task_set)
    for _ in range(len(task_set)):
        candidate_found = False
        for i, task in enumerate(unassigned):
            hp_tasks = unassigned[:i] + unassigned[i+1:]
            if check_schedulability_proposed(task, hp_tasks, shed_tasks, survive_tasks):
                unassigned.pop(i)
                candidate_found = True
                break
        if not candidate_found: return False
    return True

def calculate_survival_rate(task_set):
    """
    Returns the ratio of Util_LO_Surviving / Util_LO_Total.
    Uses Greedy Shedding Strategy.
    """
    lo_tasks = [t for t in task_set if t.criticality == 'LO']
    if not lo_tasks: return 1.0 # No LO tasks to drop
    
    # Sort LO tasks by U descending (Drop Heavy first)
    lo_tasks.sort(key=lambda x: x.u_lo, reverse=True)
    
    total_lo_u = sum(t.u_lo for t in lo_tasks)
    if total_lo_u == 0: return 1.0
    
    # Try shedding k=0, 1, 2...
    for k in range(len(lo_tasks) + 1):
        curr_shed = set(lo_tasks[:k])
        curr_survive = set(lo_tasks[k:])
        
        if run_opa(task_set, curr_shed, curr_survive):
            # Success! Calculate rate
            surviving_u = sum(t.u_lo for t in curr_survive)
            return surviving_u / total_lo_u
            
    # If dropping ALL LO tasks still fails (e.g. HI tasks overloaded), survival is 0.0
    # Or technically undefined, but for stats we can say 0.0 service available.
    return 0.0

# =========================================================
# 4. Main Experiment
# =========================================================

def main():
    if not os.path.exists(DATAFILE):
        print("Error: task_data.json not found.")
        return

    with open(DATAFILE, 'r') as f:
        data = json.load(f)
    
    util_points = data['util_points']
    tasksets_data = data['tasksets']
    
    avg_survival_rates = []
    amc_survival_rates = [] # Always 0, for reference line
    
    print("Running Survival Rate Analysis...")
    
    sorted_u = sorted([float(k) for k in tasksets_data.keys()])
    
    for u in sorted_u:
        u_str = next((k for k in tasksets_data.keys() if abs(float(k)-u) < 0.001), None)
        if not u_str: continue

        sets = tasksets_data[u_str]
        survival_accum = 0.0
        valid_count = 0
        
        for ts_data in sets:
            task_set = [Task(t) for t in ts_data]
            
            rate = calculate_survival_rate(task_set)
            
            # Optional: Exclude completely unschedulable sets?
            # Or include them as 0? Typically include as 0 to show system capacity limit.
            survival_accum += rate
            valid_count += 1
            
        avg_rate = (survival_accum / valid_count * 100) if valid_count > 0 else 0
        avg_survival_rates.append(avg_rate)
        amc_survival_rates.append(0.0) # AMC baseline
        
        print(f"Util {u}: Avg Survival Rate = {avg_rate:.1f}%")

    # Plot
    plt.figure(figsize=(8, 6))
    
    # Baseline (AMC-max) - Gray Dashed
    plt.plot(sorted_u, amc_survival_rates, 'o--', color='gray', label='AMC-max (Survival=0%)')
    
    # Proposed - Green Solid
    plt.plot(sorted_u, avg_survival_rates, 's-', color='green', label='AMC-IO-US (Proposed)')
    
    # Formatting
    plt.xlabel('System Utilization')
    plt.ylabel('LO Task Survival Rate (Weighted by Utilization) [%]')
    plt.title('Service Availability in Overrun Mode')
    plt.ylim(-5, 105) # Slightly below 0 for visibility
    plt.grid(True)
    plt.legend()
    
    # Fill area to highlight benefit
    plt.fill_between(sorted_u, 0, avg_survival_rates, color='green', alpha=0.1)
    
    output_file = FIGFILE
    plt.savefig(output_file)
    print(f"Graph saved to {output_file}")

if __name__ == "__main__":
    main()