import math
import matplotlib.pyplot as plt
import json
import os

PATH='/users/jaewoo/data/amc/rate'
DATAFILE=PATH+'/task_data.json'
FIGFILE=PATH+'/result.png'

class Task:
    def __init__(self, data_dict):
        self.id = data_dict['id']
        self.c = data_dict['c']
        self.t_lo = data_dict['t_lo']
        self.t_hi = data_dict['t_hi']
        self.d_lo = data_dict['d_lo']
        self.d_hi = data_dict['d_hi']
        self.criticality = data_dict['criticality']
        self.p_lo = 0

    @property
    def t_eff_amc(self):
        """Standard AMC uses the shortest period/deadline for priority."""
        if self.criticality == 'HI': return self.t_hi
        return self.t_lo

# ---------------------------------------------------------
# 1. Standard AMC (Static Priority)
# ---------------------------------------------------------

def calc_rta_amc_dual(task_i, hp_tasks):
    """
    RTA for Standard AMC in Dual-Rate System.
    Note: Priorities are fixed based on T_HI (for HI tasks).
    """
    
    # 1. LO-Mode Check (Target: D_LO)
    # Interference comes at T_LO rate for everyone.
    r_lo = task_i.c
    while True:
        w = task_i.c
        for task_j in hp_tasks:
            w += math.ceil(r_lo / task_j.t_lo) * task_j.c
        if w > task_i.d_lo: return float('inf')
        if w == r_lo: break
        r_lo = w
    
    if task_i.criticality == 'LO': return r_lo

    # 2. HI-Mode Check (Target: D_HI)
    # Only HI tasks run, at T_HI rate.
    hp_hi_tasks = [t for t in hp_tasks if t.criticality == 'HI']
    r_hi = task_i.c
    while True:
        w = task_i.c
        for task_j in hp_hi_tasks:
            w += math.ceil(r_hi / task_j.t_hi) * task_j.c
        if w > task_i.d_hi: return float('inf')
        if w == r_hi: break
        r_hi = w
        
    # 3. Transition Check (Target: D_HI)
    # Switch happens at s.
    # Before s: T_LO rate. After s: T_HI rate.
    
    # Find candidate s points (releases of LO tasks & Demoted HI tasks in LO mode)
    # In Standard AMC, there are no "Demoted" tasks, but LO tasks interfere up to s.
    max_r_s = 0
    s_points = {0}
    for task_j in hp_tasks:
        if task_j.criticality == 'LO': # Only LO tasks drop
            t = 0
            while t < r_lo:
                s_points.add(t)
                t += task_j.t_lo
    
    for s in s_points:
        if s >= r_lo: continue
        r_s = max(r_lo, r_hi)
        while True:
            lhs = task_i.c
            
            # I_LO: LO tasks (run at T_LO, stop at s)
            i_lo = 0
            for task_j in hp_tasks:
                if task_j.criticality == 'LO':
                    i_lo += (math.floor(s / task_j.t_lo) + 1) * task_j.c
            
            # I_HI: HI tasks (run at T_LO before s, T_HI after s)
            i_hi = 0
            for task_k in hp_hi_tasks:
                # Workload before s (Rate T_LO)
                work_pre = (math.floor(s / task_k.t_lo) + 1) * task_k.c
                
                # Workload after s (Rate T_HI)
                # Maximize jobs in [s, R]
                if r_s > s:
                    work_post = math.ceil((r_s - s) / task_k.t_hi) * task_k.c
                else:
                    work_post = 0
                
                # [Optimization] The task cannot carry over "partial" work instantly.
                # But conservative bound is sum of pre and post.
                # A tighter bound exists but this is sufficient for simulation.
                i_hi += work_pre + work_post
            
            w = lhs + i_lo + i_hi
            if w > task_i.d_hi: return float('inf')
            if w <= r_s:
                max_r_s = max(max_r_s, w)
                break
            r_s = w
            
    return max(r_lo, max_r_s)

def check_schedulability_amc(task_set):
    # Sort by Effective Period (T_HI implies High Priority)
    # DMPO equivalent
    sorted_tasks = sorted(task_set, key=lambda t: t.t_eff_amc)
    
    for i, task_i in enumerate(sorted_tasks):
        hp_tasks = sorted_tasks[:i]
        rta = calc_rta_amc_dual(task_i, hp_tasks)
        if rta == float('inf'): return False
    return True

# ---------------------------------------------------------
# 2. VP-AMC (Dynamic Priority)
# ---------------------------------------------------------

def calc_rta_vp_amc(task_i, hp_lo_candidates, all_tasks):
    # 1. LO-Mode Check (Target: D_LO)
    # Priority based on T_LO (Relaxed).
    r_lo = task_i.c
    while True:
        w = task_i.c
        for task_j in hp_lo_candidates:
            w += math.ceil(r_lo / task_j.t_lo) * task_j.c
        if w > task_i.d_lo: return float('inf'), float('inf')
        if w == r_lo: break
        r_lo = w
    
    if task_i.criticality == 'LO': return r_lo, 0

    # 2. HI-Mode Transition Check (Target: D_HI)
    # Priorities switch to P_HI (based on T_HI).
    
    # Identify Interference Sets
    hh_set = [] # Higher in LO, Higher in HI
    hl_set = [] # Higher in LO, Lower in HI (Demoted)
    lh_set = [] # Lower in LO, Higher in HI (Promoted - The Dangerous Ones)
    hp_lo_set = []
    
    for t in all_tasks:
        if t.id == task_i.id: continue
        is_in_hp_lo = t in hp_lo_candidates
        is_hi_crit = (t.criticality == 'HI')
        
        # P_HI based on T_HI
        is_higher_phi = False
        if is_hi_crit:
            if t.t_hi < task_i.t_hi: is_higher_phi = True
            elif t.t_hi == task_i.t_hi and t.id < task_i.id: is_higher_phi = True
            
        if is_in_hp_lo:
            if not is_hi_crit: hp_lo_set.append(t)
            else:
                if is_higher_phi: hh_set.append(t)
                else: hl_set.append(t)
        else:
            if is_hi_crit and is_higher_phi: lh_set.append(t)
            
    # S-Points (Releases in LO-mode)
    s_points = {0}
    # All tasks interfering in LO mode contribute to s-points
    interferers = hp_lo_set + hh_set + hl_set
    for t in interferers:
        time = 0
        while time < r_lo:
            s_points.add(time)
            time += t.t_lo # Rate is T_LO
            
    max_r_s = 0
    for s in s_points:
        if s >= r_lo: continue
        r_s = r_lo
        while True:
            lhs = task_i.c
            
            # I_LO: LO tasks (Rate T_LO, stop at s)
            i_lo = sum([(math.floor(s/t.t_lo) + 1) * t.c for t in hp_lo_set])
            
            # I_HL: Demoted HI tasks (Rate T_LO, effectively stop at s because P_HI is lower)
            i_hl = sum([(math.floor(s/t.t_lo) + 1) * t.c for t in hl_set])
            
            # I_HH: Stable High (Rate T_LO pre-s, Rate T_HI post-s)
            i_hh = 0
            for k in hh_set:
                pre = (math.floor(s/k.t_lo) + 1) * k.c
                post = 0
                if r_s > s: post = math.ceil((r_s - s)/k.t_hi) * k.c
                i_hh += pre + post
                
            # I_LH: Promoted HI tasks (Blocked pre-s, Rate T_HI post-s)
            i_lh = 0
            for k in lh_set:
                # Pre-s: Blocked (0 execution), but maybe carried over?
                # Conservative: Treat as if they start fresh at s with high rate
                if r_s > s:
                    # Plus 1 for the carry-over/start job
                    # Actually, if they were blocked, they have backlog.
                    # We assume worst case: full workload starts at s
                    count = math.ceil((r_s - s)/k.t_hi)
                    # Add 1 if we consider the job released exactly at s? 
                    # Ceiling handles it.
                    # Let's add 1 conservatively for the carry-in from LO-mode
                    i_lh += (count) * k.c # Simple ceiling is usually enough for "from s"
                    # But wait, if T_HI is small, ceiling is correct.
                    # Let's stick to ceiling.
                    
                    # To be safe and strictly dominating, let's add 1 job for carry-over
                    # if the task was active.
                    i_lh += k.c # The carry-over job
                    
            w = lhs + i_lo + i_hl + i_hh + i_lh
            
            if w > task_i.d_hi: return r_lo, float('inf')
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
            
            if r_lo <= candidate.d_lo and r_trans <= candidate.d_hi:
                unassigned.pop(i)
                candidate_found = True
                break
        
        if not candidate_found: return False
    return True

def run_simulation(filename='task_data_dual.json'):
    if not os.path.exists(filename):
        print(f"Error: {filename} not found.")
        return
    with open(filename, 'r') as f: all_data = json.load(f)
    
    util_points = all_data['util_points']
    results_amc = []
    results_vp_amc = []
    
    print(f"Running Dual-Rate Simulation...")
    
    for u in util_points:
        tasksets_data = all_data['tasksets'].get(str(u), [])
        total = len(tasksets_data)
        if total == 0:
            results_amc.append(0); results_vp_amc.append(0); continue
            
        c_amc = 0; c_vp = 0
        for data in tasksets_data:
            ts = [Task(d) for d in data]
            if check_schedulability_amc(ts): c_amc += 1
            if check_schedulability_vp_amc(ts): c_vp += 1
            
        r_amc = c_amc/total*100
        r_vp = c_vp/total*100
        results_amc.append(r_amc)
        results_vp_amc.append(r_vp)
        print(f"U_LO={u:<4} | AMC: {r_amc:5.1f}% | VP-AMC: {r_vp:5.1f}%")

    plt.figure(figsize=(10,6))
    plt.plot(util_points, results_amc, 'o--', label='Standard AMC')
    plt.plot(util_points, results_vp_amc, 's-', label='VP-AMC (Dual-Rate)')
    plt.title('Dual-Rate System (HI-Period = LO-Period / 4)')
    plt.xlabel('LO-Mode Utilization'); plt.ylabel('Schedulability (%)')
    plt.legend(); plt.grid(True)
    plt.savefig(FIGFILE)
    print("Saved to vp_amc_dual_results.png")

if __name__ == "__main__":
    run_simulation(DATAFILE)