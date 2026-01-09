import json
import math
import matplotlib.pyplot as plt
import os

PATH='/users/jaewoo/data/amc'
DATAFILE=PATH+'/data/task_data.json'
FIGFILE=PATH+'/task_result.png'

# =========================================================
# 1. Helper Functions
# =========================================================

def ceil_div(a, b):
    """
    Returns ceil(a / b) using integer arithmetic.
    """
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
        return f"T{self.id}({self.criticality}, C={self.c_lo}/{self.c_hi}, T={self.t})"

# =========================================================
# 2. RTA Logic Components
# =========================================================

def get_response_time_lo(task, hp_tasks):
    """Calculates R_LO (Steady State LO)."""
    w = task.c_lo
    while True:
        # Calculate interference based on fixed window 'w'
        interference = 0
        for h_task in hp_tasks:
            interference += ceil_div(w, h_task.t) * h_task.c_lo
            
        w_new = task.c_lo + interference
        
        if w_new > task.d: return float('inf')
        if w_new == w: return w
        w = w_new

def m_function(task_k, s, t):
    """
    Implements Eq (11): M(k, s, t)
    """
    # Term A
    numerator = t - s - (task_k.t - task_k.d)
    term_a = ceil_div(numerator, task_k.t) + 1
    
    # Term B
    term_b = ceil_div(t, task_k.t)
    
    return max(0, min(term_a, term_b))

# =========================================================
# 3. AMC-max Baseline Test
# =========================================================

def check_schedulability_amc_max(task_i, hp_tasks):
    """
    Baseline AMC-max (Method 2).
    """
    # 1. R_LO
    r_lo = get_response_time_lo(task_i, hp_tasks)
    if r_lo > task_i.d: return False
    
    if task_i.criticality == 'LO':
        return True

    # 2. R_HI (Steady State HI)
    hp_hi_tasks = [t for t in hp_tasks if t.criticality == 'HI']
    r_hi = task_i.c_hi
    while True:
        interference = 0
        for h_task in hp_hi_tasks:
            interference += ceil_div(r_hi, h_task.t) * h_task.c_hi
        
        w_new = task_i.c_hi + interference
        if w_new > task_i.d: return False
        if w_new == r_hi: break
        r_hi = w_new
        
    # 3. Transition Phase
    hp_lo_tasks = [t for t in hp_tasks if t.criticality == 'LO']
    
    # S points: 0 and releases of LO tasks < R_LO
    s_points = {0}
    for l_task in hp_lo_tasks:
        t = 0
        while t < r_lo:
            s_points.add(t)
            t += l_task.t
            
    s_list = sorted(list(s_points))
    max_r_s = 0
    
    for s in s_list:
        if s >= r_lo: continue
        
        r_s = max(r_lo, r_hi)
        while True:
            lhs = task_i.c_hi
            
            # LO Interference (up to s)
            i_l = 0
            for l_task in hp_lo_tasks:
                num_jobs = (s // l_task.t) + 1
                i_l += num_jobs * l_task.c_lo
            
            # HI Interference (Mixed)
            i_h = 0
            for h_task in hp_hi_tasks:
                m_val = m_function(h_task, s, r_s)
                term_hi = m_val * h_task.c_hi
                
                total_jobs = ceil_div(r_s, h_task.t)
                term_lo = (total_jobs - m_val) * h_task.c_lo
                
                i_h += term_hi + term_lo
            
            w_new = lhs + i_l + i_h
            
            if w_new > task_i.d: return False
            if w_new <= r_s:
                max_r_s = max(max_r_s, w_new)
                break
            r_s = w_new
            
    return max(r_lo, max_r_s) <= task_i.d

# =========================================================
# 4. Proposed AMC-IO-US Test
# =========================================================

def check_schedulability_proposed(task, hp_tasks, shed_tasks, survive_tasks):
    """
    AMC-IO-US Test.
    """
    # 1. Steady State LO
    r_lo = get_response_time_lo(task, hp_tasks)
    if r_lo > task.d: return False
    
    # Shed LO tasks are safe by definition
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
                
                # HI Interference
                for h in hp_tasks:
                    if h.criticality == 'HI':
                        if h == u: # Faulting task
                            m_val = m_function(h, s, w)
                            term_hi = m_val * h.c_hi
                            term_lo = (ceil_div(w, h.t) - m_val) * h.c_lo
                            interference += term_hi + term_lo
                        else: # Normal HI task (Isolated assumption)
                            interference += ceil_div(w, h.t) * h.c_lo
                            
                # LO Interference
                hp_lo = [t for t in hp_tasks if t.criticality == 'LO']
                for l in hp_lo:
                    if l in shed_tasks: # Drop at s
                        num_jobs = (s // l.t) + 1
                        interference += num_jobs * l.c_lo
                    else: # Survive (Full interference)
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
# 5. Corrected OPA & Main Solver
# =========================================================

def run_opa(task_set, mode, **kwargs):
    """
    Corrected Audsley's Algorithm.
    hp_tasks should ONLY contain tasks from 'unassigned' (excluding self).
    Assigned tasks are LOWER priority, so they do NOT interfere.
    """
    unassigned = list(task_set)
    # We don't need to track assigned_priority for HP calculation, 
    # because they are lower priority.
    
    # Loop for N priority levels (Lowest to Highest)
    for _ in range(len(task_set)):
        candidate_found = False
        
        for i, task in enumerate(unassigned):
            # Check if 'task' can be at the current LOWEST available priority.
            # If so, the REMAINING unassigned tasks are Higher Priority.
            hp_tasks = unassigned[:i] + unassigned[i+1:]
            
            is_schedulable = False
            if mode == 'baseline':
                is_schedulable = check_schedulability_amc_max(task, hp_tasks)
            elif mode == 'proposed':
                is_schedulable = check_schedulability_proposed(
                    task, hp_tasks, kwargs['shed_tasks'], kwargs['survive_tasks']
                )
            
            if is_schedulable:
                # Task found for this priority level.
                # Remove from unassigned (it is now assigned to the current lowest level)
                unassigned.pop(i)
                candidate_found = True
                break
        
        if not candidate_found:
            return False # No task can fit in the current lowest level
            
    return True

def solve_baseline(task_set):
    return run_opa(task_set, 'baseline')

def solve_proposed_greedy(task_set):
    lo_tasks = [t for t in task_set if t.criticality == 'LO']
    lo_tasks.sort(key=lambda x: x.u_lo, reverse=True)
    
    for k in range(len(lo_tasks) + 1):
        curr_shed = set(lo_tasks[:k])
        curr_survive = set(lo_tasks[k:])
        
        if run_opa(task_set, 'proposed', shed_tasks=curr_shed, survive_tasks=curr_survive):
            return True
    return False

def main():
    if not os.path.exists(DATAFILE):
        print("Error: task_data.json not found.")
        return

    with open(DATAFILE, 'r') as f:
        data = json.load(f)
    
    util_points = data['util_points']
    tasksets_data = data['tasksets']
    
    base_res = []
    prop_res = []
    
    print("Running Schedulability Comparison (Final Fix)...")
    
    sorted_u = sorted([float(k) for k in tasksets_data.keys()])
    
    for u in sorted_u:
        u_str = next((k for k in tasksets_data.keys() if abs(float(k)-u) < 0.001), None)
        if not u_str: continue

        sets = tasksets_data[u_str]
        b_wins = 0
        p_wins = 0
        
        for ts_data in sets:
            task_set = [Task(t) for t in ts_data]
            
            if solve_baseline(task_set):
                b_wins += 1
            
            if solve_proposed_greedy(task_set):
                p_wins += 1
                
        base_rate = b_wins / len(sets) * 100
        prop_rate = p_wins / len(sets) * 100
        
        base_res.append(base_rate)
        prop_res.append(prop_rate)
        
        print(f"Util {u}: Base={base_rate:.1f}%, Proposed={prop_rate:.1f}%")

    plt.figure(figsize=(8, 6))
    plt.plot(sorted_u, base_res, 'o--', color='gray', label='AMC-max (Baseline)')
    plt.plot(sorted_u, prop_res, 's-', color='blue', label='AMC-IO-US (Proposed)')
    
    plt.xlabel('System Utilization')
    plt.ylabel('Schedulability Ratio (%)')
    plt.title('Schedulability Comparison (Corrected)')
    plt.ylim(0, 105)
    plt.grid(True)
    plt.legend()
    
    output_file =FIGFILE
    plt.savefig(output_file)
    print(f"Graph saved to {output_file}")

if __name__ == "__main__":
    main()