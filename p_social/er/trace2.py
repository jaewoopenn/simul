import random
import math

# ==========================================
# 1. Configuration: HIC vs MIC vs LIC
# ==========================================
# Common Assumption: Total Daily Arrival is similar (~576 patients/day if lambda=0.4),
# but the pattern (Burst), Severity, and Service Capability differ.

PREFIX='/users/jaewoo/data/patient/'

CONFIG = {
    # Case 1: High-Income Country (OECD Average)
    # - Resources: Ample (10 Doctors)
    # - Pattern: Smooth (Poisson), standardized care
    'HIC': {
        'NUM_DOCTORS': 10,
        'ARRIVAL_RATE': 0.4,            
        'HI_RATIO': 0.20,               # Early access -> Low severity
        'SERVICE_TIME_MEAN': 25,        # Fast, Standardized
        'SERVICE_TIME_STD': 5,          # Low variance
        'BURST_PROB': 0.0,              # No burst (Smooth)
        'BURST_RANGE': (0, 0)
    },

    # Case 2: Middle-Income Country (Transitional)
    # - Resources: Moderate (7 Doctors)
    # - Pattern: Occasional minor surges (Traffic/EMS coordination issues)
    'MIC': {
        'NUM_DOCTORS': 7,
        'ARRIVAL_RATE': 0.4,
        'HI_RATIO': 0.35,               # Delayed access -> Moderate severity
        'SERVICE_TIME_MEAN': 32,        # Moderate delays
        'SERVICE_TIME_STD': 10,         
        'BURST_PROB': 0.1,              # 10% chance of small burst
        'BURST_RANGE': (2, 3)           # 2-3 patients arrive at once
    },

    # Case 3: Low-Income Country (Resource Constrained)
    # - Resources: Scarce (4 Doctors)
    # - Pattern: Highly irregular (Mass transport/Late referrals)
    'LIC': {
        'NUM_DOCTORS': 4,
        'ARRIVAL_RATE': 0.4,
        'HI_RATIO': 0.50,               # Very late presentation -> High severity
        'SERVICE_TIME_MEAN': 40,        # Slow (Equipment shortage)
        'SERVICE_TIME_STD': 15,         # High variance
        'BURST_PROB': 0.2,              # 20% chance of large burst
        'BURST_RANGE': (3, 6)           # 3-6 patients arrive at once
    }
}

SIMULATION_DURATION = 1440  # 1 Day = 1440 minutes

def generate_service_time(mean, std):
    """Generate service time using Gaussian distribution, ensuring min time."""
    val = random.gauss(mean, std)
    return max(5.0, val)  # Minimum procedure time 5 mins

def generate_trace_file(scenario_name, filename):
    params = CONFIG[scenario_name]
    
    current_time = 0.0
    patient_id = 1
    
    print(f"Generating trace for {scenario_name} -> {filename} ...")
    
    with open(filename, 'w', encoding='utf-8') as f:
        # Header (Optional)
        # f.write("# ID Criticality ArrivalTime ServiceTime\n")
        
        while True:
            # 1. Determine next arrival time (Inter-arrival time follows Exponential Dist)
            inter_arrival = random.expovariate(params['ARRIVAL_RATE'])
            current_time += inter_arrival
            
            if current_time > SIMULATION_DURATION:
                break
            
            # 2. Determine Batch Size (Bursty Logic)
            batch_size = 1
            if random.random() < params['BURST_PROB']:
                # Burst event occurs
                low, high = params['BURST_RANGE']
                batch_size = random.randint(low, high)
            
            # 3. Generate Patient(s) for this time slot
            for _ in range(batch_size):
                # Criticality Determination
                is_hi = random.random() < params['HI_RATIO']
                criticality = "HI" if is_hi else "LO"
                deadline="60" if is_hi else "240"
                
                # Service Time Determination
                service_time = generate_service_time(params['SERVICE_TIME_MEAN'], 
                                                   params['SERVICE_TIME_STD'])
                
                # Format: ID criticality(HI/LO) ArrivalTime ServiceTime
                line = f"{patient_id} {criticality} {int(current_time)} {deadline} {int(service_time)}\n"
                f.write(line)
                
                patient_id += 1
                
    print(f"Done. Total patients: {patient_id - 1}")

# ==========================================
# 2. Execution
# ==========================================
if __name__ == "__main__":
    generate_trace_file('HIC', PREFIX+'trace_hic.txt')
    generate_trace_file('MIC', PREFIX+'trace_mic.txt')
    generate_trace_file('LIC', PREFIX+'trace_lic.txt')