import pandas as pd
import matplotlib.pyplot as plt

# --- Configuration ---
PATH = '/users/jaewoo/data/ev/peak/'
CSV_FILE_NAME = PATH + 'ev_jobs.csv'
SAVE_FILE_NAME = PATH + 'demand_supply_plot.png'

MAX_RATE = 5
GRID_CAPACITY = 19

def run_simulation(df, max_rate, grid_capacity):

    max_time = df['Departure'].max()
    demand_per_deadline = {} 
    
    cumulative_supply_points = []
    final_cumulative_demand = []
    
    # State variables
    old_sup = 0
    running_demand_total = 0.0

    print("=== Simulation Running ===")
    
    for current_time in range(max_time + 1):
        
        # A. Demand Calculation (Update based on arrivals)
        arriving_evs = df[df['Arrival'] == current_time]
        
        if not arriving_evs.empty:
            for _, row in arriving_evs.iterrows():
                deadline = int(row['Departure'])
                energy = row['Energy']
                
                if deadline in demand_per_deadline:
                    demand_per_deadline[deadline] += energy
                else:
                    demand_per_deadline[deadline] = energy
            
            # Create cumulative demand vector (sorted by deadline)
            sorted_deadlines = sorted(demand_per_deadline.keys())
            cumulative_demand = []
            running_demand_total = 0.0
            
            for d in sorted_deadlines:
                running_demand_total += demand_per_deadline[d]
                cumulative_demand.append((d, running_demand_total))
            
            final_cumulative_demand = cumulative_demand

        # B. Supply Calculation
        if current_time == 0:
            continue
            
        known_evs = df[df['Arrival'] <= current_time]
        known_evs = known_evs[known_evs['Departure'] >= current_time]
        
        active_count = len(known_evs)
        hourly_supply = active_count * max_rate 
        
        if hourly_supply > grid_capacity:
            hourly_supply = grid_capacity
        
        cur_sup = old_sup + hourly_supply
        cumulative_supply_points.append((current_time, cur_sup))
        old_sup = cur_sup

    print("=== Simulation End ===")
    
    return final_cumulative_demand, cumulative_supply_points

def plot_results(demand_data, supply_data, save_file_name):
    """
    시뮬레이션 결과를 받아 그래프를 그리고 저장합니다.
    """
    plt.figure(figsize=(10, 6))
    
    # Plot Demand
    if demand_data:
        dem_x, dem_y = zip(*demand_data)
        plt.plot(dem_x, dem_y, marker='o', label='Cumulative Demand (Energy Required)', color='red')
    
    # Plot Supply
    if supply_data:
        sup_x, sup_y = zip(*supply_data)
        plt.plot(sup_x, sup_y, marker='s', label='Cumulative Supply (Energy Provided)', color='blue')
        
    plt.title('Cumulative Demand vs Supply Over Time')
    plt.xlabel('Time / Deadline')
    plt.ylabel('Energy (kWh)')
    plt.grid(True)
    plt.legend()
    
    # Save plot
    plt.savefig(save_file_name)
    print(f"Plot saved to {save_file_name}")

# --- Main Execution ---
if __name__ == "__main__":
    # 1. 시뮬레이션 실행
    """
    데이터를 로드하고 시뮬레이션을 수행하여 수요(Demand)와 공급(Supply) 데이터를 반환합니다.
    """
    # 1. Load Data
    try:
        df = pd.read_csv(CSV_FILE_NAME)
    except FileNotFoundError:
        print("File not found.")
        exit
    demand_vec, supply_vec = run_simulation(df, MAX_RATE, GRID_CAPACITY)
    
    # 2. 결과가 정상적으로 나왔을 경우 그래프 그리기
    if demand_vec is not None:
        plot_results(demand_vec, supply_vec, SAVE_FILE_NAME)
        
        # 3. 결과 텍스트 출력 (옵션)
        print("\nFinal Cumulative Demand Vector:")
        print(demand_vec)
        print("\nFinal Cumulative Supply Vector (Points):")
        print(supply_vec)