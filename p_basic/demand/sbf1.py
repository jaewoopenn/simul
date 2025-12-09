import matplotlib.pyplot as plt
import numpy as np
import csv

FILENAME = '/users/jaewoo/data/ev/spc/ev_jobs.csv'
EV_MAX_POWER = 6.6      # 차량당 최대 충전 속도 (kW)
TIME_STEP = 0.1         # 시뮬레이션 시간 단위 (시간)

# 공급 프로필 (Grid Capacity)
SUPPLY_PROFILE = [
    (10, 20),   
    (20, 30),   
    (None, 20)
]

class EV:
    def __init__(self, id, arrival, energy, departure):
        self.id = int(id)
        self.arrival = int(arrival)
        self.energy = float(energy)
        self.departure = int(departure)
        
        # 시뮬레이션용 상태 변수 (잔여 필요 충전량)
        self.remaining_energy = float(energy)

def load_evs_from_csv(filename):
    evs = []
    try:
        with open(filename, mode='r', encoding='utf-8') as f:
            reader = csv.DictReader(f)
            for row in reader:
                evs.append(EV(row['ID'], row['Arrival'], row['Energy'], row['Departure']))
        return evs
    except FileNotFoundError:
        print("파일이 없습니다.")
        return []

def get_grid_capacity(t, profile):
    for end_time, capacity in profile:
        if end_time is None or t < end_time:
            return capacity
    return profile[-1][1]

def calculate_simulation_sbf(time_steps, evs, profile):
    """
    Time-Step 시뮬레이션을 통해 SBF 및 실제 Active Count 계산
    Logic: EDF(Earliest Deadline First)로 에너지를 소모시킴
    """
    # 1. 상태 초기화 (모든 차의 잔여량 리셋)
    for ev in evs:
        ev.remaining_energy = ev.energy
        
    sbf_values = []
    active_counts_history = [] # 그래프용 (실제 충전중인 차 수)
    
    cumulative_supply = 0
    dt = TIME_STEP

    for t in time_steps:
        # A. 현재 물리적으로 주차장에 있는 차 찾기
        #    AND 아직 충전이 덜 된 차 (Remaining > 0)
        active_evs = [
            ev for ev in evs 
            if ev.arrival <= t < ev.departure and ev.remaining_energy > 1e-6
        ]
        
        # 기록용 카운트
        active_counts_history.append(len(active_evs))
        
        # B. 병목 계산 (Grid vs Vehicle Cap)
        grid_cap = get_grid_capacity(t, profile)
        vehicle_cap = len(active_evs) * EV_MAX_POWER
        
        # 실제 이 시간 구간(dt) 동안 공급되는 파워
        current_power = min(grid_cap, vehicle_cap)
        
        # C. SBF 누적 (적분)
        cumulative_supply += current_power * dt
        sbf_values.append(cumulative_supply)
        
        # D. 에너지 배분 (State Update) - EDF 정책 적용
        # 공급된 에너지(Energy Batch)를 급한 차부터 채워넣음
        available_energy = current_power * dt
        
        # Deadline 빠른 순 정렬
        active_evs.sort(key=lambda x: x.departure)
        
        for ev in active_evs:
            if available_energy <= 0:
                break
            
            # 이 차가 dt동안 받을 수 있는 최대량 (물리적 한계 vs 필요량)
            # 물리적 한계: 6.6kW * dt
            max_intake = EV_MAX_POWER * dt
            needed = ev.remaining_energy
            
            # 실제로 충전할 양
            charge_amount = min(max_intake, needed, available_energy)
            
            # 상태 업데이트
            ev.remaining_energy -= charge_amount
            available_energy -= charge_amount

    return sbf_values, active_counts_history

def calculate_dbf(t, evs):
    demand = 0
    for ev in evs:
        if ev.departure <= t:
            demand += ev.energy
    return demand

def main():
    evs = load_evs_from_csv(FILENAME)
    if not evs: return

    # 시간 축 설정
    max_departure = max(ev.departure for ev in evs)
    time_limit = max(25, max_departure + 2)
    time_steps = np.arange(0, time_limit + TIME_STEP, TIME_STEP)

    # --- 시뮬레이션 실행 (SBF 계산) ---
    sbf_values, active_counts = calculate_simulation_sbf(time_steps, evs, SUPPLY_PROFILE)
    
    # DBF 계산
    dbf_values = [calculate_dbf(t, evs) for t in time_steps]

    # --- 그래프 그리기 ---
    fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(10, 12), sharex=True)

    # [상단] Active EV Count 변화 (물리적 주차 vs 실제 충전 필요)
    ax1.set_title("Active EV Count (Simulated)")
    ax1.set_ylabel("Count")
    ax1.grid(True, linestyle='--', alpha=0.5)
    
    # 단순히 주차장에 있는 차 수 (비교용)
    physically_present = [
        sum(1 for ev in evs if ev.arrival <= t < ev.departure) 
        for t in time_steps
    ]
    
    ax1.plot(time_steps, physically_present, color='gray', linestyle=':', label='Physically Present (Parking)')
    ax1.plot(time_steps, active_counts, color='orange', linewidth=2, label='Active & Not Fully Charged')
    ax1.fill_between(time_steps, active_counts, color='orange', alpha=0.2)
    ax1.legend()
    
    # [하단] DBF vs SBF
    ax2.set_title("DBF vs Simulation-based SBF")
    ax2.set_xlabel("Time (t)")
    ax2.set_ylabel("Energy (kWh)")
    ax2.grid(True, which='both', linestyle='--')
    
    ax2.plot(time_steps, dbf_values, color='blue', linewidth=2, label='Demand (DBF)')
    ax2.plot(time_steps, sbf_values, color='red', linestyle='--', linewidth=2, label='Simulated Supply (SBF)')

    # Grid Cap 표시
    for end_time, capacity in SUPPLY_PROFILE:
        if end_time is not None and end_time <= time_limit:
            ax2.axvline(x=end_time, color='black', linestyle=':', alpha=0.3)
    
    # Overload 표시
    dbf_arr = np.array(dbf_values)
    sbf_arr = np.array(sbf_values)
    ax2.fill_between(time_steps, dbf_arr, sbf_arr, 
                     where=(dbf_arr > sbf_arr), 
                     color='red', alpha=0.3, interpolate=True, label='Overload')

    ax2.legend()
    plt.tight_layout()
    plt.show()

if __name__ == "__main__":
    main()