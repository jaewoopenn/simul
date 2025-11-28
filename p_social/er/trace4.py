import random
import math

# ==========================================
# 1. Configuration: HIC vs MIC vs LIC
# ==========================================
# Refined based on WHO Workforce Statistics (2023), Obermeyer et al. (2015),
# and Clinical Guidelines (AHA Stroke, Sepsis Campaign, NHS 4-Hour Rule).
PREFIX='/users/jaewoo/data/patient/'

CONFIG = {
    # Case 1: High-Income Country (HIC)
    'HIC': {
        'NUM_DOCTORS': 10,
        'ARRIVAL_RATE': 0.4,            
        'HI_RATIO': 0.20,               
        'SERVICE_TIME_MEAN': 25,        
        'SERVICE_TIME_STD': 5,          
        'BURST_PROB': 0.0,              
        'BURST_RANGE': (0, 0),
        # Deadlines (Minutes)
        'DEADLINE_HI_MEAN': 60,         # Golden Hour / Door-to-Needle
        'DEADLINE_HI_STD': 10,          # Strict adherence
        'DEADLINE_LO_MEAN': 240,        # 4-Hour Target (Standard)
        'DEADLINE_LO_STD': 30           
    },

    # Case 2: Middle-Income Country (MIC)
    'MIC': {
        'NUM_DOCTORS': 6,
        'ARRIVAL_RATE': 0.4,
        'HI_RATIO': 0.35,               
        'SERVICE_TIME_MEAN': 35,        
        'SERVICE_TIME_STD': 12,         
        'BURST_PROB': 0.1,              
        'BURST_RANGE': (2, 3),
        # Deadlines
        'DEADLINE_HI_MEAN': 60,         # Biology implies same urgency
        'DEADLINE_HI_STD': 15,          # Unclear diagnosis delay
        'DEADLINE_LO_MEAN': 240,        # Same target, harder to meet
        'DEADLINE_LO_STD': 45           
    },

    # Case 3: Low-Income Country (LIC)
    'LIC': {
        'NUM_DOCTORS': 3,
        'ARRIVAL_RATE': 0.4,
        'HI_RATIO': 0.50,               
        'SERVICE_TIME_MEAN': 45,        
        'SERVICE_TIME_STD': 20,         
        'BURST_PROB': 0.2,              
        'BURST_RANGE': (3, 6),
        # Deadlines
        'DEADLINE_HI_MEAN': 60,         # Critical physiology unchanged
        'DEADLINE_HI_STD': 20,          # High variance in presentation
        'DEADLINE_LO_MEAN': 300,        # Extended wait expectations (5 hrs)
        'DEADLINE_LO_STD': 60           
    }
}

SIMULATION_DURATION = 1440  # 1 Day = 1440 minutes

def generate_gaussian_value(mean, std, min_val):
    """Generate value using Gaussian distribution, ensuring min value."""
    val = random.gauss(mean, std)
    return max(min_val, val)

def generate_trace_file(scenario_name, filename):
    params = CONFIG[scenario_name]
    
    current_time = 0.0
    patient_id = 1
    
    print(f"Generating trace for {scenario_name} -> {filename} ...")
    
    with open(filename, 'w', encoding='utf-8') as f:
        # File Format: ID Criticality ArrivalTime Deadline ServiceTime
        
        while True:
            # 1. Inter-arrival time (Exponential Distribution)
            inter_arrival = random.expovariate(params['ARRIVAL_RATE'])
            current_time += inter_arrival
            
            if current_time > SIMULATION_DURATION:
                break
            
            # 2. Burst Logic
            batch_size = 1
            if random.random() < params['BURST_PROB']:
                low, high = params['BURST_RANGE']
                batch_size = random.randint(low, high)
            
            # 3. Generate Patient(s)
            for _ in range(batch_size):
                # Criticality
                is_hi = random.random() < params['HI_RATIO']
                criticality = "HI" if is_hi else "LO"
                
                # Service Time
                service_time = generate_gaussian_value(
                    params['SERVICE_TIME_MEAN'], 
                    params['SERVICE_TIME_STD'], 
                    min_val=5.0
                )
                
                # Deadline Generation based on Criticality
                if criticality == "HI":
                    deadline_duration = generate_gaussian_value(
                        params['DEADLINE_HI_MEAN'],
                        params['DEADLINE_HI_STD'],
                        min_val=30.0 # Minimum 30 min for critical
                    )
                else:
                    deadline_duration = generate_gaussian_value(
                        params['DEADLINE_LO_MEAN'],
                        params['DEADLINE_LO_STD'],
                        min_val=60.0 # Minimum 1 hour for non-critical
                    )+service_time
                
                # Absolute Deadline = Arrival + Duration
                # Note: The file stores the 'Relative Deadline' (Duration) or 'Absolute'?
                # The prompt says "Deadline", usually in scheduling traces this is relative (D_i) 
                # or absolute (d_i). Let's write the Duration (Relative Deadline) as requested 
                # implicitly by "Deadline depending on patient characteristics".
                # If you need Absolute, change to: current_time + deadline_duration
                
                line = f"{patient_id} {criticality} {int(current_time)} {int(deadline_duration)} {int(service_time)}\n"
                f.write(line)
                
                patient_id += 1
                
    print(f"Done. Total patients: {patient_id - 1}")
if __name__ == "__main__":
    generate_trace_file('HIC', PREFIX+'trace_hic.txt')
    generate_trace_file('MIC', PREFIX+'trace_mic.txt')
    generate_trace_file('LIC', PREFIX+'trace_lic.txt')
