import pandas as pd
import matplotlib.pyplot as plt

SAVE_FILE_NAME='/users/jaewoo/data/ev/spc/demand_supply_plot.png'

MAX_RATE = 6.6
GRID_CAPACITY = 29
CSV_FILE_NAME='/users/jaewoo/data/ev/spc/ev_jobs.csv'

# MAX_RATE = 5
# GRID_CAPACITY = 10
# CSV_FILE_NAME='/users/jaewoo/data/ev/spc/ev_jobs4.csv'

def simulate_and_plot(file_path):
    # Load data
    try:
        df = pd.read_csv(file_path)
    except FileNotFoundError:
        print("File not found.")
        return

    max_time = df['Departure'].max()
    demand_per_deadline = {} 

    cumulative_supply_points = []
    
    # State variables for user's logic
    old_sup = 0
    
    # To capture the final demand vector
    final_cumulative_demand = []

    print("=== Simulation Running ===")
    old_demand=0
    running_demand_total = 0.0
    for current_time in range(max_time + 1):
        
        arriving_evs = df[df['Arrival'] == current_time]
        
        if not arriving_evs.empty:
            # 1. Update Demand
            for _, row in arriving_evs.iterrows():
                deadline = int(row['Departure'])
                energy = row['Energy']
                
                if deadline in demand_per_deadline:
                    demand_per_deadline[deadline] += energy
                else:
                    demand_per_deadline[deadline] = energy
            
            # Prepare Demand Vector for this step (we will use the last one for plotting)
            sorted_deadlines = sorted(demand_per_deadline.keys())
            
            cumulative_demand = []
            running_demand_total = 0.0
            for d in sorted_deadlines:
                running_demand_total += demand_per_deadline[d]
                cumulative_demand.append((d, running_demand_total))
            
            
            final_cumulative_demand = cumulative_demand # Update the reference to the latest

            
        # 2. Calculate Supply (User's Logic)
        known_evs = df[df['Arrival'] <= current_time]
        known_evs = known_evs[known_evs['Departure'] >= current_time]
        
        active_count = len(known_evs)
        hourly_supply = active_count * MAX_RATE
        if hourly_supply > GRID_CAPACITY:
            hourly_supply = GRID_CAPACITY
        
        # Calculate accumulated supply since old_time
        cur_sup = min(old_sup + hourly_supply,old_demand) 
        cumulative_supply_points.append((current_time, cur_sup))
        old_sup=cur_sup
        old_demand=running_demand_total
    print("=== Simulation End ===")
    
    # --- Plotting ---
    plt.figure(figsize=(10, 6))
    
    # Extract data for plotting
    if final_cumulative_demand:
        dem_x, dem_y = zip(*final_cumulative_demand)
        plt.plot(dem_x, dem_y, marker='o', label='Cumulative Demand (Energy Required)', color='red')
    
    if cumulative_supply_points:
        sup_x, sup_y = zip(*cumulative_supply_points)
        plt.plot(sup_x, sup_y, marker='s', label='Cumulative Supply (Energy Provided)', color='blue')
        
    plt.title('Cumulative Demand vs Supply Over Time')
    plt.xlabel('Time / Deadline')
    plt.ylabel('Energy (kWh)')
    plt.grid(True)
    plt.legend()
    
    # Save plot
    plt.savefig(SAVE_FILE_NAME)
    
    # Print the final vectors as text as well (optional, but good for confirmation)
    print("\nFinal Cumulative Demand Vector:")
    print(final_cumulative_demand)
    print("\nFinal Cumulative Supply Vector (Points):")
    print(cumulative_supply_points)

simulate_and_plot(CSV_FILE_NAME)