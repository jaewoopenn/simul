import matplotlib.pyplot as plt
import numpy as np
import csv

# 불러올 파일 이름
FILENAME = '/users/jaewoo/data/ev/spc/ev_jobs.csv'
EV_MAX_POWER = 6.6  # 차량 1대당 최대 수전 용량 (kW)

# ---------------------------------------------------------
# SBF 공급 프로필 (Grid Capacity)
# (시간, 용량): 해당 시간'까지' 해당 용량으로 전력망 공급 가능
# ---------------------------------------------------------
SUPPLY_PROFILE = [
    (10, 20),   # 0 ~ 10시간: 20kW
    (20, 30),   # 10 ~ 20시간: 30kW
    (None, 20)  # 20시간 이후: 20kW
]

class EV:
    def __init__(self, id, arrival, energy, departure):
        self.id = int(id)
        self.arrival = int(arrival)
        self.energy = float(energy)
        self.departure = int(departure)

def load_evs_from_csv(filename):
    """CSV 파일에서 EV 데이터 로드"""
    evs = []
    try:
        with open(filename, mode='r', encoding='utf-8') as f:
            reader = csv.DictReader(f)
            for row in reader:
                evs.append(EV(
                    row['ID'], 
                    row['Arrival'], 
                    row['Energy'], 
                    row['Departure']
                ))
        print(f"성공: '{filename}'에서 {len(evs)}개의 데이터를 불러왔습니다.")
        return evs
    except FileNotFoundError:
        print(f"오류: '{filename}' 파일을 찾을 수 없습니다. 생성 코드를 먼저 실행하세요.")
        return []

def get_grid_capacity(t, profile):
    """특정 시점 t에서의 전력망 용량 반환"""
    current_cap = 0
    # 프로필을 순회하며 현재 시간 t에 해당하는 용량 찾기
    for end_time, capacity in profile:
        current_cap = capacity
        if end_time is None or t < end_time:
            break
    return current_cap

def get_active_ev_count(t, evs):
    """
    특정 시점 t에 충전소에 존재하는(충전 가능한) 차량 수
    조건: Arrival <= t < Departure
    """
    count = 0
    for ev in evs:
        # t 시점에 주차장에 있는 차 (도착함 AND 아직 안떠남)
        if ev.arrival <= t and t < ev.departure:
            count += 1
    return count

def calculate_dynamic_sbf_array(time_steps, evs, profile):
    """
    전체 시간축에 대한 SBF 값을 순차적으로 계산 (적분)
    Logic: 매 스텝마다 min(Grid, Cars * 6.6) 을 누적
    """
    sbf_values = []
    current_energy = 0
    
    # 시간 간격 (dt) 계산 (보통 0.1)
    if len(time_steps) > 1:
        dt = time_steps[1] - time_steps[0]
    else:
        dt = 0.1

    for t in time_steps:
        # 1. 전력망 한계 (Grid Limit)
        grid_cap = get_grid_capacity(t, profile)
        
        # 2. 차량 수용 한계 (Vehicle Acceptance Limit)
        active_cars = get_active_ev_count(t, evs)
        vehicle_cap = active_cars * EV_MAX_POWER
        
        # 3. 실제 공급률 (Bottleneck 결정)
        actual_supply_rate = min(grid_cap, vehicle_cap)
        
        # 4. 적분 (에너지 누적)
        # SBF(t)는 0부터 t까지의 누적 공급량
        # (주의: t=0일 때는 에너지가 0이어야 하므로, 계산 후 append 하거나 이전 값을 더함)
        # 여기서는 Step 방식으로 적분 (Rectangular rule)
        current_energy += actual_supply_rate * dt
        sbf_values.append(current_energy)
        
    return sbf_values, dt # dt는 나중에 디버깅용으로 리턴

def calculate_dbf(t, evs):
    """Demand Bound Function (기존 동일)"""
    demand = 0
    for ev in evs:
        if ev.departure <= t:
            demand += ev.energy
    return demand

def main():
    # 1. 데이터 로드
    evs = load_evs_from_csv(FILENAME)
    if not evs: return

    # 2. 시간 축 설정
    max_departure = max(ev.departure for ev in evs)
    time_limit = max(25, max_departure + 2)
    time_steps = np.arange(0, time_limit + 0.1, 0.1)

    # 3. 계산
    # DBF 계산
    dbf_values = [calculate_dbf(t, evs) for t in time_steps]
    
    # SBF 계산 (새로운 로직: Grid Cap vs EV Cap)
    sbf_values, dt = calculate_dynamic_sbf_array(time_steps, evs, SUPPLY_PROFILE)

    # 4. 그래프 그리기
    fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(10, 12), sharex=True)

    # === [상단] Gantt Chart ===
    ax1.set_title(f"EV Schedule & Active Count")
    ax1.set_ylabel("EV ID")
    ax1.grid(True, linestyle='--', alpha=0.5)
    ax1.set_ylim(0, len(evs) + 2) # 공간 확보
    
    # 차량별 바 그리기
    for ev in evs:
        ax1.hlines(ev.id, ev.arrival, ev.departure, colors='gray', linestyles='dotted')
        ax1.plot(ev.arrival, ev.id, 'go', markersize=5)
        ax1.plot(ev.departure, ev.id, 'rx', markersize=7)
        min_duration = ev.energy / EV_MAX_POWER
        ax1.barh(ev.id, min_duration, left=ev.arrival, height=0.4, color='dodgerblue', alpha=0.6)

    # 동시 접속 차량 수(Active Count)를 상단 그래프 배경에 실선으로 표시 (보조축 사용)
    ax1_twin = ax1.twinx()
    active_counts = [get_active_ev_count(t, evs) for t in time_steps]
    ax1_twin.fill_between(time_steps, active_counts, color='orange', alpha=0.1, step='post', label='Active EVs Count')
    ax1_twin.set_ylabel("Active EVs Count", color='orange')
    ax1_twin.tick_params(axis='y', labelcolor='orange')
    ax1_twin.set_ylim(0, max(active_counts)+2)

    # === [하단] DBF vs Dynamic SBF ===
    ax2.set_title("Demand Bound vs Dynamic Supply Bound (Min[Grid, Cars*6.6])")
    ax2.set_xlabel("Time (t)")
    ax2.set_ylabel("Energy (kWh)")
    ax2.grid(True, which='both', linestyle='--')
    
    ax2.plot(time_steps, dbf_values, color='blue', linewidth=2, label='Demand (DBF)')
    ax2.plot(time_steps, sbf_values, color='red', linestyle='--', linewidth=2, label='Dynamic SBF')
    
    # Grid Capacity 변경 시점 표시
    prev_time = 0
    for end_time, capacity in SUPPLY_PROFILE:
        if end_time is not None and end_time <= time_limit:
            ax2.axvline(x=end_time, color='black', linestyle=':', alpha=0.3)
            ax2.text(end_time, max(sbf_values)/15, f"Grid Cap Change", fontsize=8, rotation=90, alpha=0.5)

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