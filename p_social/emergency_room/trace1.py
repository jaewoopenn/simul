'''
Created on 2025. 11. 26.

@author: jaewoo
'''
import random
import math

# ==========================================
# 1. Configuration (Defined previously)
# ==========================================
CONFIG = {
    'HIC': {
        'ARRIVAL_RATE': 0.4,            # ~24 patients/hour
        'HI_RATIO': 0.20,               # 20% Severe
        'SERVICE_TIME_MEAN': 25,
        'SERVICE_TIME_STD': 5,
        'IS_BURSTY': False              # Smooth Poisson
    },
    'LMIC': {
        'ARRIVAL_RATE': 0.4,            # Same average volume
        'HI_RATIO': 0.40,               # 40% Severe (Late presentation)
        'SERVICE_TIME_MEAN': 35,        # Slower
        'SERVICE_TIME_STD': 15,         # High variance
        'IS_BURSTY': True               # Batch/Burst arrivals
    }
}

SIMULATION_DURATION = 1440  # 1 Day = 1440 minutes
PREFIX='/users/jaewoo/data/patient/'

def generate_service_time(mean, std):
    """Generate service time using Gaussian distribution, ensuring no negative values."""
    val = random.gauss(mean, std)
    return max(5.0, val)  # Minimum procedure time 5 mins

def generate_trace_file(scenario_name, filename):
    params = CONFIG[scenario_name]
    
    current_time = 0.0
    patient_id = 1
    
    print(f"Generating trace for {scenario_name} -> {filename} ...")
    
    with open(filename, 'w', encoding='utf-8') as f:
        # Header (Optional: Remove if strict raw format is needed)
        # f.write("# ID Criticality ArrivalTime ServiceTime\n")
        
        while True:
            # 1. Determine next arrival time (Inter-arrival time follows Exponential Dist)
            # lambda = rate, 1/lambda = mean interval
            inter_arrival = random.expovariate(params['ARRIVAL_RATE'])
            current_time += inter_arrival
            
            if current_time > SIMULATION_DURATION:
                break
            
            # 2. Determine Batch Size (Bursty Logic)
            # HIC: Always 1 patient at a time
            # LMIC: 80% chance 1 patient, 20% chance of "Burst" (3-6 patients at once)
            batch_size = 1
            if params['IS_BURSTY']:
                if random.random() < 0.2:  # 20% probability of a burst event
                    batch_size = random.randint(3, 6)
            
            # 3. Generate Patient(s) for this time slot
            for _ in range(batch_size):
                # Criticality Determination
                is_hi = random.random() < params['HI_RATIO']
                criticality = "HI" if is_hi else "LO"
                deadline="60" if is_hi else "240"
                # Service Time Determination
                service_time = generate_service_time(params['SERVICE_TIME_MEAN'], 
                                                   params['SERVICE_TIME_STD'])
                
                # Format: ID criticality(HI/LO) 도착시간(분) 처치시간
                # Using .2f for readability
                line = f"{patient_id} {criticality} {int(current_time)} {deadline} {int(service_time)}\n"
                f.write(line)
                
                patient_id += 1
                
    print(f"Done. Total patients: {patient_id - 1}")

# ==========================================
# 2. Execution
# ==========================================
if __name__ == "__main__":
    # Generate HIC Trace
    generate_trace_file('HIC', PREFIX+'trace_hic.txt')
    
    # Generate LMIC Trace
    generate_trace_file('LMIC', PREFIX+'trace_lmic.txt')