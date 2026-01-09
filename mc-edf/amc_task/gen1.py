import random
import math
import json
import os


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
    """Generates a random mixed-criticality task set with Constrained Deadlines (Integer)."""
    tasks_data = []
    utils = uunifast(num_tasks, utilization)
    
    # [FIX] Period range increased to mitigate integer rounding errors
    # Old: 10 ~ 1000 -> New: 500 ~ 5000 (effectively scaling up time units)
    min_period = 500
    max_period = 5000
    
    for i in range(num_tasks):
        # 1. Period Generation (Integer)
        period = int(math.exp(random.uniform(math.log(min_period), math.log(max_period))))
        
        # 2. C_LO Calculation (Integer)
        # Use round or floor/ceil. max(1, ...) ensures no zero execution time
        c_lo = int(round(utils[i] * period))
        if c_lo < 1: c_lo = 1
        
        # 3. Criticality & C_HI Calculation
        is_hi = random.random() < hi_prob
        criticality = 'HI' if is_hi else 'LO'
        
        if is_hi:
            c_hi = int(c_lo * hi_factor)
        else:
            c_hi = 0 # LO tasks technically don't have C_HI budget
            
        # 4. Validity Check (C > T Case)
        # If C_LO or C_HI exceeds T, this task is invalid. 
        # Instead of clamping (which distorts utilization), we return None to retry generation.
        worst_case_c = c_hi if is_hi else c_lo
        if worst_case_c > period:
            return None 

        # 5. Deadline Generation (Constrained: C_worst <= D <= T)
        min_deadline = worst_case_c
        
        # Randomly pick D in [C_worst, T]
        deadline = random.randint(min_deadline, period)
        
        # Store as dictionary
        task_dict = {
            'id': i,
            'c_lo': c_lo,       # Integer
            'c_hi': c_hi,       # Integer
            'd': deadline,      # Integer
            't': period,        # Integer
            'criticality': criticality,
            'u_target': utils[i] # Original target util (for debug)
        }
        tasks_data.append(task_dict)
        
    return tasks_data

def main():
    # [Parameters]
    num_tasks = 20          
    hi_prob = 0.5
    hi_factor = 4.0         
    num_sets_per_point = 100
    
    # Utilization points (0.05 to 0.95 step 0.05)
    util_points = [round(0.05 * i, 2) for i in range(1, 20)]
    
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
    print(f"Saving to: {os.path.abspath(DATAFILE)}")
    
    for u in util_points:
        print(f"Generating for Utilization {u}...", end=" ")
        tasksets_for_u = []
        count = 0
        
        while count < num_sets_per_point:
            ts_data = generate_taskset_data(num_tasks, u, hi_prob, hi_factor)
            
            # If generation failed (invalid C > T), try again
            if ts_data is None:
                continue
                
            # Double Check: Total LO Utilization <= 1.0
            # Although UUnifast targets U, rounding to Int might push it over.
            actual_u_lo = sum([t['c_lo']/t['t'] for t in ts_data])
            
            # [FIX] Tolerance Check
            # Ensure actual utilization is reasonably close to target u.
            # Example: Allow +/- 2.5% deviation or absolute 0.02
            if abs(actual_u_lo - u) > 0.025:
                continue

            if actual_u_lo <= 1.0: 
                tasksets_for_u.append(ts_data)
                count += 1
        
        print(f"Done ({count} sets)")
        all_data['tasksets'][str(u)] = tasksets_for_u
        
    with open(DATAFILE, 'w') as f:
        json.dump(all_data, f, indent=2)
        
    print(f"\nGeneration Complete! Data saved to '{DATAFILE}'")

if __name__ == "__main__":
    main()