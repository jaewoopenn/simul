import matplotlib.pyplot as plt
import matplotlib.patches as patches
import numpy as np
import csv

# ---------------------------------------------------------
# 설정 및 데이터 로드
# ---------------------------------------------------------
FILENAME = '/users/jaewoo/data/ev/spc/ev_jobs.csv'
EV_MAX_POWER = 6.6
TIME_STEP = 0.05  # 3분 단위

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
# Fair Sharing (Water-Filling) 스케줄러
# ---------------------------------------------------------
def run_fair_sharing_schedule(evs, profile, max_time):
    time_steps = np.arange(0, max_time, TIME_STEP)
    system_history = []
    
    for current_time in time_steps:
        # A. 공급 용량 확인
        grid_cap = profile[-1][1]
        for t_end, cap in profile:
            if t_end is None or current_time < t_end:
                grid_cap = cap
                break
        
        # B. Active EV 선정 (도착함 & 완충 안됨 & 데드라인 전)
        active_evs = [
            ev for ev in evs 
            if ev.arrival <= current_time 
            and current_time < ev.departure 
            and ev.rem_energy > 1e-6
        ]
        
        # C. Water-Filling 전력 배분 로직
        # 목표: grid_cap을 active_evs에게 최대한 공평하게, 남김없이 뿌리기
        
        current_rates = {ev.id: 0.0 for ev in active_evs}
        
        if active_evs:
            power_to_distribute = grid_cap
            
            # 재분배 루프: 제한(6.6kW)에 걸린 차들을 제외하고 남은 전력을 다시 N빵
            while power_to_distribute > 1e-6 and active_evs:
                # 1. 이번 라운드에 나눠줄 수 있는 전력이 없는 차량 제외 (이미 6.6을 받았거나 배터리 다 찬 경우)
                # (아래 로직에서 처리됨)
                
                # 2. 공평하게 분배 (Fair Share)
                count = len(active_evs)
                share = power_to_distribute / count
                
                # 3. 할당 및 제한 확인
                saturated_evs = [] # 더 이상 전력을 못 받는 차들
                
                for ev in active_evs:
                    # 기존에 받은거 + 이번에 받을거
                    prev_alloc = current_rates[ev.id]
                    potential_total = prev_alloc + share
                    
                    # 물리적 한계 (6.6kW)
                    limit_power = EV_MAX_POWER
                    
                    # (선택) 배터리 잔량 한계 (이번 스텝에 완충될 양까지만)
                    # 이를 적용하면 정말 완벽하게 낭비가 줄어듦
                    limit_energy_rate = ev.rem_energy / TIME_STEP
                    real_limit = min(limit_power, limit_energy_rate)
                    
                    if potential_total >= real_limit:
                        # 한계에 도달함 -> 한계만큼만 주고 리스트에서 제외
                        alloc = real_limit - prev_alloc
                        current_rates[ev.id] += alloc
                        power_to_distribute -= alloc
                        saturated_evs.append(ev)
                    else:
                        # 한계 도달 안함 -> 일단 share만큼 다 줌
                        current_rates[ev.id] += share
                        power_to_distribute -= share
                
                # 더 이상 못 받는 차들은 다음 분배에서 제외
                for ev in saturated_evs:
                    active_evs.remove(ev)
                
                # 만약 포화된 차가 하나도 없었다면(모두가 share를 다 받음), 분배 끝
                if not saturated_evs:
                    break
        
        # D. 상태 업데이트
        # (원래 active 리스트가 위에서 변형되었으므로, 다시 전체 ev 대상으로 기록)
        total_used_power = sum(current_rates.values())
        system_history.append((current_time, current_time + TIME_STEP, grid_cap, total_used_power))
        
        # 실제 에너지 차감 및 기록
        # 다시 전체 리스트에서 찾아서 업데이트 (위의 active_evs는 루프 돌면서 비워짐)
        targets = [ev for ev in evs if ev.id in current_rates and current_rates[ev.id] > 0]
        
        for ev in targets:
            rate = current_rates[ev.id]
            consumed = rate * TIME_STEP
            ev.rem_energy -= consumed
            ev.charged_energy += consumed
            
            # 시각화 기록 병합
            if ev.charge_history and abs(ev.charge_history[-1][2] - rate) < 1e-3 and \
               abs(ev.charge_history[-1][1] - current_time) < 1e-5:
                start, _, p = ev.charge_history.pop()
                ev.charge_history.append((start, current_time + TIME_STEP, p))
            else:
                ev.charge_history.append((current_time, current_time + TIME_STEP, rate))
            
            if ev.rem_energy < 1e-6:
                ev.rem_energy = 0
                ev.is_finished = True

    return system_history

# ---------------------------------------------------------
# 시각화 함수 (보라색 테마)
# ---------------------------------------------------------
def plot_results(evs, system_history, max_time):
    fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(12, 10), sharex=True, gridspec_kw={'height_ratios': [2, 1]})
    
    # 상단 그래프
    ax1.set_title("1. EV Charging Schedule (Fair Sharing / Max Utilization)")
    ax1.set_ylabel("EV ID")
    ax1.set_ylim(0, len(evs) + 1)
    ax1.set_yticks(range(1, len(evs) + 1))
    ax1.grid(True, linestyle='--', alpha=0.5)

    for ev in evs:
        y_pos = ev.id
        ax1.hlines(y_pos, ev.arrival, ev.departure, colors='gray', linestyles='dotted', alpha=0.5)
        ax1.plot(ev.arrival, y_pos, 'g>', markersize=5)
        ax1.plot(ev.departure, y_pos, 'r<', markersize=5)
        
        for start, end, power in ev.charge_history:
            width = end - start
            if width < 0.01: continue
            alpha_val = 0.3 + 0.7 * (power / EV_MAX_POWER)
            # 색상: Purple
            rect = patches.Rectangle((start, y_pos - 0.3), width, 0.6, 
                                     facecolor='mediumpurple', edgecolor='indigo', alpha=alpha_val)
            ax1.add_patch(rect)
            
            # 파워 표시
            if width > 2.0:
                 ax1.text(start + width/2, y_pos, f"{power:.1f}", 
                         ha='center', va='center', fontsize=6, color='white')

        status_color = 'blue' if ev.rem_energy < 1e-4 else 'red'
        ax1.text(ev.departure + 0.2, y_pos, f"{ev.charged_energy:.1f}/{ev.req_energy:.1f}", 
                 va='center', fontsize=8, color=status_color, fontweight='bold')

    # 하단 그래프
    ax2.set_title("2. System Power Usage (Minimized Waste)")
    ax2.set_ylabel("Power (kW)")
    ax2.set_xlabel("Time (Hour)")
    ax2.grid(True, linestyle='--', alpha=0.5)
    
    # 데이터 병합
    merged_history = []
    if system_history:
        curr_start, _, curr_cap, curr_used = system_history[0]
        curr_end = system_history[0][1]
        for i in range(1, len(system_history)):
            s, e, c, u = system_history[i]
            if c == curr_cap and abs(u - curr_used) < 1e-3:
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
                                          facecolor='mediumpurple', alpha=0.7))
        unused = cap - used
        if unused > 0:
            ax2.add_patch(patches.Rectangle((start, used), width, unused,
                                            facecolor='lightgray', hatch='//', alpha=0.5))
        ax2.hlines(cap, start, end, colors='red', linewidth=1.5)

    ax2.set_ylim(0, max_cap_seen * 1.2)
    plt.tight_layout()
    plt.show()

def main():
    evs = load_evs(FILENAME)
    if not evs: return
    
    max_dep = max(ev.departure for ev in evs)
    sim_time = max(25, max_dep + 2)
    
    print("--- Start Scheduling (Fair Sharing - Maximize Usage) ---")
    system_history = run_fair_sharing_schedule(evs, SUPPLY_PROFILE, sim_time)
    
    print(f"\n{'EV ID':<6} | {'Req(kWh)':<10} | {'Charged':<10} | {'Status'}")
    print("-" * 50)
    sum=0
    for ev in evs:
        status = "COMPLETE" if ev.rem_energy < 1e-4 else "MISS"
        sum+=ev.charged_energy
        print(f"{ev.id:<6} | {ev.req_energy:<10.2f} | {ev.charged_energy:<10.2f} | {status}")
    print(f"{sum:.2f}")
    plot_results(evs, system_history, sim_time)

if __name__ == "__main__":
    main()