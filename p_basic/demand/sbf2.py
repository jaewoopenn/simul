import matplotlib.pyplot as plt
import numpy as np
import csv
import heapq

# ---------------------------------------------------------
# 설정 및 상수
# ---------------------------------------------------------
FILENAME = '/users/jaewoo/data/ev/spc/ev_jobs.csv'
EV_MAX_POWER = 6.6  # 차량 1대당 최대 수전 용량 (kW)

# 공급 프로필: (시간, 용량)
SUPPLY_PROFILE = [
    (10, 20),   
    (20, 30),   
    (None, 20)
]

# ---------------------------------------------------------
# 클래스 및 기본 함수
# ---------------------------------------------------------
class EV:
    def __init__(self, id, arrival, energy, departure):
        self.id = int(id)
        self.arrival = int(arrival)
        self.energy = float(energy)
        self.departure = int(departure)
        self.actual_finish_time = None 

    # Heapq 정렬 시 ID 기준 비교 (에러 방지)
    def __lt__(self, other):
        return self.id < other.id

def load_evs_from_csv(filename):
    evs = []
    try:
        with open(filename, mode='r', encoding='utf-8') as f:
            reader = csv.DictReader(f)
            for row in reader:
                evs.append(EV(row['ID'], row['Arrival'], row['Energy'], row['Departure']))
        print(f"Loaded {len(evs)} EVs from {filename}")
        return evs
    except FileNotFoundError:
        print(f"Error: {filename} not found.")
        return []

def calculate_dbf(t, evs):
    """Demand Bound Function"""
    demand = 0
    for ev in evs:
        if ev.departure <= t:
            demand += ev.energy
    return demand

# ---------------------------------------------------------
# 핵심 로직: Event-Driven Exact SBF Calculation
# ---------------------------------------------------------
def calculate_exact_sbf_and_finish_times(evs, profile, max_time):
    """
    이벤트 기반 정밀 시뮬레이션
    조건:
      1. Deadline 지나면 충전 불가 (Active 제외)
      2. 완충되면 충전 불가 (Active 제외)
      3. 공급량 = min(Grid, Active_Count * 6.6)
    """
    # 1. 초기화
    remaining_energy = {ev.id: ev.energy for ev in evs}
    sbf_points = [(0, 0)]
    current_time = 0.0
    cumulative_supply = 0.0
    
    # 전력망 용량 초기화
    current_grid_cap = profile[0][1]
    
    # 2. 고정 이벤트 큐 생성 
    # Type 정의: 0=GridChange, 1=Arrival, 2=Departure (우선순위 순)
    events = []
    
    # (A) Grid Events
    for t_end, cap in profile:
        if t_end is not None and t_end <= max_time:
            heapq.heappush(events, (t_end, 0, cap))
    
    # (B) Vehicle Fixed Events (Arrival AND Departure)
    for ev in evs:
        if ev.arrival <= max_time:
            heapq.heappush(events, (ev.arrival, 1, ev))
        if ev.departure <= max_time:
            heapq.heappush(events, (ev.departure, 2, ev)) # 데드라인도 이벤트로 등록

    # --- Event Loop ---
    while current_time < max_time:
        # 0. 현재 시간의 이벤트 처리 (Grid update 등)
        while events and events[0][0] <= current_time + 1e-9:
            t, type, data = heapq.heappop(events)
            if type == 0: # Grid Change
                current_grid_cap = data
            # Arrival(1), Departure(2)는 active_evs 필터링 조건에서 자동 처리됨
        
        # A. 진성 Active 차량 식별
        # 조건: (도착함) AND (데드라인 전임) AND (에너지 남음)
        active_evs = [
            ev for ev in evs 
            if ev.arrival <= current_time 
            and current_time < ev.departure  # 데드라인 지나면 칼같이 제외
            and remaining_energy[ev.id] > 1e-6 # 완충되면 제외
        ]
        
        # B. 시스템 공급 능력 계산 (Upper Bound 적용)
        active_count = len(active_evs)
        vehicle_limit_cap = active_count * EV_MAX_POWER # 차량 수에 의한 상한선
        
        # 최종 공급 파워: 전력망 용량과 차량 수용 한계 중 작은 것
        actual_total_power = min(current_grid_cap, vehicle_limit_cap)
        
        # C. 다음 상태 변화(충전 완료) 예측
        min_dt_to_finish = float('inf')
        
        if active_count > 0 and actual_total_power > 1e-9:
            # EDF(급한 순) 배분 정책 가정
            active_evs.sort(key=lambda x: x.departure)
            
            power_left = actual_total_power
            for ev in active_evs:
                if power_left <= 1e-9: break
                
                # 이 차가 가져가는 파워
                allocated = min(EV_MAX_POWER, power_left)
                
                # 완충까지 남은 시간
                time_needed = remaining_energy[ev.id] / allocated
                if time_needed < min_dt_to_finish:
                    min_dt_to_finish = time_needed
                
                power_left -= allocated
        
        # D. 다음 이벤트 시간 결정
        # (1) 다음 고정 이벤트 (Grid, Arrival, Departure)
        next_static_time = events[0][0] if events else max_time
        
        # (2) 다음 완충 예측 시간
        next_finish_time = current_time + min_dt_to_finish if min_dt_to_finish != float('inf') else float('inf')
        
        # 둘 중 더 빠른 시간으로 점프
        next_event_time = min(next_static_time, next_finish_time)
        
        if next_event_time > max_time:
            next_event_time = max_time
            
        # E. 에너지 업데이트 (Simulation Step)
        dt = next_event_time - current_time
        
        if dt > 1e-9:
            # SBF 누적
            cumulative_supply += actual_total_power * dt
            sbf_points.append((next_event_time, cumulative_supply))
            
            # 차량 에너지 차감 (실제 배분)
            power_left_for_update = actual_total_power
            # 순서는 위에서 정렬한 EDF 그대로 사용
            for ev in active_evs:
                if power_left_for_update <= 1e-9: break
                
                allocated = min(EV_MAX_POWER, power_left_for_update)
                consumed = allocated * dt
                
                remaining_energy[ev.id] -= consumed
                power_left_for_update -= allocated
                
                # 완충 처리
                if remaining_energy[ev.id] < 1e-6:
                    remaining_energy[ev.id] = 0.0
                    # 처음 완충된 순간 기록
                    if ev.actual_finish_time is None:
                        ev.actual_finish_time = next_event_time
        
        # F. 시간 전진
        current_time = next_event_time
        
        if current_time >= max_time:
            break
            
    return sbf_points, evs

# ---------------------------------------------------------
# 메인 함수
# ---------------------------------------------------------
def main():
    evs = load_evs_from_csv(FILENAME)
    if not evs: return

    # 그래프 범위
    max_departure = max(ev.departure for ev in evs)
    time_limit = max(25, max_departure + 2)
    
    # 1. Exact SBF 계산
    sbf_points, updated_evs = calculate_exact_sbf_and_finish_times(evs, SUPPLY_PROFILE, time_limit)
    
    # 2. 데이터 보간 (그래프 그리기용)
    dense_time_steps = np.arange(0, time_limit + 0.1, 0.1)
    
    sbf_times, sbf_cumulatives = zip(*sbf_points)
    sbf_interp = np.interp(dense_time_steps, sbf_times, sbf_cumulatives)
    dbf_values = [calculate_dbf(t, evs) for t in dense_time_steps]

    # 3. Active Count 계산 (검증용)
    # 그래프에 그릴 때는 "실제로 충전기를 꽂고 있는 차"의 수
    active_counts = []
    for t in dense_time_steps:
        count = 0
        for ev in updated_evs:
            # 도착함 AND 데드라인 전 AND 완충 전(혹은 완충 시점 전)
            # actual_finish_time이 있으면 그때까지만 Active
            # 없으면(못 채우고 끝남) departure까지만 Active
            end_t = ev.actual_finish_time if ev.actual_finish_time else ev.departure
            
            if ev.arrival <= t < end_t:
                count += 1
        active_counts.append(count)

    # ---------------------------------------------------------
    # 그래프 그리기
    # ---------------------------------------------------------
    fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(10, 12), sharex=True)

    # === [상단] Gantt Chart ===
    ax1.set_title("EV Schedule & Real Active Count")
    ax1.set_ylabel("EV ID")
    ax1.grid(True, linestyle='--', alpha=0.5)
    ax1.set_ylim(0, len(evs) + 2)

    # Active Count 배경
    ax1_twin = ax1.twinx()
    ax1_twin.fill_between(dense_time_steps, active_counts, color='orange', alpha=0.15, step='post', label='Real Active Count')
    ax1_twin.plot(dense_time_steps, active_counts, color='orange', alpha=0.6, linewidth=1)
    ax1_twin.set_ylabel("Active Count (Limiting Factor)", color='orange')
    ax1_twin.set_ylim(0, max(active_counts)+2 if active_counts else 5)

    for ev in updated_evs:
        ax1.hlines(ev.id, ev.arrival, ev.departure, colors='gray', linestyles='dotted')
        ax1.plot(ev.arrival, ev.id, 'go', markersize=5)
        ax1.plot(ev.departure, ev.id, 'rx', markersize=7)
        
        # 실제 충전된 구간 표시
        finish_t = ev.actual_finish_time if ev.actual_finish_time else ev.departure
        duration = finish_t - ev.arrival
        
        # 색상: 정상완료(파랑), 미완료/데드라인도달(보라)
        # remaining_energy 검사 불가(지역변수라), finish_time 유무로 판단
        is_finished = (ev.actual_finish_time is not None)
        bar_color = 'dodgerblue' if is_finished else 'purple'
        
        ax1.barh(ev.id, duration, left=ev.arrival, height=0.4, 
                 color=bar_color, alpha=0.6, align='center', edgecolor=bar_color)

    # === [하단] DBF vs SBF ===
    ax2.set_title("Demand Bound vs Exact SBF (Limited by Active EVs)")
    ax2.set_xlabel("Time (t)")
    ax2.set_ylabel("Energy (kWh)")
    ax2.grid(True, which='both', linestyle='--')
    
    ax2.plot(dense_time_steps, dbf_values, color='blue', linewidth=2, label='Demand (DBF)')
    ax2.plot(sbf_times, sbf_cumulatives, color='red', linestyle='--', linewidth=2, label='Supply (SBF)')
    
    for t_end, cap in SUPPLY_PROFILE:
        if t_end and t_end <= time_limit:
            ax2.axvline(x=t_end, color='black', linestyle=':', alpha=0.3)

    # Overload
    ax2.fill_between(dense_time_steps, dbf_values, sbf_interp, 
                     where=(np.array(dbf_values) > np.array(sbf_interp)), 
                     color='red', alpha=0.3, interpolate=True, label='Overload')

    ax2.legend()
    plt.tight_layout()
    plt.show()

if __name__ == "__main__":
    main()