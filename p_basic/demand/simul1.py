import matplotlib.pyplot as plt
import matplotlib.patches as patches
import numpy as np
import csv

# ---------------------------------------------------------
# 1. 설정 및 상수
# ---------------------------------------------------------
FILENAME = '/users/jaewoo/data/ev/spc/ev_jobs.csv'
EV_MAX_POWER = 6.6  # 단일 차량 최대 충전 속도 (kW)

# 공급 프로필: (시간, 용량)
# 예: 10까지 20kW, 20까지 30kW, 그 이후 20kW
SUPPLY_PROFILE = [
    (10, 20),   
    (20, 30),   
    (None, 20)
]

# ---------------------------------------------------------
# 2. 클래스 정의
# ---------------------------------------------------------
class EV:
    def __init__(self, id, arrival, energy, departure):
        self.id = int(id)
        self.arrival = int(arrival)
        self.req_energy = float(energy)
        self.departure = int(departure)
        
        # 시뮬레이션 상태 변수
        self.rem_energy = float(energy)
        self.charged_energy = 0.0
        self.is_finished = False
        
        # 스케줄링 기록용 [(start, end, power), ...]
        self.charge_history = []

    def __repr__(self):
        return f"EV{self.id}"

# ---------------------------------------------------------
# 3. 데이터 로드 함수
# ---------------------------------------------------------
def load_evs(filename):
    evs = []
    try:
        with open(filename, mode='r', encoding='utf-8') as f:
            reader = csv.DictReader(f)
            for row in reader:
                evs.append(EV(row['ID'], row['Arrival'], row['Energy'], row['Departure']))
        print(f"Loaded {len(evs)} EVs.")
        return evs
    except FileNotFoundError:
        print("CSV 파일을 찾을 수 없습니다. 먼저 생성 코드를 실행하세요.")
        return []

# ---------------------------------------------------------
# 4. EDF 스케줄러 (Event-Driven Simulation)
# ---------------------------------------------------------
def run_edf_schedule(evs, profile, max_time):
    """
    EDF 알고리즘을 사용하여 정밀 충전 스케줄링 수행
    """
    # 시간 순서대로 처리하기 위해 정렬하지 않고, 이벤트 루프에서 처리
    current_time = 0.0
    
    # 정적 이벤트 목록 생성 (Grid Change, Arrival, Departure)
    # (time, type) -> type은 디버깅용
    static_events = set()
    static_events.add(max_time)
    
    for t, cap in profile:
        if t and t <= max_time: static_events.add(t)
    
    for ev in evs:
        if ev.arrival <= max_time: static_events.add(ev.arrival)
        if ev.departure <= max_time: static_events.add(ev.departure)
        
    # set을 list로 변환 후 정렬 (이벤트 타임라인)
    sorted_events = sorted(list(static_events))
    
    # 시뮬레이션 루프
    while current_time < max_time:
        # ----------------------------------------
        # A. 현재 상태 파악
        # ----------------------------------------
        # 1. 전력망 용량 확인
        grid_cap = profile[-1][1]
        for t_end, cap in profile:
            if t_end is None or current_time < t_end:
                grid_cap = cap
                break
        
        # 2. Active 차량 선별 (도착함 & 데드라인 전 & 충전 덜 됨)
        active_evs = [
            ev for ev in evs 
            if ev.arrival <= current_time 
            and current_time < ev.departure 
            and ev.rem_energy > 1e-6
        ]
        
        # 3. EDF 정렬 (가장 중요한 부분!) -> 데드라인 빠른 순
        active_evs.sort(key=lambda x: x.departure)
        
        # ----------------------------------------
        # B. 전력 할당 (Power Allocation)
        # ----------------------------------------
        remain_grid_power = grid_cap
        charging_rates = {} # {ev_id: power}
        
        for ev in active_evs:
            # 차량이 받을 수 있는 최대 파워 (6.6 vs 남은 그리드 용량)
            alloc = min(EV_MAX_POWER, remain_grid_power)
            
            if alloc > 0:
                charging_rates[ev.id] = alloc
                remain_grid_power -= alloc
            else:
                charging_rates[ev.id] = 0
            
            # 그리드 용량 소진 시 루프 중단 (뒷차들은 대기)
            if remain_grid_power <= 0:
                remain_grid_power = 0

        # ----------------------------------------
        # C. 다음 시간 점프 (Next Event Prediction)
        # ----------------------------------------
        # 1. 다음 정적 이벤트 찾기
        next_static = max_time
        for t in sorted_events:
            if t > current_time + 1e-9:
                next_static = t
                break
        
        # 2. 다음 동적 이벤트 (완충 시점) 예측
        min_dt_finish = float('inf')
        
        for ev in active_evs:
            rate = charging_rates.get(ev.id, 0)
            if rate > 0:
                time_needed = ev.rem_energy / rate
                if time_needed < min_dt_finish:
                    min_dt_finish = time_needed
                    
        next_finish = current_time + min_dt_finish if min_dt_finish != float('inf') else float('inf')
        
        # 3. 최종 점프 시간 결정
        next_time = min(next_static, next_finish)
        
        if next_time > max_time:
            next_time = max_time
            
        # ----------------------------------------
        # D. 상태 업데이트 및 기록
        # ----------------------------------------
        dt = next_time - current_time
        
        if dt > 1e-9:
            for ev in active_evs:
                rate = charging_rates.get(ev.id, 0)
                if rate > 0:
                    # 에너지 소모
                    consumed = rate * dt
                    ev.rem_energy -= consumed
                    ev.charged_energy += consumed
                    
                    # 기록 (히스토리)
                    ev.charge_history.append((current_time, next_time, rate))
                    
                    # 완충 체크
                    if ev.rem_energy < 1e-6:
                        ev.rem_energy = 0
                        ev.is_finished = True

        current_time = next_time
        if current_time >= max_time:
            break

# ---------------------------------------------------------
# 5. 결과 시각화 함수
# ---------------------------------------------------------
def plot_schedule(evs, profile, max_time):
    fig, ax = plt.subplots(figsize=(12, 7))
    
    # Y축 설정
    ax.set_ylim(0, len(evs) + 1)
    ax.set_yticks(range(1, len(evs) + 1))
    ax.set_yticklabels([f"EV {ev.id}" for ev in evs])
    ax.set_ylabel("Electric Vehicles")
    ax.set_xlabel("Time (Hour)")
    ax.set_title("Optimal EV Charging Schedule (EDF Policy)")
    ax.grid(True, linestyle='--', alpha=0.5)

    # 1. 배경에 그리드 용량 변화 표시 (선택사항)
    for t, cap in profile:
        if t and t <= max_time:
            ax.axvline(x=t, color='black', linestyle=':', alpha=0.3)
            ax.text(t, len(evs)+0.2, f"Grid Change", fontsize=8, rotation=90, color='gray')

    # 2. 차량별 스케줄 그리기
    for i, ev in enumerate(evs):
        y_pos = ev.id
        
        # A. 주차 가능 구간 (Arrival ~ Departure) - 회색 박스/선
        ax.hlines(y_pos, ev.arrival, ev.departure, colors='gray', linestyles='dotted', alpha=0.5)
        ax.plot(ev.arrival, y_pos, 'g>', markersize=5)    # 도착
        ax.plot(ev.departure, y_pos, 'r<', markersize=5)  # 출발(데드라인)
        
        # B. 실제 충전 구간 (Colored Bars)
        for start, end, power in ev.charge_history:
            width = end - start
            
            # 전력량에 따라 투명도 조절 (6.6kW면 진하게, 낮으면 연하게)
            alpha_val = 0.3 + 0.7 * (power / EV_MAX_POWER)
            
            rect = patches.Rectangle(
                (start, y_pos - 0.3),  # (x, y)
                width,                 # width
                0.6,                   # height
                facecolor='dodgerblue',
                edgecolor='blue',
                alpha=alpha_val
            )
            ax.add_patch(rect)
            
            # 구간이 길면 파워 텍스트 표시
            if width > 1.0:
                ax.text(start + width/2, y_pos, f"{power:.1f}kW", 
                        ha='center', va='center', fontsize=6, color='white')

        # C. 결과 텍스트 (우측)
        status_color = 'blue' if ev.rem_energy < 1e-4 else 'red'
        status_text = "Done" if ev.rem_energy < 1e-4 else f"Missed({ev.rem_energy:.1f})"
        ax.text(ev.departure + 0.2, y_pos, 
                f"{ev.charged_energy:.1f}/{ev.req_energy:.1f}kWh [{status_text}]", 
                va='center', fontsize=8, color=status_color, fontweight='bold')

    plt.tight_layout()
    plt.show()

# ---------------------------------------------------------
# 6. 메인 실행
# ---------------------------------------------------------
def main():
    # 1. 데이터 로드
    evs = load_evs(FILENAME)
    if not evs: return
    
    # 2. 시뮬레이션 범위 설정
    max_dep = max(ev.departure for ev in evs)
    sim_time = max(25, max_dep + 2)
    
    print("--- Start Scheduling (EDF) ---")
    
    # 3. 스케줄링 실행
    run_edf_schedule(evs, SUPPLY_PROFILE, sim_time)
    
    # 4. 결과 리포트 출력
    print(f"\n{'EV ID':<6} | {'Arrival':<7} | {'Deadline':<8} | {'Req(kWh)':<10} | {'Charged':<10} | {'Status'}")
    print("-" * 70)
    for ev in evs:
        status = "COMPLETE" if ev.rem_energy < 1e-4 else "MISS"
        print(f"{ev.id:<6} | {ev.arrival:<7} | {ev.departure:<8} | {ev.req_energy:<10.2f} | {ev.charged_energy:<10.2f} | {status}")
    
    # 5. 그래프 출력
    plot_schedule(evs, SUPPLY_PROFILE, sim_time)

if __name__ == "__main__":
    main()