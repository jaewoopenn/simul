import random
import math

# ==========================================
# 1. Configuration: HIC vs MIC vs LIC
# ==========================================
# Refined based on WHO Workforce Statistics (2023) and Obermeyer et al. (2015)
PREFIX='/users/jaewoo/data/patient/'


CONFIG = {
    # Case 1: High-Income Country (HIC)
    # - Reference: OECD Average ~3.7 doctors/1000 pop
    # - Setting: Baseline (10 Doctors)
    'HIC': {
        'NUM_DOCTORS': 10,
        'ARRIVAL_RATE': 0.4,            # ~24 patients/hour
        'HI_RATIO': 0.20,               # ESI Level 1-2 approx 20%
        'SERVICE_TIME_MEAN': 25,        # Efficient workflow
        'SERVICE_TIME_STD': 5,          # Low variance (Standardized)
        'BURST_PROB': 0.0,              # Poisson Arrival
        'BURST_RANGE': (0, 0)
    },

    # Case 2: Middle-Income Country (MIC)
    # - Reference: Global average varies, ~1.5-2.0 doctors/1000 pop
    # - Setting: 60% of Baseline (6 Doctors)
    'MIC': {
        'NUM_DOCTORS': 6,
        'ARRIVAL_RATE': 0.4,
        'HI_RATIO': 0.35,               # Delayed access effect
        'SERVICE_TIME_MEAN': 35,        # Moderate delays
        'SERVICE_TIME_STD': 12,         # Moderate variance
        'BURST_PROB': 0.1,              # 10% chance of small burst
        'BURST_RANGE': (2, 3)
    },

    # Case 3: Low-Income Country (LIC)
    # - Reference: Sub-Saharan Africa <0.2 doctors/1000 pop (Extreme shortage)
    # - Setting: 30% of Baseline (3 Doctors) - Scaled for simulation viability
    'LIC': {
        'NUM_DOCTORS': 3,
        'ARRIVAL_RATE': 0.4,
        'HI_RATIO': 0.50,               # Late Presentation is dominant
        'SERVICE_TIME_MEAN': 45,        # Resource waiting time added
        'SERVICE_TIME_STD': 20,         # High variance (Unpredictable)
        'BURST_PROB': 0.2,              # 20% chance of burst
        'BURST_RANGE': (3, 6)           # Mass casualty / Shared transport
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
        # File Format: ID Criticality ArrivalTime ServiceTime
        # Example: 1 LO 2.50 24.10
        
        while True:
            # 1. Inter-arrival time (Exponential Distribution)
            inter_arrival = random.expovariate(params['ARRIVAL_RATE'])
            current_time += inter_arrival
            
            if current_time > SIMULATION_DURATION:
                break
            
            # 2. Burst Logic (Batch Arrival)
            batch_size = 1
            if random.random() < params['BURST_PROB']:
                low, high = params['BURST_RANGE']
                batch_size = random.randint(low, high)
            
            # 3. Generate Patient(s)
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

if __name__ == "__main__":
    generate_trace_file('HIC', PREFIX+'trace_hic.txt')
    generate_trace_file('MIC', PREFIX+'trace_mic.txt')
    generate_trace_file('LIC', PREFIX+'trace_lic.txt')