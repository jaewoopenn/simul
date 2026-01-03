import pandas as pd
import numpy as np

MAX_RATE = 6.6
GRID_CAPACITY = 30

def simulate_ev_demand_supply(file_path):
    # 데이터 로드
    try:
        df = pd.read_csv(file_path)
    except FileNotFoundError:
        print("파일을 찾을 수 없습니다.")
        return

    max_arrival_time = df['Arrival'].max()
    demand_per_deadline = {}  # {Deadline: Energy (개별)}

    print("=== EV Demand & Supply Vector Simulation ===")
    cumulative_supply = []
    old_time=0
    old_sup=0
    for current_time in range(max_arrival_time + 1):
        
        arriving_evs = df[df['Arrival'] == current_time]
        
        if not arriving_evs.empty:
            # 1. Update Demand (Cumulative Arrivals)
            for _, row in arriving_evs.iterrows():
                deadline = int(row['Departure'])
                energy = row['Energy']
                
                if deadline in demand_per_deadline:
                    demand_per_deadline[deadline] += energy
                else:
                    demand_per_deadline[deadline] = energy
            
            # --- Vector 준비 ---
            sorted_deadlines = sorted(demand_per_deadline.keys())
            
            # A. Calculate Demand Vector (누적)
            cumulative_demand = []
            running_demand_total = 0.0
            for d in sorted_deadlines:
                running_demand_total += demand_per_deadline[d]
                cumulative_demand.append((d, running_demand_total))
            
            # B. Calculate Supply Vector (현재 시점부터 Deadline까지 공급 가능량)
            
            known_evs = df[df['Arrival'] <= current_time ]  
            known_evs = known_evs[known_evs['Departure']> current_time ]  
            active_count=len(known_evs)
            hourly_supply = active_count * MAX_RATE
            if hourly_supply > GRID_CAPACITY:
                hourly_supply = GRID_CAPACITY
            print(f"{hourly_supply}, {current_time-old_time}")
            cur_sup=old_sup+hourly_supply*(current_time-old_time)
            cumulative_supply.append((current_time, cur_sup))
            
            # --- 출력 ---
            print(f"\n[Time: {current_time}] 새로운 EV 도착")
            
            print("  -> Demand Vector (Deadline, Cumulative Energy):")
            print("[")
            for d, e in cumulative_demand:
                print(f"  ({d}, {float(e):.2f}),")
            print("]")
            
            print("  -> Supply Vector (Deadline, Cumulative Supply):")
            print("[")
            for d, s in cumulative_supply:
                print(f"  ({d}, {float(s):.2f}),")
            print("]")
            old_time=current_time
            old_sup=cur_sup
# 코드 실행

    print("\n=== Simulation End ===")

# 코드 실행 (로컬 파일 경로 예시)
simulate_ev_demand_supply('/users/jaewoo/data/ev/spc/ev_jobs.csv')
