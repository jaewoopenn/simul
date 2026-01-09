import json
import math
import copy
import matplotlib.pyplot as plt
import os

PATH='/users/jaewoo/data/amc'
DATAFILE=PATH+'/data/task_data.json'
FIGFILE=PATH+'/task_result.png'

# --- 1. Task Model Definition ---
class Task:
    def __init__(self, task_data):
        self.id = task_data['id']
        self.c_lo = float(task_data['c_lo'])
        self.c_hi = float(task_data['c_hi'])
        self.d = int(task_data['d'])
        self.t = int(task_data['t'])
        self.criticality = task_data['criticality'] # 'LO' or 'HI'
        self.u_lo = self.c_lo / self.t
        self.priority = -1 # Assigned by OPA

    def __repr__(self):
        return f"T{self.id}({self.criticality}, U={self.u_lo:.2f})"

# --- 2. Response Time Analysis Helper Functions ---

def get_response_time_lo(task, hp_tasks):
    """
    Calculates response time in Normal Mode (all tasks execute C_LO).
    Standard fixed-priority response time analysis.
    """
    w = task.c_lo
    while True:
        w_new = task.c_lo
        for h_task in hp_tasks:
            w_new += math.ceil(w / h_task.t) * h_task.c_lo
        
        if w_new > task.d:
            return w_new # Deadline miss
        if w_new == w:
            return w_new
        w = w_new

def calculate_interference_amc_max(task_j, s, t):
    """
    Calculates interference from a HI task (task_j) in AMC-max analysis.
    Considers mode switch at time s.
    Logic from Eq (11) in the paper.
    """
    # Number of jobs of task_j that can execute fully in HI mode after s
    # M(j, s, t) = min( ceil((t - s - (T_j - D_j))/T_j) + 1, ceil(t/T_j) )
    # But usually simplified or strictly followed. Let's follow the paper Eq (11).
    
    # Note: The paper uses D_j <= T_j.
    term1 = math.ceil((t - s - (task_j.t - task_j.d)) / task_j.t) + 1
    term2 = math.ceil(t / task_j.t)
    m = min(term1, term2)
    
    # Ensure m is non-negative
    m = max(0, m) 
    
    # Interference: M jobs at C_HI, remaining jobs at C_LO
    # The remaining jobs count is (Total jobs - M)
    total_jobs = math.ceil(t / task_j.t)
    # Clamp m to not exceed total_jobs
    m = min(m, total_jobs)
    
    val = (m * task_j.c_hi) + ((total_jobs - m) * task_j.c_lo)
    return val

# --- 3. Schedulability Tests ---

def check_schedulability_amc_max_baseline(task, hp_tasks):
    """
    AMC-max (Baseline) Schedulability Test for a single task.
    - System-level mode switch.
    - If switch happens:
        - ALL LO tasks are dropped immediately (interference limited to s).
        - ALL HI tasks start behaving as HI (interference increases).
    """
    # 1. LO Mode Check
    r_lo = get_response_time_lo(task, hp_tasks)
    if r_lo > task.d:
        return False
    
    if task.criticality == 'LO':
        return True # In AMC, LO tasks only need to meet deadlines in LO mode.

    # 2. HI Mode Check (Only for HI tasks)
    # We need to find the worst-case response time considering all possible s.
    # s ranges from 0 to R_LO (of the task being analyzed).
    # Check s at release points of LO tasks in hp(i).
    
    # Filter hp tasks
    hp_hi = [t for t in hp_tasks if t.criticality == 'HI']
    hp_lo = [t for t in hp_tasks if t.criticality == 'LO']
    
    # Possible s points: 0 and release times of LO tasks < r_lo
    s_points = [0]
    # Optimisation: Only check s points that could maximize interference?
    # The paper suggests checking all release points of LO tasks in [0, R_LO).
    for l_task in hp_lo:
        k = 0
        while True:
            t_point = k * l_task.t
            if t_point >= r_lo:
                break
            s_points.append(t_point)
            k += 1
            
    # Prune duplicates and sort
    s_points = sorted(list(set(s_points)))
    
    max_r_star = 0
    
    for s in s_points:
        # Calculate R_star for a fixed s
        w = task.c_hi # Start with C_HI (self)
        
        while True:
            w_new = task.c_hi
            
            # Interference from HI tasks (AMC-max logic)
            for h_task in hp_hi:
                w_new += calculate_interference_amc_max(h_task, s, w)
            
            # Interference from LO tasks (Execute up to s)
            for l_task in hp_lo:
                # Floor(s/T) + 1 jobs
                # Eq (9) in paper
                w_new += (math.floor(s / l_task.t) + 1) * l_task.c_lo
                
            if w_new > task.d:
                return False # Deadline miss for this s
            
            if w_new == w:
                break
            w = w_new
        
        if w > max_r_star:
            max_r_star = w
            
    return max_r_star <= task.d


def check_schedulability_amc_io_us_proposed(task, hp_tasks, shed_tasks, survive_tasks):
    """
    AMC-IO-US (Proposed) Schedulability Test.
    - Isolated Overrun Hypothesis: Only ONE HI task (tau_u) overruns.
    - Utilization-based Shedding:
        - shed_tasks: Dropped at s (like AMC-max LO tasks).
        - survive_tasks: Continue normally (like HI tasks in LO mode).
    """
    # 1. LO Mode Check
    r_lo = get_response_time_lo(task, hp_tasks)
    if r_lo > task.d:
        return False
    
    # 2. Isolated Overrun Check
    # Must be safe for ANY single HI task u in hp(i) U {i} overrunning.
    # If task is LO, it only cares if it is in 'survive_tasks'.
    # But wait, if task is LO and survives, it must meet deadline even if a HI task overruns.
    # If task is LO and sheds, we don't care about its deadline in overrun mode.
    
    if task.criticality == 'LO':
        if task in shed_tasks:
            return True # It's allowed to drop, so safe by definition (in this context)
        # If it survives, it must tolerate the overrun of any higher priority HI task.
    
    # Identify potential faulting tasks (Priority >= Task)
    # Note: In OPA, we only know hp_tasks.
    # Faulting task u can be 'task' (if HI) or any HI task in hp_tasks.
    
    potential_faulting = [t for t in hp_tasks if t.criticality == 'HI']
    if task.criticality == 'HI':
        potential_faulting.append(task)
        
    if not potential_faulting:
        return True # No HI tasks to cause overrun
    
    hp_hi = [t for t in hp_tasks if t.criticality == 'HI']
    
    # For EACH potential faulting task u
    for u in potential_faulting:
        
        # We need to find worst-case s (overrun start time of u)
        # s points from release of Shedding LO tasks (Eq 12 logic applied to shed tasks)
        s_points = [0]
        # Only shed tasks generate step functions in interference
        for l_shed in shed_tasks:
            k = 0
            while True:
                t_point = k * l_shed.t
                if t_point >= r_lo:
                    break
                s_points.append(t_point)
                k += 1
        s_points = sorted(list(set(s_points)))

        max_r_u = 0
        
        for s in s_points:
            # Self execution time depends on if 'task' is the faulting one
            base_c = task.c_hi if (task == u) else task.c_lo
            
            w = base_c
            while True:
                w_new = base_c
                
                # 1. HI tasks interference
                for h_task in hp_hi:
                    if h_task == u:
                        # This is the faulting task
                        # Use AMC-max logic for u: mixed C_LO and C_HI based on s
                        w_new += calculate_interference_amc_max(h_task, s, w)
                    else:
                        # Non-faulting HI task -> Behave as LO (Isolated Assumption)
                        w_new += math.ceil(w / h_task.t) * h_task.c_lo
                
                # 2. LO tasks interference
                
                # A. Shed tasks -> Execute up to s
                for l_shed in shed_tasks:
                    w_new += (math.floor(s / l_shed.t) + 1) * l_shed.c_lo
                     
                # B. Survive tasks -> Execute fully
                for l_surv in survive_tasks:
                    w_new += math.ceil(w / l_surv.t) * l_surv.c_lo
                
                if w_new > task.d:
                    return False # Deadline miss for this u, s
                
                if w_new == w:
                    break
                w = w_new
            
            if w > max_r_u:
                max_r_u = w
                
        if max_r_u > task.d:
            return False # Failed for faulting task u
            
    return True


# --- 4. OPA (Audsley's Algorithm) ---

def run_opa(task_set, test_function, **kwargs):
    """
    Implements Audsley's Optimal Priority Assignment.
    test_function: function(task, hp_tasks, **kwargs) -> bool
    Returns: (is_schedulable, priority_assigned_tasks)
    """
    unassigned = list(task_set)
    assigned_priority = [] # From lowest priority (index 0) to highest
    
    # N priority levels. We fill from Lowest (Level 0) to Highest.
    # In fixed priority, usually 1 is highest. Here let's just use index list.
    # The last element in 'assigned_priority' has the lowest priority?
    # Audsley: Pick a task that can be lowest priority.
    
    while unassigned:
        candidate_found = False
        for i, task in enumerate(unassigned):
            # Check if 'task' can be at the current lowest priority level
            # It means all other tasks (unassigned + assigned) are Higher Priority
            hp_tasks = unassigned[:i] + unassigned[i+1:] + assigned_priority
            
            if test_function(task, hp_tasks, **kwargs):
                # Found a candidate for this level
                assigned_priority.append(task)
                unassigned.pop(i)
                candidate_found = True
                break
        
        if not candidate_found:
            return False, []
            
    return True, assigned_priority

# --- 5. Wrapper for Simulations ---

def solve_baseline(task_set):
    """
    Runs OPA with AMC-max Baseline.
    Returns: Success (bool), Survival Rate (0.0 or 1.0 - typically 0 if HI mode)
    """
    # For baseline, LO tasks are always dropped in HI mode.
    # Survival rate is 0% conceptually for HI mode analysis.
    success, _ = run_opa(task_set, check_schedulability_amc_max_baseline)
    return success, 0.0

def solve_proposed(task_set):
    """
    Runs Greedy Shedding Optimization with AMC-IO-US.
    1. Sort LO tasks by U.
    2. Try 0 shed. OPA.
    3. If fail, shed 1 (highest U). OPA.
    4. Repeat.
    """
    lo_tasks = [t for t in task_set if t.criticality == 'LO']
    # Sort by U descending
    lo_tasks.sort(key=lambda x: x.u_lo, reverse=True)
    
    total_lo_util = sum(t.u_lo for t in lo_tasks)
    if total_lo_util == 0: total_lo_util = 1.0 # Avoid div by zero
    
    # Try shedding k tasks (k = 0 to N)
    for k in range(len(lo_tasks) + 1):
        shed_list = lo_tasks[:k]
        survive_list = lo_tasks[k:]
        
        # Convert to sets for faster lookup in test function
        # But we pass lists to OPA, test function separates them using ID or object reference
        # We need to pass shed_tasks and survive_tasks to the test function
        # But OPA generates hp_tasks dynamically. 
        # We need to filter hp_tasks inside the test function or pass the global sets.
        
        # Correct approach: Pass the global definitions of shed/survive for this iteration
        # The test function will check if a specific hp task is in shed or survive set.
        
        success, _ = run_opa(task_set, check_schedulability_amc_io_us_proposed, 
                             shed_tasks=set(shed_list), survive_tasks=set(survive_list))
        
        if success:
            surviving_util = sum(t.u_lo for t in survive_list)
            return True, surviving_util / total_lo_util
            
    return False, 0.0

# --- 6. Main Execution ---

def main():
    # Load Data
    with open(DATAFILE, 'r') as f:
        data = json.load(f)
    
    util_points = data['util_points']
    tasksets_data = data['tasksets']
    
    results_baseline_sr = []
    results_proposed_sr = []
    
    results_baseline_survival = []
    results_proposed_survival = []
    
    print(f"Starting Simulation with {len(util_points)} utilization points...")
    
    for u_str in tasksets_data: # keys are strings "0.05", "0.1"...
        u_val = float(u_str)
        sets = tasksets_data[u_str]
        
        base_wins = 0
        prop_wins = 0
        base_surv_acc = 0.0
        prop_surv_acc = 0.0
        
        for ts_data in sets:
            # Parse TaskSet
            task_set = [Task(t) for t in ts_data]
            
            # 1. Baseline
            b_ok, b_surv = solve_baseline(task_set)
            if b_ok:
                base_wins += 1
                base_surv_acc += 0.0 # Baseline drops all in HI mode
            
            # 2. Proposed
            p_ok, p_surv = solve_proposed(task_set)
            if p_ok:
                prop_wins += 1
                prop_surv_acc += p_surv
        
        num_sets = len(sets)
        results_baseline_sr.append(base_wins / num_sets * 100)
        results_proposed_sr.append(prop_wins / num_sets * 100)
        
        # Average survival rate over SCHEDULABLE systems (or all? Usually schedulable)
        # If unschedulable, survival is undefined or 0. Let's average over all samples (0 if fail).
        results_baseline_survival.append(0.0)
        results_proposed_survival.append(prop_surv_acc / num_sets * 100)
        
        print(f"Util {u_val}: Base SR={base_wins}%, Prop SR={prop_wins}%, Prop Surv={prop_surv_acc/num_sets*100:.1f}%")

    # Plotting
    # Sort results by util (keys might be unordered)
    sorted_indices = sorted(range(len(util_points)), key=lambda k: util_points[k])
    x_axis = [util_points[i] for i in sorted_indices]
    
    y_base_sr = [results_baseline_sr[i] for i in sorted_indices]
    y_prop_sr = [results_proposed_sr[i] for i in sorted_indices]
    y_prop_surv = [results_proposed_survival[i] for i in sorted_indices]
    
    plt.figure(figsize=(12, 5))
    
    # Subplot 1: Schedulability Ratio
    plt.subplot(1, 2, 1)
    plt.plot(x_axis, y_base_sr, 'o--', label='AMC-max (Baseline)', color='gray')
    plt.plot(x_axis, y_prop_sr, 's-', label='AMC-IO-US (Proposed)', color='blue')
    plt.xlabel('System Utilization')
    plt.ylabel('Schedulability Ratio (%)')
    plt.title('Schedulability Comparison')
    plt.grid(True)
    plt.legend()
    
    # Subplot 2: Survival Rate
    plt.subplot(1, 2, 2)
    plt.plot(x_axis, [0]*len(x_axis), 'o--', label='AMC-max', color='gray')
    plt.plot(x_axis, y_prop_surv, 's-', label='AMC-IO-US', color='green')
    plt.xlabel('System Utilization')
    plt.ylabel('Avg. LO Task Survival (by Util %)')
    plt.title('Service Survivability in HI Mode')
    plt.grid(True)
    plt.legend()
    
    plt.tight_layout()
    plt.savefig(FIGFILE)
    print("Simulation Complete. Results saved to comparison_result.png")

if __name__ == "__main__":
    main()