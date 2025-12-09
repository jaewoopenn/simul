import matplotlib.pyplot as plt
import numpy as np
import random

# -------------------------------------------------------------
# 1. 공급 프로필 설정 (사용자가 자유롭게 조절하는 부분)
# 형식: (이 시간까지, 이 용량으로 공급)
# 마지막 항목의 시간은 None (그 이후 쭉 계속됨을 의미)
# -------------------------------------------------------------
# 예시: 0~10초: 20kW, 10~20초: 30kW, 20초 이후: 20kW
SUPPLY_PROFILE = [
    (10, 15),   
    (20, 25),   
    (None, 15)  
]

class EV:
    def __init__(self, id, arrival, energy, departure):
        self.id = id
        self.arrival = arrival       # 도착 시간
        self.energy = energy         # 필요한 충전량 (kWh)
        self.departure = departure   # 출차 시간 (Deadline)

    def __repr__(self):
        return f"EV{self.id}(Arr={self.arrival}, Energy={self.energy:.1f}, Dep={self.departure})"


def generate_random_evs(num_evs=10):
    """
    랜덤한 10대의 EV 생성
    조건: 개별 차량의 최대 충전 속도는 6.6 kW
    """
    evs = []
    print("--- Generated EVs (Arrival, Energy, Departure) ---")
    for i in range(num_evs):
        # Arrival: 0 ~ 15 사이 랜덤
        a = random.randint(0, 15)
        
        # 주차 시간 (Duration): 최소 1시간 ~ 최대 10시간
        duration = random.randint(1, 10)
        departure = a + duration
        
        # 충전 요구량 (Energy): 
        # 물리적 한계: (머무는 시간) * 6.6 kW 를 넘을 수 없음
        max_feasible_energy = duration * 6.6
        
        # 최소 5kWh ~ 최대 가능량 사이 랜덤
        # (max_feasible_energy가 5보다 작으면 그 값을 최대로 씀)
        min_req = min(5, max_feasible_energy)
        energy = random.uniform(min_req, max_feasible_energy)
        
        evs.append(EV(i+1, a, energy, departure))
        print(f"EV {i+1}: Arr={a}, Dep={departure} (Duration={duration}), Energy={energy:.2f} kWh (Max Possible: {max_feasible_energy:.1f})")
    print("-------------------------------------------------------")
    return evs

def calculate_dbf(t, evs):
    """
    Demand Bound Function (DBF)
    시간 t까지 반드시 충전되어야 하는 에너지의 총합 (Deadline <= t 인 EV들의 합)
    """
    demand = 0
    for ev in evs:
        if ev.departure <= t:
            demand += ev.energy
    return demand


def calculate_sbf(t):
    """
    유연한 Supply Bound Function (SBF) 계산
    주어진 공급 프로필(profile)에 따라 시간 t까지의 누적 공급량(에너지)을 계산
    """
    total_supply = 0
    current_time_cursor = 0  # 계산을 시작할 기준 시간

    for end_time, capacity in SUPPLY_PROFILE:
        # 이미 시간 t에 도달했으면 계산 종료
        if t <= current_time_cursor:
            break

        # 이번 구간에서 계산할 종료 시점 결정
        # end_time이 None이면(마지막 구간), 그냥 t까지 계산
        if end_time is None:
            limit = t
        else:
            limit = min(t, end_time)

        # 이번 구간의 지속 시간(Duration)
        duration = limit - current_time_cursor
        
        # 누적 공급량 추가 (시간 * 용량)
        if duration > 0:
            total_supply += duration * capacity

        # 기준 시간을 이번 구간 끝으로 업데이트
        current_time_cursor = limit
        
        # 만약 이번 구간 끝이 목표 시간 t보다 크거나 같다면 루프 종료
        if current_time_cursor >= t:
            break
            
    return total_supply

# -------------------------------------------------------------
# 사용 예시 (기존 코드의 main 부분에 반영하는 방법)
# -------------------------------------------------------------
# ... (기존 generate_random_evs, calculate_dbf 함수 등은 동일) ...

def main():
    
    # 랜덤 EV 10대 생성 (기존 함수 사용)
    evs = generate_random_evs(15)

    # 그래프 시간 축 설정
    max_departure = max(ev.departure for ev in evs) if evs else 0
    # 프로필 변화(t=20)를 포함하고, 마지막 출차보다 조금 더 뒤까지 표시
    time_limit = max(25, max_departure + 2)
    time_steps = np.arange(0, time_limit + 0.1, 0.1)

    # ---------------------------------------------------------
    # 2. 계산 (DBF & Variable SBF)
    # ---------------------------------------------------------
    dbf_values = [calculate_dbf(t, evs) for t in time_steps]
    sbf_values = [calculate_sbf(t) for t in time_steps]

    # ---------------------------------------------------------
    # 3. 그래프 그리기
    # ---------------------------------------------------------
    fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(10, 12), sharex=True)

    # === [상단 그래프] EV 스케줄링 시각화 (개선된 버전) ===
    ax1.set_title("EV Charging Request (Parking Window vs Required Time)")
    ax1.set_ylabel("EV ID")
    ax1.grid(True, linestyle='--', alpha=0.5)
    ax1.set_ylim(0, len(evs) + 1)
    
    for ev in evs:
        # 주차 구간 (회색 점선)
        ax1.hlines(ev.id, ev.arrival, ev.departure, colors='gray', linestyles='dotted', linewidth=1.5)
        # 도착 및 출차 포인트
        ax1.plot(ev.arrival, ev.id, 'go', markersize=6, zorder=3)
        ax1.plot(ev.departure, ev.id, 'rx', markersize=8, markeredgewidth=2, zorder=3)
        
        # 최소 충전 시간 막대 (6.6kW 기준)
        min_duration = ev.energy / 6.6
        ax1.barh(ev.id, min_duration, left=ev.arrival, height=0.45, 
                 color='dodgerblue', alpha=0.6, align='center', edgecolor='blue')
        
        # 라벨 (에너지량)
        ax1.text(ev.arrival, ev.id + 0.3, f"{ev.energy:.1f}kWh", 
                 fontsize=8, color='darkblue', fontweight='bold')

    # 상단 범례
    from matplotlib.lines import Line2D
    from matplotlib.patches import Patch
    legend_elements = [
        Line2D([0], [0], color='green', marker='o', lw=0, label='Arrival'),
        Line2D([0], [0], color='red', marker='x', lw=0, label='Departure'),
        Patch(facecolor='dodgerblue', edgecolor='blue', alpha=0.6, label='Min Charge Time (@6.6kW)')
    ]
    ax1.legend(handles=legend_elements, loc='upper right', fontsize='small')

    # === [하단 그래프] DBF vs Variable SBF ===
    ax2.set_title("Demand Bound vs Variable Supply Bound")
    ax2.set_xlabel("Time (t)")
    ax2.set_ylabel("Energy (kWh)")
    ax2.grid(True, which='both', linestyle='--')
    
    # DBF & SBF 플롯
    ax2.plot(time_steps, dbf_values, color='blue', linewidth=2, label='Demand (DBF)')
    ax2.plot(time_steps, sbf_values, color='red', linestyle='--', linewidth=2, label='Supply (SBF)')
    
    # 공급 용량 변경 지점 표시 (세로선)
    prev_time = 0
    for end_time, capacity in SUPPLY_PROFILE:
        # 현재 구간의 시작점(이전 끝점)에 텍스트 표시
        mid_point = (prev_time + (end_time if end_time else time_limit)) / 2
        ax2.text(min(mid_point, time_limit-1), max(sbf_values)/10, f"Cap: {capacity}", 
                 color='red', alpha=0.5, ha='center', fontsize=8)
        
        if end_time is not None and end_time <= time_limit:
            ax2.axvline(x=end_time, color='orange', linestyle=':', alpha=0.8)
            prev_time = end_time

    # 오버로드(Overload) 구간 색칠
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


