import random
import math

# ==========================================
# 1. Configuration: Rambam Actual vs LMIC (30 Days)
# ==========================================
CONFIG = {
    # [Reference Baseline] HIC - Based on Rambam Hospital (Armony et al., 2015)
    # - Daily Volume: ~245 patients (Time-Dependent)
    # - Beds: 40
    # - ALOS: 4.5 hours
    'HIC_REF': {
        'LOS_MEAN': 270,                # 4.5 hours
        'LOS_STD': 60,
        'HI_RATIO': 0.25,
        'TARGET_WAIT_HI': 60,
        'TARGET_WAIT_LO': 240,
        'BURST_PROB': 0.0,
        'BURST_RANGE': (0, 0)
    },

    # [Target Scenario] LMIC - Resource Constrained
    # - Volume: Same demand pattern
    # - Capacity: 20 beds (50% of HIC)
    'LMIC': {
        'HI_RATIO': 0.45,               # High severity
        'LOS_MEAN': 380,                # ~6.3 hours
        'LOS_STD': 100,
        'TARGET_WAIT_HI': 60,
        'TARGET_WAIT_LO': 240,
        'BURST_PROB': 0.15,
        'BURST_RANGE': (2, 4)
    }
}

# Simulation Duration: 30 Days in Minutes
# 1440 min/day * 30 days = 43200 minutes
SIMULATION_DURATION = 1440 * 30

def get_arrival_rate_rambam(minute_of_simulation):
    """
    Returns arrival rate (patients/min) based on time of day.
    Repeats daily cycle every 1440 minutes.
    """
    # Convert simulation time (minute from start) to minute of day (0-1439)
    minute_of_day = minute_of_simulation % 1440
    hour = (minute_of_day / 60.0)
    
    if 0 <= hour < 6:
        return 0.04  # Night low
    elif 6 <= hour < 10:
        # Linear increase from 0.04 to 0.20
        slope = (0.20 - 0.04) / 4
        return 0.04 + slope * (hour - 6)
    elif 10 <= hour < 14:
        return 0.20  # Morning Peak
    elif 14 <= hour < 20:
        return 0.17  # Afternoon High
    else: # 20:00 - 24:00
        # Linear decrease from 0.17 to 0.04
        slope = (0.17 - 0.04) / 4
        return 0.17 - slope * (hour - 20)

def generate_gaussian_value(mean, std, min_val):
    val = random.gauss(mean, std)
    return max(min_val, val)

def generate_trace_file(scenario_name, filename):
    params = CONFIG[scenario_name]
    current_time = 0.0
    patient_id = 1
    
    print(f"Generating trace for {scenario_name} -> {filename} ...")
    
    with open(filename, 'w', encoding='utf-8') as f:
        while current_time < SIMULATION_DURATION:
            # 1. Determine Arrival Rate for current time
            current_rate = get_arrival_rate_rambam(current_time)
            
            if current_rate <= 0.001: current_rate = 0.001
            
            # Generate next inter-arrival time
            inter_arrival = random.expovariate(current_rate)
            current_time += inter_arrival
            
            if current_time > SIMULATION_DURATION:
                break
            
            # 2. Burst Logic
            batch_size = 1
            if random.random() < params['BURST_PROB']:
                low, high = params['BURST_RANGE']
                batch_size = random.randint(low, high)
            
            for _ in range(batch_size):
                # Criticality
                is_hi = random.random() < params['HI_RATIO']
                criticality = "HI" if is_hi else "LO"
                
                # Service Time (LOS)
                los = generate_gaussian_value(
                    params['LOS_MEAN'], 
                    params['LOS_STD'], 
                    min_val=20.0
                )
                
                # Deadline = Target Start Time + LOS
                if criticality == "HI":
                    target_wait = params['TARGET_WAIT_HI']
                else:
                    target_wait = params['TARGET_WAIT_LO']
                
                deadline_relative = target_wait + los
                
                # Output: ID Criticality ArrivalTime Deadline ServiceTime
                line = f"{patient_id} {criticality} {int(current_time)} {int(deadline_relative)} {int(los)}\n"
                f.write(line)
                
                patient_id += 1
                
    print(f"Done. Total patients: {patient_id - 1}")

PREFIX='/users/jaewoo/data/patient/'

if __name__ == "__main__":
    # generate_trace_file('HIC_REF', 'trace_hic.txt')
    generate_trace_file('LMIC', PREFIX+'trace_lmic.txt')
               

