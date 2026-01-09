import random
import math
import matplotlib.pyplot as plt
import copy

PATH='/users/jaewoo/data/amc'
SAVEFIG=PATH+'/result.png'
# ---------------------------------------------------------
# 1. Task Model & Generation
# ---------------------------------------------------------

class Task:
    def __init__(self, t_id, c_lo, c_hi, d, t, criticality):
        self.id = t_id
        self.c_lo = c_lo
        self.c_hi = c_hi
        self.d = d
        self.t = t
        self.criticality = criticality # 'LO' or 'HI'
        
        # Priorities (Assigned later)
        self.p_hi = 0 # Higher value means higher priority
        self.p_lo = 0

    def __repr__(self):
        return f"T{self.id}({self.criticality}, C_L={self.c_lo:.1f}, C_H={self.c_hi:.1f}, D={self.d}, T={self.t})"

def uunifast(n, u_limit):
    """Generates n utilizations summing to u_limit using UUnifast algorithm."""
    utilizations = []
    sum_u = u_limit
    for i in range(1, n):
        next_sum_u = sum_u * (random.random() ** (1.0 / (n - i)))
        utilizations.append(sum_u - next_sum_u)
        sum_u = next_sum_u
    utilizations.append(sum_u)
    return utilizations

def generate_taskset(num_tasks, utilization, hi_prob=0.5, hi_factor=2.0):
    """Generates a random mixed-criticality task set."""
    tasks = []
    utils = uunifast(num_tasks, utilization)
    
    # Log-uniform period distribution: 10ms to 1000ms
    min_period = 10
    max_period = 1000
    
    for i in range(num_tasks):
        period = math.exp(random.uniform(math.log(min_period), math.log(max_period)))
        period = round(period)
        
        c_lo = utils[i] * period
        if c_lo < 1: c_lo = 1 # Minimum execution time
        
        is_hi = random.random() < hi_prob
        criticality = 'HI' if is_hi else 'LO'
        
        c_hi = c_lo
        if is_hi:
            c_hi = c_lo * hi_factor
        else:
            c_hi = 0 # LO tasks don't run in HI mode
            
        # Constrained Deadline (D <= T), here D=T for simplicity implicitly
        # but explicit D can be randomized. Using D=T (Implicit Deadline) for standard comparison.
        deadline = period 
        
        # Enforce C_HI <= D
        if is_hi and c_hi > deadline:
            # Scale down to fit (or discard, but scaling keeps utilization close)
            ratio = deadline / c_hi
            c_hi = deadline
            c_lo = c_lo * ratio
        elif not is_hi and c_lo > deadline:
            c_lo = deadline
            
        tasks.append(Task(i, c_lo, c_hi, deadline, period, criticality))
        
    return tasks

# ---------------------------------------------------------
# 2. Helper Math Functions for RTA
# ---------------------------------------------------------

def get_interfering_workload(task_k, time_window, mode='LO'):
    """Calculates workload of task_k in a given time_window."""
    # Workload = ceil(t / T) * C
    if time_window < 0: return 0
    val = math.ceil(time_window / task_k.t)
    if mode == 'LO':
        return val * task_k.c_lo
    else:
        return val * task_k.c_hi

def m_function(task_k, s, t):
    """
    Calculates the maximum number of jobs of task_k that can execute strictly 
    within [s, t] assuming one job is active at s.
    (See Eq 11 in AMC paper or outline)
    """
    # Number of full periods in (t-s) minus the first partial job handling
    # The paper defines M(k, s, t) = min( ceil((t - s - (T_k - D_k)) / T_k) + 1, ceil(t/T_k) )
    # Since D=T here, T_k - D_k = 0.
    term1 = math.ceil((t - s) / task_k.t) + 1
    term2 = math.ceil(t / task_k.t)
    return min(term1, term2)

# ---------------------------------------------------------
# 3. Standard AMC-max Implementation
# ---------------------------------------------------------

def calc_rta_amc_max(task_i, hp_tasks):
    """
    Calculates response time using AMC-max (Method 2).
    hp_tasks: set of tasks with higher priority than task_i
    """
    
    # 1. Calculate R_LO (Steady State LO)
    r_lo = task_i.c_lo
    while True:
        w = task_i.c_lo
        for task_j in hp_tasks:
            w += math.ceil(r_lo / task_j.t) * task_j.c_lo
        
        if w > task_i.d: return float('inf') # Fail
        if w == r_lo: break
        r_lo = w
    
    # If LO task, we are done
    if task_i.criticality == 'LO':
        return r_lo

    # 2. Calculate R_HI (Steady State HI) - ONLY HI tasks interfere
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
    # Iterate s over release points of LO tasks in [0, R_LO)
    # Optimization: Check s = 0 and s = releases.
    # We check s at discrete points.
    
    max_r_s = 0
    
    # Determine candidate s points: 0 and arrival times of all HP LO tasks < r_lo
    s_points = {0}
    for task_j in hp_tasks:
        if task_j.criticality == 'LO':
            t = 0
            while t < r_lo:
                s_points.add(t)
                t += task_j.t
    
    for s in s_points:
        if s >= r_lo: continue
        
        # Solve for R_s
        # Start guess
        r_s = max(r_lo, r_hi) # heuristic start
        
        while True:
            # Base load
            lhs = task_i.c_hi
            
            # I_L(s): LO tasks interference (up to s)
            i_l = 0
            for task_j in hp_tasks:
                if task_j.criticality == 'LO':
                    # floor(s/T) + 1
                    i_l += (math.floor(s / task_j.t) + 1) * task_j.c_lo
            
            # I_H(s): HI tasks interference
            i_h = 0
            for task_k in hp_hi_tasks:
                m_val = m_function(task_k, s, r_s)
                term_hi = m_val * task_k.c_hi
                term_lo = (math.ceil(r_s / task_k.t) - m_val) * task_k.c_lo
                i_h += term_hi + term_lo
            
            w = lhs + i_l + i_h
            
            if w > task_i.d: 
                return float('inf')
            if w <= r_s: # Converged
                max_r_s = max(max_r_s, w)
                break
            r_s = w
            
    return max(r_lo, max_r_s)

def check_schedulability_amc(task_set):
    """
    Checks schedulability using Audsley's Algorithm with AMC-max.
    """
    n = len(task_set)
    unassigned = task_set[:]
    assigned_priority = {} # Task ID -> Priority (Higher val = Higher Prio)
    
    # Loop from lowest priority (level 0) to highest (level n-1)
    for priority_level in range(n):
        candidate_found = False
        
        for i, candidate in enumerate(unassigned):
            # hp_tasks are all OTHER unassigned tasks (assumed higher) 
            # + already assigned (actually, Audsley's builds from bottom. 
            # The ones NOT YET assigned are the 'Higher Priority' ones relative to current level)
            hp_tasks = unassigned[:i] + unassigned[i+1:]
            
            rta = calc_rta_amc_max(candidate, hp_tasks)
            
            if rta <= candidate.d:
                # Assign priority
                assigned_priority[candidate.id] = priority_level
                unassigned.pop(i)
                candidate_found = True
                break
        
        if not candidate_found:
            return False # Schedulability check failed
            
    return True

# ---------------------------------------------------------
# 4. Virtual Priority (VP-AMC) Implementation
# ---------------------------------------------------------

def calc_rta_vp_amc(task_i, hp_lo_candidates, all_tasks):
    """
    Calculates RTA for VP-AMC.
    task_i: Candidate task for current LO-priority level.
    hp_lo_candidates: Tasks that would be Higher Priority in LO-mode (unassigned ones).
    all_tasks: Needed to identify LH (Promoted) tasks.
    """
    
    # 1. LO-Mode Check (Same as Standard, uses hp_lo_candidates)
    r_lo = task_i.c_lo
    while True:
        w = task_i.c_lo
        for task_j in hp_lo_candidates:
            w += math.ceil(r_lo / task_j.t) * task_j.c_lo
        if w > task_i.d: return float('inf'), float('inf') # Return R_LO, R_Transition
        if w == r_lo: break
        r_lo = w
        
    if task_i.criticality == 'LO':
        return r_lo, 0 # LO tasks only care about R_LO

    # 2. HI-Mode Transition Check
    
    # Identify Sets relative to task_i
    # HH: HI tasks in hp_lo_candidates AND Higher P_HI
    # HL: HI tasks in hp_lo_candidates AND Lower P_HI
    # LH: HI tasks NOT in hp_lo_candidates AND Higher P_HI (The dangerous ones)
    
    hh_set = []
    hl_set = []
    lh_set = []
    hp_lo_set = [] # LO-criticality tasks in HP_LO
    
    for t in all_tasks:
        if t.id == task_i.id: continue
        
        is_in_hp_lo = t in hp_lo_candidates
        is_hi_crit = (t.criticality == 'HI')
        
        # P_HI is fixed by DMPO. 
        # Since we generated D=T, shorter T = Higher P_HI.
        # Break ties with ID.
        is_higher_phi = False
        if t.d < task_i.d: is_higher_phi = True
        elif t.d == task_i.d and t.id < task_i.id: is_higher_phi = True
        
        if is_in_hp_lo:
            if not is_hi_crit:
                hp_lo_set.append(t)
            else:
                if is_higher_phi: hh_set.append(t)
                else: hl_set.append(t)
        else:
            # Not in HP_LO (so P_LO(t) < P_LO(task_i))
            if is_hi_crit and is_higher_phi:
                lh_set.append(t)
                
    # Search over s in [0, R_LO)
    # s points: releases of LO tasks in HP_LO set + HL set (since they run in LO mode)
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
        
        r_s = r_lo # Start guess
        while True:
            lhs = task_i.c_hi
            
            # I_LO(s): LO tasks (valid up to s)
            i_lo = sum([(math.floor(s/t.t) + 1) * t.c_lo for t in hp_lo_set])
            
            # I_HL(s): Demoted HI tasks (valid up to s, treated like LO)
            i_hl = sum([(math.floor(s/t.t) + 1) * t.c_lo for t in hl_set])
            
            # I_HH(s): Stable High (AMC-max style interference)
            i_hh = 0
            for k in hh_set:
                m = m_function(k, s, r_s)
                i_hh += m * k.c_hi + (math.ceil(r_s/k.t) - m) * k.c_lo
                
            # I_LH(s): Promoted HI tasks (valid AFTER s)
            # This is the new term.
            i_lh = 0
            for k in lh_set:
                # Carry-over (1 job) + New arrivals in [s, R_s]
                # Conservative bound: 1 + ceil((R_s - s)/T)
                if r_s > s:
                    count = 1 + math.ceil((r_s - s) / k.t)
                    i_lh += count * k.c_hi
                else:
                    # If R_s <= s (unlikely but math safe), interference is just the carry over if active
                    i_lh += 1 * k.c_hi 
            
            w = lhs + i_lo + i_hl + i_hh + i_lh
            
            if w > task_i.d: return r_lo, float('inf')
            if w <= r_s:
                max_r_s = max(max_r_s, w)
                break
            r_s = w
            
    return r_lo, max_r_s

def check_schedulability_vp_amc(task_set):
    """
    Checks VP-AMC schedulability.
    Step 1: P_HI is fixed by DMPO.
    Step 2: P_LO assigned by Audsley.
    """
    n = len(task_set)
    unassigned = task_set[:]
    
    # In this function, we don't need to explicitly store P_HI 
    # because 'calc_rta_vp_amc' calculates P_HI relationships dynamically using Deadlines.
    
    # Audsley's loop for P_LO (Lowest to Highest)
    for priority_level in range(n):
        candidate_found = False
        
        for i, candidate in enumerate(unassigned):
            # hp_lo_candidates are the other unassigned tasks
            hp_lo_candidates = unassigned[:i] + unassigned[i+1:]
            
            r_lo, r_trans = calc_rta_vp_amc(candidate, hp_lo_candidates, task_set)
            
            # Check both conditions
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

def run_simulation():
    # Parameters
    num_tasks = 20
    hi_prob = 0.5
    hi_factor = 2.0
    num_sets_per_point = 100
    
    util_points = [u/20.0 for u in range(1, 20)] # 0.05 to 0.95
    
    results_amc = []
    results_vp_amc = []
    
    print(f"Starting Simulation: N={num_tasks}, CP={hi_prob}, CF={hi_factor}")
    print(f"Iterating Util from {util_points[0]} to {util_points[-1]}...")
    
    for u in util_points:
        count_amc = 0
        count_vp_amc = 0
        
        for _ in range(num_sets_per_point):
            # Generate Task Set
            while True:
                ts = generate_taskset(num_tasks, u, hi_prob, hi_factor)
                # Filter out trivially unschedulable sets (U > 1) if necessary, 
                # but UUnifast handles U sum. Individual C_HI check is done in generation.
                # Just check simple LO-mode utilization valid
                u_lo = sum([t.c_lo/t.t for t in ts])
                if u_lo <= 1.0: break
            
            # Test AMC
            if check_schedulability_amc(ts):
                count_amc += 1
                # If AMC schedulable, VP-AMC should also be (dominance property)
                # But let's run it to verify implementation correctness
                if check_schedulability_vp_amc(ts):
                    count_vp_amc += 1
                else:
                    print("ERROR: Dominance violation! AMC passed but VP-AMC failed.")
                    # Debug print
                    # print(ts)
            else:
                # AMC Failed, Check VP-AMC
                if check_schedulability_vp_amc(ts):
                    count_vp_amc += 1
                    
        ratio_amc = (count_amc / num_sets_per_point) * 100
        ratio_vp_amc = (count_vp_amc / num_sets_per_point) * 100
        
        results_amc.append(ratio_amc)
        results_vp_amc.append(ratio_vp_amc)
        
        print(f"U={u:.2f} | AMC: {ratio_amc:.1f}% | VP-AMC: {ratio_vp_amc:.1f}%")

    # Plotting
    plt.figure(figsize=(10, 6))
    plt.plot(util_points, results_amc, marker='o', linestyle='--', label='AMC-max (Standard)')
    plt.plot(util_points, results_vp_amc, marker='s', linestyle='-', label='VP-AMC (Virtual Priority)')
    
    plt.title(f'Schedulability Comparison (N={num_tasks}, CP={hi_prob}, CF={hi_factor})')
    plt.xlabel('Utilization (LO)')
    plt.ylabel('Schedulability (%)')
    plt.legend()
    plt.grid(True)
    plt.ylim(0, 110)
    
    # Save/Show
    plt.savefig(SAVEFIG)
    print("Graph saved to vp_amc_results.png")
    # plt.show() # Uncomment if running locally with display

if __name__ == "__main__":
    run_simulation()