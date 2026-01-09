import math
import matplotlib.pyplot as plt
import json
import os

PATH='/users/jaewoo/data/amc'
DATAFILE=PATH+'/data/task_data.json'
SAVEFIG=PATH+'/result.png'
# ---------------------------------------------------------
# 1. Task Model (Rehydrated from JSON)
# ---------------------------------------------------------

class Task:
    def __init__(self, data_dict):
        self.id = data_dict['id']
        self.c_lo = data_dict['c_lo']
        self.c_hi = data_dict['c_hi']
        self.d = data_dict['d']
        self.t = data_dict['t']
        self.criticality = data_dict['criticality']
        
        self.p_hi = 0 
        self.p_lo = 0

    def __repr__(self):
        return f"T{self.id}({self.criticality}, C_L={self.c_lo:.1f}, C_H={self.c_hi:.1f}, D={self.d})"

# ---------------------------------------------------------
# 2. Helper Math Functions for RTA
# ---------------------------------------------------------

def m_function(task_k, s, t):
    """
    Calculates the maximum number of jobs of task_k that can execute strictly 
    within [s, t] assuming one job is active at s.
    """
    if t <= s: return 0
    term1 = math.ceil((t - s) / task_k.t) + 1
    term2 = math.ceil(t / task_k.t)
    return min(term1, term2)

# ---------------------------------------------------------
# 3. Standard AMC-max Implementation
# ---------------------------------------------------------

def calc_rta_amc_max(task_i, hp_tasks):
    """Calculates response time using AMC-max (Method 2)."""
    
    # 1. Calculate R_LO (Steady State LO)
    r_lo = task_i.c_lo
    while True:
        w = task_i.c_lo
        for task_j in hp_tasks:
            w += math.ceil(r_lo / task_j.t) * task_j.c_lo
        
        if w > task_i.d: return float('inf')
        if w == r_lo: break
        r_lo = w
    
    if task_i.criticality == 'LO':
        return r_lo

    # 2. Calculate R_HI (Steady State HI)
    hp_hi_tasks = [t for t in hp_tasks if t.criticality == 'HI']
    r_hi = task_i.c_hi
    while True:
        w = task_i.c_hi
        for task_j in hp_hi_tasks:
            w += math.ceil(r_hi / task_j.t) * task_j.c_hi
            
        if w > task_i.d: return float('inf')
        if w == r_hi: break
        r_hi = w
        
    # 3. Calculate Transition Phase (AMC-max)
    max_r_s = 0
    s_points = {0}
    for task_j in hp_tasks:
        if task_j.criticality == 'LO':
            t = 0
            while t < r_lo:
                s_points.add(t)
                t += task_j.t
    
    for s in s_points:
        if s >= r_lo: continue
        
        r_s = max(r_lo, r_hi) 
        while True:
            lhs = task_i.c_hi
            
            i_l = 0
            for task_j in hp_tasks:
                if task_j.criticality == 'LO':
                    i_l += (math.floor(s / task_j.t) + 1) * task_j.c_lo
            
            i_h = 0
            for task_k in hp_hi_tasks:
                m_val = m_function(task_k, s, r_s)
                term_hi = m_val * task_k.c_hi
                term_lo = (math.ceil(r_s / task_k.t) - m_val) * task_k.c_lo
                i_h += term_hi + term_lo
            
            w = lhs + i_l + i_h
            
            if w > task_i.d: return float('inf')
            if w <= r_s:
                max_r_s = max(max_r_s, w)
                break
            r_s = w
            
    return max(r_lo, max_r_s)

def check_schedulability_amc(task_set):
    """Checks schedulability using Audsley's Algorithm with AMC-max."""
    n = len(task_set)
    unassigned = task_set[:]
    
    for priority_level in range(n):
        candidate_found = False
        for i, candidate in enumerate(unassigned):
            hp_tasks = unassigned[:i] + unassigned[i+1:]
            rta = calc_rta_amc_max(candidate, hp_tasks)
            
            if rta <= candidate.d:
                unassigned.pop(i)
                candidate_found = True
                break
        
        if not candidate_found:
            return False
    return True

# ---------------------------------------------------------
# 4. Virtual Priority (VP-AMC) Implementation
# ---------------------------------------------------------

def calc_rta_vp_amc(task_i, hp_lo_candidates, all_tasks):
    """Calculates RTA for VP-AMC using Dual Priority logic."""
    
    # 1. LO-Mode Check
    r_lo = task_i.c_lo
    while True:
        w = task_i.c_lo
        for task_j in hp_lo_candidates:
            w += math.ceil(r_lo / task_j.t) * task_j.c_lo
        if w > task_i.d: return float('inf'), float('inf')
        if w == r_lo: break
        r_lo = w
        
    if task_i.criticality == 'LO':
        return r_lo, 0

    # 2. HI-Mode Transition Check
    hh_set = []
    hl_set = []
    lh_set = []
    hp_lo_set = []
    
    for t in all_tasks:
        if t.id == task_i.id: continue
        
        is_in_hp_lo = t in hp_lo_candidates
        is_hi_crit = (t.criticality == 'HI')
        
        # P_HI is fixed by DMPO (Implicit D=T, so smaller T = higher P)
        is_higher_phi = False
        if t.d < task_i.d: is_higher_phi = True
        elif t.d == task_i.d and t.id < task_i.id: is_higher_phi = True
        
        if is_in_hp_lo:
            if not is_hi_crit: hp_lo_set.append(t)
            else:
                if is_higher_phi: hh_set.append(t)
                else: hl_set.append(t)
        else:
            if is_hi_crit and is_higher_phi: lh_set.append(t)
                
    s_points = {0}
    interfering_lo_mode = hp_lo_set + hl_set + hh_set
    for t in interfering_lo_mode:
        time = 0
        while time < r_lo:
            s_points.add(time)
            time += t.t
            
    max_r_s = 0
    for s in s_points:
        if s >= r_lo: continue
        
        r_s = r_lo 
        while True:
            lhs = task_i.c_hi
            
            i_lo = sum([(math.floor(s/t.t) + 1) * t.c_lo for t in hp_lo_set])
            i_hl = sum([(math.floor(s/t.t) + 1) * t.c_lo for t in hl_set])
            
            i_hh = 0
            for k in hh_set:
                m = m_function(k, s, r_s)
                i_hh += m * k.c_hi + (math.ceil(r_s/k.t) - m) * k.c_lo
                
            # [CRITICAL FIX]: LH interference logic
            # LH tasks are blocked in [0, s) because P_LO(k) < P_LO(i).
            # They become higher priority at s.
            # Total interference is the total work released in [0, R_s]
            i_lh = sum([math.ceil(r_s / k.t) * k.c_hi for k in lh_set])
            
            w = lhs + i_lo + i_hl + i_hh + i_lh
            
            if w > task_i.d: return r_lo, float('inf')
            if w <= r_s:
                max_r_s = max(max_r_s, w)
                break
            r_s = w
            
    return r_lo, max_r_s

def check_schedulability_vp_amc(task_set):
    n = len(task_set)
    unassigned = task_set[:]
    
    for priority_level in range(n):
        candidate_found = False
        for i, candidate in enumerate(unassigned):
            hp_lo_candidates = unassigned[:i] + unassigned[i+1:]
            
            r_lo, r_trans = calc_rta_vp_amc(candidate, hp_lo_candidates, task_set)
            
            if r_lo <= candidate.d and r_trans <= candidate.d:
                unassigned.pop(i)
                candidate_found = True
                break
        
        if not candidate_found:
            return False
            
    return True

# ---------------------------------------------------------
# 5. Simulation Runner
# ---------------------------------------------------------

def run_simulation_from_file(filename='task_data.json'):
    if not os.path.exists(filename):
        print(f"Error: File '{filename}' not found.")
        return

    print(f"Loading tasksets from {filename}...")
    with open(filename, 'r') as f:
        all_data = json.load(f)
        
    util_points = all_data['util_points']
    results_amc = []
    results_vp_amc = []
    
    print("Running Simulation...")
    
    for u in util_points:
        u_key = str(u)
        tasksets_data = all_data['tasksets'].get(u_key, [])
        total_sets = len(tasksets_data)
        
        if total_sets == 0:
            results_amc.append(0)
            results_vp_amc.append(0)
            continue
            
        count_amc = 0
        count_vp_amc = 0
        
        for ts_data in tasksets_data:
            ts = [Task(t_data) for t_data in ts_data]
            
            is_amc = check_schedulability_amc(ts)
            if is_amc: count_amc += 1
            
            is_vp = check_schedulability_vp_amc(ts)
            if is_vp: count_vp_amc += 1
            
            if is_amc and not is_vp:
                print(f"CRITICAL ERROR: Dominance violation at U={u}")
                # Optional: print(ts) to debug specific set
        
        ratio_amc = (count_amc / total_sets) * 100
        ratio_vp_amc = (count_vp_amc / total_sets) * 100
        
        results_amc.append(ratio_amc)
        results_vp_amc.append(ratio_vp_amc)
        
        print(f"U={u:<4} | AMC: {ratio_amc:5.1f}% | VP-AMC: {ratio_vp_amc:5.1f}%")

    plt.figure(figsize=(10, 6))
    plt.plot(util_points, results_amc, marker='o', linestyle='--', label='AMC-max (Standard)')
    plt.plot(util_points, results_vp_amc, marker='s', linestyle='-', label='VP-AMC (Virtual Priority)')
    
    plt.title('Schedulability Comparison')
    plt.xlabel('Utilization (LO)')
    plt.ylabel('Schedulability (%)')
    plt.legend()
    plt.grid(True)
    plt.ylim(0, 110)
    
    plt.savefig(SAVEFIG)
    print("\nGraph saved to vp_amc_results.png")

if __name__ == "__main__":
    run_simulation_from_file(DATAFILE)