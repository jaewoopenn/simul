import random
import math
import json

PATH='/users/jaewoo/data/amc/rate'
DATAFILE=PATH+'/task_data.json'

def uunifast(n, u_limit):
    """Generates n utilizations summing to u_limit."""
    utilizations = []
    sum_u = u_limit
    for i in range(1, n):
        next_sum_u = sum_u * (random.random() ** (1.0 / (n - i)))
        utilizations.append(sum_u - next_sum_u)
        sum_u = next_sum_u
    utilizations.append(sum_u)
    return utilizations

def generate_taskset_dual_rate(num_tasks, utilization, hi_prob=0.5, rate_factor=2.0):
    """
    Generates Dual-Rate Tasksets.
    rate_factor: T_LO / T_HI ratio (e.g., 4.0 means 4x faster in HI mode).
    """
    tasks_data = []
    
    # 1. Generate Base Utilizations (for LO-Mode)
    utils = uunifast(num_tasks, utilization)
    
    min_period = 100
    max_period = 1000
    
    for i in range(num_tasks):
        # Base Period (LO-Mode Period)
        t_lo = math.exp(random.uniform(math.log(min_period), math.log(max_period)))
        t_lo = int(round(t_lo))
        
        c = utils[i] * t_lo
        if c < 1: c = 1
        
        is_hi = random.random() < hi_prob
        criticality = 'HI' if is_hi else 'LO'
        
        # [KEY LOGIC: DUAL RATE]
        if is_hi:
            # HI-Task: Runs faster in HI-Mode
            t_hi = int(t_lo / rate_factor)
            if t_hi < c: t_hi = int(c) # Physical limit constraint
        else:
            # LO-Task: Runs only in LO-Mode
            t_hi = 0 # Not applicable
            
        # Deadlines = Periods (Implicit)
        d_lo = t_lo
        d_hi = t_hi if is_hi else 0
            
        task_dict = {
            'id': i,
            'c': float(c),         # C is constant!
            't_lo': int(t_lo),
            't_hi': int(t_hi),
            'd_lo': int(d_lo),
            'd_hi': int(d_hi),
            'criticality': criticality
        }
        tasks_data.append(task_dict)
        
    return tasks_data

def main():
    # [Parameters]
    num_tasks = 20
    hi_prob = 0.5
    
    # [KEY PARAMETER] Rate Factor
    # If 4.0, tasks run 4x faster in emergency.
    # This creates HUGE pressure on Standard AMC (which must verify against T_HI).
    rate_factor = 4.0 
    
    num_sets_per_point = 200
    
    # Generate utilization points
    # Note: HI-mode utilization grows by rate_factor.
    # If U_LO = 0.25 and all tasks are HI with rate_factor=4, U_HI = 1.0.
    # So we stop generation around U_LO = 0.4 because U_HI will exceed 1.0
    util_points = [round(u/20.0, 2) for u in range(1, 10)] 
    
    all_data = {
        'parameters': {
            'num_tasks': num_tasks,
            'hi_prob': hi_prob,
            'rate_factor': rate_factor,
            'note': "Dual-Rate System (Constant C, Varying T)"
        },
        'util_points': util_points,
        'tasksets': {} 
    }
    
    print(f"Generating Dual-Rate Tasksets (Rate Factor={rate_factor}x)...")
    
    for u in util_points:
        print(f"Generating U_LO={u}...", end='\r')
        tasksets_for_u = []
        for _ in range(num_sets_per_point):
            while True:
                ts_data = generate_taskset_dual_rate(num_tasks, u, hi_prob, rate_factor)
                
                # [Feasibility Check]
                # 1. LO-Mode Utilization <= 1.0
                u_lo_total = sum([t['c']/t['t_lo'] for t in ts_data])
                
                # 2. HI-Mode Utilization <= 1.0 (Only HI tasks run)
                u_hi_total = sum([t['c']/t['t_hi'] for t in ts_data if t['criticality']=='HI'])
                
                if u_lo_total <= 1.0 and u_hi_total <= 1.0: 
                    tasksets_for_u.append(ts_data)
                    break
        all_data['tasksets'][str(u)] = tasksets_for_u
        
    filename = DATAFILE
    with open(filename, 'w') as f:
        json.dump(all_data, f, indent=2)
    print(f"\nDone! Saved to {filename}")

if __name__ == "__main__":
    main()