import matplotlib.pyplot as plt
import matplotlib.patches as patches
import numpy as np
import csv

# ---------------------------------------------------------
# 1. 설정 및 데이터 로드
# ---------------------------------------------------------
FILENAME = '/users/jaewoo/data/ev/spc/ev_jobs.csv'
EV_MAX_POWER = 6.6

# 공급 프로필 (시간, 용량)
SUPPLY_PROFILE = [
    (10, 20),   
    (20, 30),   
    (None, 20)
]

class EV:
    def __init__(self, id, arrival, energy, departure):
        self.id = int(id)
        self.arrival = int(arrival)
        self.req_energy = float(energy)
        self.departure = int(departure)
        self.rem_energy = float(energy)
        self.charged_energy = 0.0
        self.is_finished = False
        self.charge_history = []

def load_evs(filename):
    evs = []
    try:
        with open(filename, mode='r', encoding='utf-8') as f:
            reader = csv.DictReader(f)
            for row in reader:
                evs.append(EV(row['ID'], row['Arrival'], row['Energy'], row['Departure']))
        return evs
    except FileNotFoundError:
        return []

# ---------------------------------------------------------
# 2. EDF 스케줄러 (시스템 로그 기록 추가)
# ---------------------------------------------------------
def run_edf_schedule(evs, profile, max_time):
    current_time = 0.0
    
    # 시스템 상태 기록용 리스트 [(start, end, grid_cap, total_used), ...]
    system_history = []
    
    # 정적 이벤트 목록
    static_events = set()
    static_events.add(max_time)
    for t, cap in profile:
        if t and t <= max_time: static_events.add(t)
    for ev in evs:
        if ev.arrival <= max_time: static_events.add(ev.arrival)
        if ev.departure <= max_time: static_events.add(ev.departure)
    sorted_events = sorted(list(static_events))
    
    while current_time < max_time:
        # A. 공급 용량 확인
        grid_cap = profile[-1][1]
        for t_end, cap in profile:
            if t_end is None or current_time < t_end:
                grid_cap = cap
                break
        
        # B. Active EV 선정
        active_evs = [
            ev for ev in evs 
            if ev.arrival <= current_time 
            and current_time < ev.departure 
            and ev.rem_energy > 1e-6
        ]
        active_evs.sort(key=lambda x: x.departure) # EDF
        
        # C. 전력 배분
        remain_grid_power = grid_cap
        total_used_power = 0.0  # 현재 시점 실제 사용 총량
        charging_rates = {}
        
        for ev in active_evs:
            alloc = min(EV_MAX_POWER, remain_grid_power)
            if alloc > 0:
                charging_rates[ev.id] = alloc
                remain_grid_power -= alloc
                total_used_power += alloc
            else:
                charging_rates[ev.id] = 0
            
            if remain_grid_power <= 0:
                remain_grid_power = 0

        # D. 다음 시간 점프
        next_static = max_time
        for t in sorted_events:
            if t > current_time + 1e-9:
                next_static = t
                break
        
        min_dt_finish = float('inf')
        for ev in active_evs:
            rate = charging_rates.get(ev.id, 0)
            if rate > 0:
                time_needed = ev.rem_energy / rate
                if time_needed < min_dt_finish:
                    min_dt_finish = time_needed
                    
        next_finish = current_time + min_dt_finish if min_dt_finish != float('inf') else float('inf')
        next_time = min(next_static, next_finish)
        
        if next_time > max_time: next_time = max_time
            
        # E. 업데이트 및 기록
        dt = next_time - current_time
        if dt > 1e-9:
            # --- [추가된 부분] 시스템 로그 기록 ---
            system_history.append((current_time, next_time, grid_cap, total_used_power))
            # -------------------------------------
            
            for ev in active_evs:
                rate = charging_rates.get(ev.id, 0)
                if rate > 0:
                    consumed = rate * dt
                    ev.rem_energy -= consumed
                    ev.charged_energy += consumed
                    ev.charge_history.append((current_time, next_time, rate))
                    if ev.rem_energy < 1e-6:
                        ev.rem_energy = 0
                        ev.is_finished = True

        current_time = next_time
        if current_time >= max_time: break
            
    return system_history

# ---------------------------------------------------------
# 3. 시각화 (미사용 전력량 표시)
# ---------------------------------------------------------
def plot_results(evs, system_history, max_time):
    # 2개의 서브플롯 생성 (위: 간트차트, 아래: 전력량)
    fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(12, 10), sharex=True, gridspec_kw={'height_ratios': [2, 1]})
    
    # === [상단 그래프] EV 스케줄링 간트 차트 ===
    ax1.set_title("1. Individual EV Charging Schedule (EDF)")
    ax1.set_ylabel("EV ID")
    ax1.set_ylim(0, len(evs) + 1)
    ax1.set_yticks(range(1, len(evs) + 1))
    ax1.set_yticklabels([f"EV {ev.id}" for ev in evs])
    ax1.grid(True, linestyle='--', alpha=0.5)
    miss=0
    for ev in evs:
        y_pos = ev.id
        # 주차 구간
        ax1.hlines(y_pos, ev.arrival, ev.departure, colors='gray', linestyles='dotted', alpha=0.5)
        ax1.plot(ev.arrival, y_pos, 'g>', markersize=5)
        ax1.plot(ev.departure, y_pos, 'r<', markersize=5)
        
        # 충전 바
        for start, end, power in ev.charge_history:
            width = end - start
            alpha_val = 0.3 + 0.7 * (power / EV_MAX_POWER)
            rect = patches.Rectangle((start, y_pos - 0.3), width, 0.6, 
                                     facecolor='dodgerblue', edgecolor='blue', alpha=alpha_val)
            ax1.add_patch(rect)

        # 상태 텍스트
        status_color = 'blue' if ev.rem_energy < 1e-4 else 'red'
        if status_color=='red':
            miss+=1
        ax1.text(ev.departure + 0.2, y_pos, f"{ev.charged_energy:.1f}/{ev.req_energy:.1f}", 
                 va='center', fontsize=8, color=status_color, fontweight='bold')
    print(f"{miss/len(evs):.2f}")
    # === [하단 그래프] 시스템 전력 및 미사용량 ===
    ax2.set_title("2. System Power Usage & Wasted Capacity")
    ax2.set_ylabel("Power (kW)")
    ax2.set_xlabel("Time (Hour)")
    ax2.grid(True, linestyle='--', alpha=0.5)
    
    # 데이터를 그리기 좋게 변환 (Rectangle Patch 사용)
    max_cap_seen = 0
    
    for start, end, cap, used in system_history:
        width = end - start
        max_cap_seen = max(max_cap_seen, cap)
        
        # 1. 사용된 전력 (Used) - 파란색
        if used > 0:
            rect_used = patches.Rectangle((start, 0), width, used,
                                          facecolor='dodgerblue', alpha=0.6, edgecolor='none')
            ax2.add_patch(rect_used)
            
        # 2. 미사용 전력 (Unused) - 회색 (Used 위부터 Cap까지)
        unused = cap - used
        if unused > 0:
            rect_unused = patches.Rectangle((start, used), width, unused,
                                            facecolor='lightgray', hatch='//', edgecolor='gray', alpha=0.5)
            ax2.add_patch(rect_unused)
            
        # 3. 전체 용량 선 (Capacity Line) - 빨간 실선
        ax2.hlines(cap, start, end, colors='red', linewidth=1.5)
        # 세로선(용량 변화 시각적 연결)
        ax2.vlines(start, 0, cap, colors='gray', linestyles=':', alpha=0.2)

    # 범례 생성
    from matplotlib.lines import Line2D
    legend_elements = [
        patches.Patch(facecolor='dodgerblue', alpha=0.6, label='Used Power (Charging)'),
        patches.Patch(facecolor='lightgray', hatch='//', edgecolor='gray', alpha=0.5, label='Unused Capacity (Wasted)'),
        Line2D([0], [0], color='red', lw=2, label='Grid Capacity Limit')
    ]
    ax2.legend(handles=legend_elements, loc='upper right')
    
    # Y축 범위 조정
    ax2.set_ylim(0, max_cap_seen * 1.2)

    plt.tight_layout()
    plt.show()

# ---------------------------------------------------------
# 4. 메인 실행
# ---------------------------------------------------------
def main():
    evs = load_evs(FILENAME)
    if not evs: return
    
    max_dep = max(ev.departure for ev in evs)
    sim_time = max(25, max_dep + 2)
    
    print("--- Scheduling & Analyzing Power Usage ---")
    system_history = run_edf_schedule(evs, SUPPLY_PROFILE, sim_time)
    
    plot_results(evs, system_history, sim_time)

if __name__ == "__main__":
    main()