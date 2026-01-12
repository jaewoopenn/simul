import math
import matplotlib.pyplot as plt
import json
import os

PATH='/users/jaewoo/data/amc'
DATAFILE=PATH+'/data/task_data.json'
FIGFILE=PATH+'/result.png'

class Task:
    def __init__(self, data_dict):
        self.id = data_dict['id']
        self.c_lo = data_dict['c_lo']
        self.c_hi = data_dict['c_hi']
        self.d_lo = data_dict['d_lo']
        self.d_hi = data_dict['d_hi'] # 0 for LO tasks
        self.t = data_dict['t']
        self.criticality = data_dict['criticality']
        self.p_hi = 0 
        self.p_lo = 0

    @property
    def d_eff_amc(self):
        """Effective deadline for Standard AMC (Fixed Priority)."""
        if self.criticality == 'HI':
            return self.d_hi # Must satisfy tighter deadline
        return self.d_lo

def m_function(task_k, s, t):
    if t <= s: return 0
    term1 = math.ceil((t - s) / task_k.t) + 1
    term2 = math.ceil(t / task_k.t)
    return min(term1, term2)

# ---------------------------------------------------------
# 3. Standard AMC (Using Effective Deadline)
# ---------------------------------------------------------

def calc_rta_amc_max(task_i, hp_tasks):
    # 1. R_LO (Check against D_LO)
    r_lo = task_i.c_lo
    while True:
        w = task_i.c_lo
        for task_j in hp_tasks:
            w += math.ceil(r_lo / task_j.t) * task_j.c_lo
        # [NOTE] Even in standard AMC, in LO-mode we only strictly need to meet D_LO.
        # However, the priority assignment assumes we must safeguard D_HI eventualities.
        # But RTA itself checks physical completion.
        if w > task_i.d_lo: return float('inf')
        if w == r_lo: break
        r_lo = w
    
    if task_i.criticality == 'LO': return r_lo

    # 2. R_HI (Check against D_HI)
    hp_hi_tasks = [t for t in hp_tasks if t.criticality == 'HI']
    r_hi = task_i.c_hi
    while True:
        w = task_i.c_hi
        for task_j in hp_hi_tasks:
            w += math.ceil(r_hi / task_j.t) * task_j.c_hi
        if w > task_i.d_hi: return float('inf')
        if w == r_hi: break
        r_hi = w
        
    # 3. Transition (Check against D_HI)
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
            if w > task_i.d_hi: return float('inf') # Fail tight deadline
            if w <= r_s:
                max_r_s = max(max_r_s, w)
                break
            r_s = w
            
    return max(r_lo, max_r_s)

def check_schedulability_amc(task_set):
    """
    Standard AMC with Audsley.
    CRITICAL: Priority assignment uses D_eff = D_HI for HI tasks.
    This forces them to have high priority.
    """
    n = len(task_set)
    unassigned = task_set[:]
    
    for priority_level in range(n):
        candidate_found = False
        for i, candidate in enumerate(unassigned):
            hp_tasks = unassigned[:i] + unassigned[i+1:]
            
            # [KEY] Schedulability test
            rta = calc_rta_amc_max(candidate, hp_tasks)
            
            # Must satisfy its own deadline requirements
            # calc_rta_amc_max internally checks R_LO <= D_LO and R_HI <= D_HI
            if rta != float('inf'):
                unassigned.pop(i)
                candidate_found = True
                break
        
        if not candidate_found: return False
    return True

# ---------------------------------------------------------
# 4. VP-AMC (Mode-Dependent Logic)
# ---------------------------------------------------------

def calc_rta_vp_amc(task_i, hp_lo_candidates, all_tasks):
    # 1. LO-Mode Check (Check against D_LO - RELAXED!)
    r_lo = task_i.c_lo
    while True:
        w = task_i.c_lo
        for task_j in hp_lo_candidates:
            w += math.ceil(r_lo / task_j.t) * task_j.c_lo
        if w > task_i.d_lo: return float('inf'), float('inf')
        if w == r_lo: break
        r_lo = w
        
    if task_i.criticality == 'LO': return r_lo, 0

    # 2. HI-Mode Transition Check (Check against D_HI - TIGHT!)
    hh_set = []
    hl_set = []
    lh_set = []
    hp_lo_set = []
    
    for t in all_tasks:
        if t.id == task_i.id: continue
        is_in_hp_lo = t in hp_lo_candidates
        is_hi_crit = (t.criticality == 'HI')
        
        # P_HI based on D_HI (Tight Deadline Monotonic)
        is_higher_phi = False
        if is_hi_crit:
            if t.d_hi < task_i.d_hi: is_higher_phi = True
            elif t.d_hi == task_i.d_hi and t.id < task_i.id: is_higher_phi = True
        
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
                
            i_lh = sum([math.ceil(r_s / k.t) * k.c_hi for k in lh_set])
            
            w = lhs + i_lo + i_hl + i_hh + i_lh
            
            if w > task_i.d_hi: return r_lo, float('inf') # Check against tight D_HI
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
            
            # Calculate RTA
            r_lo, r_trans = calc_rta_vp_amc(candidate, hp_lo_candidates, task_set)
            
            # [KEY DIFFERENCE]
            # Standard AMC effectively forces P_LO to satisfy D_HI because priority is static.
            # VP-AMC allows P_LO to satisfy D_LO (r_lo <= candidate.d_lo),
            # while ensuring Transition satisfies D_HI (r_trans <= candidate.d_hi).
            if r_lo <= candidate.d_lo and r_trans <= candidate.d_hi:
                unassigned.pop(i)
                candidate_found = True
                break
        
        if not candidate_found: return False
    return True

def run_simulation(filename='task_data_md.json'):
    if not os.path.exists(filename):
        print(f"Error: {filename} not found.")
        return
    with open(filename, 'r') as f: all_data = json.load(f)
    
    util_points = all_data['util_points']
    results_amc = []
    results_vp_amc = []
    
    print(f"Running Mode-Dependent Simulation...")
    
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
        print(f"U={u:<4} | AMC: {r_amc:5.1f}% | VP-AMC: {r_vp:5.1f}%")

    plt.figure(figsize=(10,6))
    plt.plot(util_points, results_amc, 'o--', label='AMC (Static Prio)')
    plt.plot(util_points, results_vp_amc, 's-', label='VP-AMC (Mode-Dependent)')
    plt.title('Schedulability with Mode-Dependent Deadlines (Tightness=3x)')
    plt.xlabel('Utilization'); plt.ylabel('Schedulability (%)')
    plt.legend(); plt.grid(True)
    plt.savefig(FIGFILE)
    print("Saved to vp_amc_md_results.png")

if __name__ == "__main__":
    run_simulation(DATAFILE)