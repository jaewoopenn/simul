import random

# ==========================================
# 1. Configuration: Rambam Actual vs LMIC
# ==========================================
PREFIX='/users/jaewoo/data/patient/'
CONFIG = {
    # [Reference Baseline] HIC - Based on Rambam Hospital (Armony et al., 2015)
    # - Daily Volume: ~245 patients (Lambda approx 0.17)
    # - Beds: 40 (Original capacity)
    # - ALOS: 4.5 hours (270 min) - Slightly increased as requested
    'HIC_REF': {
        'ARRIVAL_RATE': 0.17,           # ~245 patients/day
        'LOS_MEAN': 270,                # 4.5 hours
        'LOS_STD': 60,
        'HI_RATIO': 0.25,
        'TARGET_WAIT_HI': 60,
        'TARGET_WAIT_LO': 240,
        'BURST_PROB': 0.0,
        'BURST_RANGE': (0, 0)
    },

    # [Target Scenario] LMIC - Resource Constrained
    # - Volume: Same demand (Lambda=0.17)
    # - Capacity: 20 beds (50% of HIC)
    'LMIC': {
        'ARRIVAL_RATE': 0.17,           # Same demand
        'HI_RATIO': 0.45,               # High severity
        'LOS_MEAN': 380,                # ~6.3 hours
        'LOS_STD': 100,
        'TARGET_WAIT_HI': 60,
        'TARGET_WAIT_LO': 240,
        'BURST_PROB': 0.15,
        'BURST_RANGE': (2, 4)
    }
}

SIMULATION_DURATION = 1440*10  # 1 Day

def generate_gaussian_value(mean, std, min_val):
    val = random.gauss(mean, std)
    return max(min_val, val)

def generate_trace_file(scenario_name, filename):
    params = CONFIG[scenario_name]
    current_time = 0.0
    patient_id = 1
    
    print(f"Generating trace for {scenario_name} -> {filename} ...")
    
    with open(filename, 'w', encoding='utf-8') as f:
        while True:
            # Arrival Time
            inter_arrival = random.expovariate(params['ARRIVAL_RATE'])
            current_time += inter_arrival
            
            if current_time > SIMULATION_DURATION:
                break
            
            # Burst Logic
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

if __name__ == "__main__":
    # generate_trace_file('HIC_REF', 'trace_hic.txt')
    generate_trace_file('LMIC', PREFIX+'trace_lmic.txt')
