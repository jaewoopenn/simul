import matplotlib.pyplot as plt
import matplotlib.patches as patches
import numpy as np
import csv

# ---------------------------------------------------------
# 1. 설정 및 데이터 로드
# ---------------------------------------------------------
FILENAME = '/users/jaewoo/data/ev/spc/ev_jobs.csv'
EV_MAX_POWER = 6.6
TIME_STEP = 0.05  # 시뮬레이션 시간 단위 (시간), 0.05h = 3분

# 공급 프로필 (시간, 용량)
SUPPLY_PROFILE = [
    # (10, 20),   
    # (20, 30),   
    (None, 21)
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
        
        # 시각화를 위한 기록 [(start, end, power), ...]
        self.charge_history = []
        
        # 내부 계산용: 현재 스텝에서의 Laxity
        self.current_laxity = 0.0

def load_evs(filename):
    evs = []
    try:
        with open(filename, mode='r', encoding='utf-8') as f:
            reader = csv.DictReader(f)
            for row in reader:
                evs.append(EV(row['ID'], row['Arrival'], row['Energy'], row['Departure']))
        return evs
    except FileNotFoundError:
        print("CSV 파일이 없습니다.")
        return []

# ---------------------------------------------------------
# 2. LLF 스케줄러 (Time-Step Simulation)
# ---------------------------------------------------------
def run_llf_schedule(evs, profile, max_time):
    # 시뮬레이션 시간 축 생성
    time_steps = np.arange(0, max_time, TIME_STEP)
    
    # 시스템 상태 기록용 리스트
    system_history = []
    
    # 기록 최적화용 (연속된 충전 구간 병합)
    last_rates = {ev.id: -1 for ev in evs} # 직전 스텝의 충전 파워
    
    for current_time in time_steps:
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
        
        # --- [LLF 핵심 로직] Laxity 계산 및 정렬 ---
        for ev in active_evs:
            # 남은 시간
            time_remaining = ev.departure - current_time
            # 완충까지 필요한 최소 시간
            time_needed = ev.rem_energy / EV_MAX_POWER
            # Laxity = 남은 시간 - 필요한 시간
            ev.current_laxity = time_remaining - time_needed
            
        # Laxity가 작은 순서대로 정렬 (음수면 이미 늦음 -> 최우선)
        active_evs.sort(key=lambda x: x.current_laxity)
        # ----------------------------------------
        
        # C. 전력 배분
        remain_grid_power = grid_cap
        total_used_power = 0.0
        current_rates = {} # 이번 스텝의 할당량
        
        for ev in active_evs:
            alloc = min(EV_MAX_POWER, remain_grid_power)
            
            if alloc > 0:
                current_rates[ev.id] = alloc
                remain_grid_power -= alloc
                total_used_power += alloc
            else:
                current_rates[ev.id] = 0
            
            if remain_grid_power <= 0:
                remain_grid_power = 0
                
        # D. 상태 업데이트 (에너지 차감)
        # 시스템 기록
        system_history.append((current_time, current_time + TIME_STEP, grid_cap, total_used_power))
        
        for ev in active_evs:
            rate = current_rates.get(ev.id, 0)
            
            if rate > 0:
                consumed = rate * TIME_STEP
                ev.rem_energy -= consumed
                ev.charged_energy += consumed
                
                # 시각화 기록 (연속 구간 병합 로직)
                # 만약 직전 스텝과 파워가 같고, 시간이 이어지면 -> 종료 시간만 연장
                if ev.charge_history and ev.charge_history[-1][2] == rate and \
                   abs(ev.charge_history[-1][1] - current_time) < 1e-5:
                    start, _, p = ev.charge_history.pop()
                    ev.charge_history.append((start, current_time + TIME_STEP, p))
                else:
                    ev.charge_history.append((current_time, current_time + TIME_STEP, rate))
                
                # 완충 체크
                if ev.rem_energy < 1e-6:
                    ev.rem_energy = 0
                    ev.is_finished = True
                    
            # 이번 스텝 파워 기록 (다음 스텝 비교용)
            last_rates[ev.id] = rate

    return system_history

# ---------------------------------------------------------
# 3. 시각화 함수 (동일)
# ---------------------------------------------------------
def plot_results(evs, system_history, max_time):
    fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(12, 10), sharex=True, gridspec_kw={'height_ratios': [2, 1]})
    
    # === [상단] LLF 스케줄링 결과 ===
    ax1.set_title("1. Individual EV Charging Schedule (LLF Policy)")
    ax1.set_ylabel("EV ID")
    ax1.set_ylim(0, len(evs) + 1)
    ax1.set_yticks(range(1, len(evs) + 1))
    ax1.set_yticklabels([f"EV {ev.id}" for ev in evs])
    ax1.grid(True, linestyle='--', alpha=0.5)

    for ev in evs:
        y_pos = ev.id
        ax1.hlines(y_pos, ev.arrival, ev.departure, colors='gray', linestyles='dotted', alpha=0.5)
        ax1.plot(ev.arrival, y_pos, 'g>', markersize=5)
        ax1.plot(ev.departure, y_pos, 'r<', markersize=5)
        
        for start, end, power in ev.charge_history:
            width = end - start
            if width < 0.01: continue # 너무 짧은 구간 생략
            alpha_val = 0.3 + 0.7 * (power / EV_MAX_POWER)
            rect = patches.Rectangle((start, y_pos - 0.3), width, 0.6, 
                                     facecolor='darkorange', edgecolor='chocolate', alpha=alpha_val)
            ax1.add_patch(rect)

        status_color = 'blue' if ev.rem_energy < 1e-4 else 'red'
        ax1.text(ev.departure + 0.2, y_pos, f"{ev.charged_energy:.1f}/{ev.req_energy:.1f}", 
                 va='center', fontsize=8, color=status_color, fontweight='bold')

    # === [하단] 시스템 전력 ===
    ax2.set_title("2. System Power Usage & Wasted Capacity (LLF)")
    ax2.set_ylabel("Power (kW)")
    ax2.set_xlabel("Time (Hour)")
    ax2.grid(True, linestyle='--', alpha=0.5)
    
    # 시스템 기록 병합 및 시각화
    # (Time-Step 기록이 너무 많으므로, 시각화 시 같은 상태면 병합하여 그림)
    merged_history = []
    if system_history:
        curr_start, _, curr_cap, curr_used = system_history[0]
        curr_end = system_history[0][1]
        
        for i in range(1, len(system_history)):
            s, e, c, u = system_history[i]
            if c == curr_cap and abs(u - curr_used) < 1e-4:
                curr_end = e
            else:
                merged_history.append((curr_start, curr_end, curr_cap, curr_used))
                curr_start, curr_end, curr_cap, curr_used = s, e, c, u
        merged_history.append((curr_start, curr_end, curr_cap, curr_used))

    max_cap_seen = 0
    for start, end, cap, used in merged_history:
        width = end - start
        max_cap_seen = max(max_cap_seen, cap)
        
        if used > 0:
            ax2.add_patch(patches.Rectangle((start, 0), width, used,
                                          facecolor='darkorange', alpha=0.6))
        unused = cap - used
        if unused > 0:
            ax2.add_patch(patches.Rectangle((start, used), width, unused,
                                            facecolor='lightgray', hatch='//', alpha=0.5))
        ax2.hlines(cap, start, end, colors='red', linewidth=1.5)

    from matplotlib.lines import Line2D
    legend_elements = [
        patches.Patch(facecolor='darkorange', alpha=0.6, label='Used Power (LLF)'),
        patches.Patch(facecolor='lightgray', hatch='//', alpha=0.5, label='Wasted'),
        Line2D([0], [0], color='red', lw=2, label='Grid Limit')
    ]
    ax2.legend(handles=legend_elements, loc='upper right')
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
    
    print("--- Start Scheduling (LLF - Least Laxity First) ---")
    system_history = run_llf_schedule(evs, SUPPLY_PROFILE, sim_time)
    
    print(f"\n{'EV ID':<6} | {'Arrival':<7} | {'Deadline':<8} | {'Req(kWh)':<10} | {'Charged':<10} | {'Status'}")
    print("-" * 70)
    sum=0
    miss=0
    tot=len(evs)
    for ev in evs:
        status = "COMPLETE" if ev.rem_energy < 1e-4 else "MISS"
        if status == "MISS":
            miss+=1
        sum+=ev.charged_energy
        print(f"{ev.id:<6} | {ev.req_energy:<10.2f} | {ev.charged_energy:<10.2f} | {status}")
    print(f"{sum:.2f}")
    print(f"{miss/tot:.2f}")
    
    plot_results(evs, system_history, sim_time)

if __name__ == "__main__":
    main()