'''
Created on 2026. 1. 8.

@author: jaewoo
'''
import random
import math
import json

PATH='/users/jaewoo/data/amc'
DATAFILE=PATH+'/data/task_data.json'

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

def generate_taskset_data(num_tasks, utilization, hi_prob=0.5, hi_factor=4.0):
    """Generates a random mixed-criticality task set with Constrained Deadlines."""
    tasks_data = []
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
            
        # ---------------------------------------------------------
        # [MODIFIED] Constrained Deadline Generation (D <= T)
        # ---------------------------------------------------------
        # Minimum feasible deadline is the worst-case execution time itself
        min_deadline = int(c_hi) if is_hi else int(c_lo)
        
        # Safety check: if C is already larger than T (due to UUnifast scaling or rounding),
        # cap C to T and set D = T.
        if min_deadline > period:
            min_deadline = period
            if is_hi: c_hi = period
            else: c_lo = period
            
        # Choose D uniformly in [C, T]
        # To make it slightly tighter/interesting, we can pick in [C, T]
        deadline = random.randint(min_deadline, period)
        
        # ---------------------------------------------------------
        
        # Enforce C_HI <= D (Double check after randomization)
        if is_hi and c_hi > deadline:
            c_hi = deadline
            # Adjust LO execution time to maintain ratio if possible, or just clamp
            c_lo = c_hi / hi_factor
            if c_lo < 1: c_lo = 1
            
        elif not is_hi and c_lo > deadline:
            c_lo = deadline
            
        # Store as dictionary
        task_dict = {
            'id': i,
            'c_lo': float(c_lo),
            'c_hi': float(c_hi),
            'd': int(deadline),
            't': int(period),
            'criticality': criticality
        }
        tasks_data.append(task_dict)
        
    return tasks_data

# ---------------------------------------------------------
# 2. Main Execution (Save to File)
# ---------------------------------------------------------

def main():
    # [UPDATED PARAMETERS for better visibility of VP-AMC benefits]
    num_tasks = 20          # Reduced from 1000 to 20
    hi_prob = 0.5
    hi_factor = 4.0         # Increased from 2.0 to 4.0 (Higher Penalty)
    num_sets_per_point = 100
    
    # Utilization points (0.05 to 0.95)
    util_points = [round(u/20.0, 2) for u in range(1, 20)] 
    
    all_data = {
        'parameters': {
            'num_tasks': num_tasks,
            'hi_prob': hi_prob,
            'hi_factor': hi_factor,
            'num_sets_per_point': num_sets_per_point,
            'deadline_model': 'Constrained (D <= T)'
        },
        'util_points': util_points,
        'tasksets': {} 
    }
    
    print(f"Generating Tasksets: N={num_tasks}, CP={hi_prob}, CF={hi_factor}, Deadline=Constrained")
    
    for u in util_points:
        print(f"Generating for Utilization {u}...")
        tasksets_for_u = []
        
        for _ in range(num_sets_per_point):
            while True:
                ts_data = generate_taskset_data(num_tasks, u, hi_prob, hi_factor)
                # Validity check: LO utilization sum <= 1.0 (Implicitly handled by UUnifast usually, but good to check)
                u_lo = sum([t['c_lo']/t['t'] for t in ts_data])
                if u_lo <= 1.0: 
                    tasksets_for_u.append(ts_data)
                    break
        
        all_data['tasksets'][str(u)] = tasksets_for_u
        
    filename = DATAFILE
    with open(filename, 'w') as f:
        json.dump(all_data, f, indent=2)
        
    print(f"\nDone! All tasksets saved to '{filename}'")

if __name__ == "__main__":
    main()