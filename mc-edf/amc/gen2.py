import random
import math
import json

PATH='/users/jaewoo/data/amc'
DATAFILE=PATH+'/data/task_data.json'

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

def generate_taskset_mode_dependent_v2(num_tasks, utilization, hi_prob=0.5, hi_factor=2.0, tight_factor=2.0):
    tasks_data = []
    utils = uunifast(num_tasks, utilization)
    
    # [TWEAK 1] Increase min_period to reduce quantization error (c_lo=1 effect)
    min_period = 100
    max_period = 1000
    
    for i in range(num_tasks):
        period = math.exp(random.uniform(math.log(min_period), math.log(max_period)))
        period = round(period)
        
        c_lo = utils[i] * period
        if c_lo < 1: c_lo = 1
        
        is_hi = random.random() < hi_prob
        criticality = 'HI' if is_hi else 'LO'
        
        c_hi = c_lo
        if is_hi:
            c_hi = c_lo * hi_factor
        else:
            c_hi = 0
            
        # [MODIFIED LOGIC]
        if is_hi:
            # HI-Task: Loose in LO-mode (D=T), Tight in HI-mode (D=T/tight_factor)
            d_lo = period 
            d_hi = int(period / tight_factor) 
            
            # Sanity check: D_HI >= C_HI
            if d_hi < c_hi: d_hi = int(c_hi)
            if d_lo < d_hi: d_lo = d_hi
            
        else:
            # LO-Task: Urgent!
            # [TWEAK 2] Relax constraints slightly to make tasksets schedulable at low U
            # D between C and 0.8*T (still looser than HI-task's D_HI=0.5T)
            # So AMC will prioritize HI-task (D=0.5T) over LO-task (D=0.8T) -> LO task dies.
            # VP-AMC will prioritize LO-task (D=0.8T) over HI-task (D=T) -> Everyone lives.
            upper_bound = max(c_lo, period * 0.8)
            d_lo = random.randint(int(c_lo), int(upper_bound))
            d_hi = 0 
            
        task_dict = {
            'id': i,
            'c_lo': float(c_lo),
            'c_hi': float(c_hi),
            'd_lo': int(d_lo),
            'd_hi': int(d_hi),
            't': int(period),
            'criticality': criticality
        }
        tasks_data.append(task_dict)
        
    return tasks_data

def main():
    # [TWEAK 3] Parameters optimized for demonstration
    num_tasks = 20
    hi_prob = 0.5
    hi_factor = 2.0 
    tight_factor = 2.0  # HI-mode deadline is 1/2 of Period (Still very urgent compared to LO-mode)
    num_sets_per_point = 100
    
    util_points = [round(u/20.0, 2) for u in range(1, 20)] 
    
    all_data = {
        'parameters': {
            'num_tasks': num_tasks,
            'hi_prob': hi_prob,
            'hi_factor': hi_factor,
            'deadline_tightness': tight_factor,
            'note': "V2.1: Adjusted parameters to fix low-U schedulability"
        },
        'util_points': util_points,
        'tasksets': {} 
    }
    
    print(f"Generating Tasksets V2.1 (Refined Sweet Spot)...")
    
    for u in util_points:
        print(f"Generating U={u}...", end='\r')
        tasksets_for_u = []
        for _ in range(num_sets_per_point):
            while True:
                ts_data = generate_taskset_mode_dependent_v2(num_tasks, u, hi_prob, hi_factor, tight_factor)
                
                # Check feasibility
                feasible = True
                for t in ts_data:
                    if t['criticality'] == 'HI' and t['c_hi'] > t['d_hi']: feasible = False
                    if t['c_lo'] > t['d_lo']: feasible = False
                
                u_lo = sum([t['c_lo']/t['t'] for t in ts_data])
                
                if u_lo <= 1.0 and feasible: 
                    tasksets_for_u.append(ts_data)
                    break
        all_data['tasksets'][str(u)] = tasksets_for_u
        
    filename = DATAFILE
    with open(filename, 'w') as f:
        json.dump(all_data, f, indent=2)
    print(f"\nDone! Saved to {filename}")

if __name__ == "__main__":
    main()