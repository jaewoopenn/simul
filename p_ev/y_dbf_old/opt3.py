import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches

# 설정 상수
# --- 상수 설정 ---
MAX_RATE_PER_EV = 6.6          # EV당 최대 충전 속도 (kW)
GRID_CAPACITY = 29      # 전체 전력망 용량 (kW)
CSV_FILE_NAME='/users/jaewoo/data/ev/spc/ev_jobs.csv'

# MAX_RATE_PER_EV = 5          # EV당 최대 충전 속도 (kW)
# GRID_CAPACITY = 10      # 전체 전력망 용량 (kW)
# CSV_FILE_NAME='/users/jaewoo/data/ev/spc/ev_jobs4.csv'
# --- 1. sLLF Scheduler Classes (제공해주신 코드 활용) ---

class EV:
    def __init__(self, id1, arrival_time, departure_time, energy_demand, max_rate):
        self.id = id1
        self.a = arrival_time        # a_i: 도착 시간
        self.d = departure_time      # d_i: 출발 시간
        self.e_target = energy_demand # e_i: 총 요구 전력량
        self.r_bar = max_rate        # \bar{r}_i: 최대 충전 속도
        
        self.e_current = energy_demand # e_i(t): 남은 충전 요구량 (초기값은 총 요구량)
        self.charged_log = []        # 충전 기록 [(time, amount)]

    def get_laxity(self, t):
        """
        논문 식 (1) 및 Definition 4 구현:
        l_i(t) = [d_i - t]^+ - e_i(t) / r_bar_i
        """
        if t < self.a:
            return float('inf') # 아직 도착하지 않음
        
        remaining_time = max(0, self.d - t)
        
        # 0으로 나누기 방지
        if self.r_bar == 0:
            return float('inf')
            
        time_to_charge = self.e_current / self.r_bar
        
        return remaining_time - time_to_charge

class sLLF_Scheduler:
    def __init__(self, power_limit_func):
        """
        :param power_limit_func: 시간 t에 따른 전력 제한 P(t)를 반환하는 함수
        """
        self.P_t = power_limit_func
        self.evs = []

    def add_ev(self, ev):
        self.evs.append(ev)

    def calculate_rate(self, ev, L, t):
        """
        논문 식 (16) 구현:
        r*_i(t) = [r_bar_i * (L(t) - l_i(t) + 1)] bounded by [0, min(r_bar_i, e_i(t))]
        """
        l_i = ev.get_laxity(t)
        
        # 기본 계산: r_bar * (L - l_i + 1)
        raw_rate = ev.r_bar * (L - l_i + 1)
        
        # 상한선: min(최대충전속도, 남은요구량)
        upper_bound = min(ev.r_bar, ev.e_current)
        
        # Projection [.]_0^upper_bound
        return max(0, min(raw_rate, upper_bound))

    def solve_for_L(self, active_evs, t, P_limit):
        """
        논문 식 (17) 및 Algorithm 1의 2단계 구현:
        Binary Search를 사용하여 최적의 임계값 L(t) 탐색
        """
        # 목표 총 전력량 계산 (식 17 우변)
        # min(P(t), sum(min(r_bar, e(t))))
        max_possible_sum = sum(min(ev.r_bar, ev.e_current) for ev in active_evs)
        target_power = min(P_limit, max_possible_sum)

        # 이진 탐색 범위 설정
        # Laxity 주변 값에서 해가 형성되므로 범위를 laxity 기준으로 설정
        laxities = [ev.get_laxity(t) for ev in active_evs]
        
        if not laxities:
            return 0.0
            
        low = min(laxities) - 5.0  # 범위를 조금 더 넉넉하게 잡음
        high = max(laxities) + 5.0
        
        # 이진 탐색 수행 (오차 허용 범위 epsilon)
        epsilon = 1e-5
        for _ in range(100): # 최대 반복 횟수 제한
            mid_L = (low + high) / 2
            
            # 현재 L값으로 모든 EV의 충전 속도 계산 후 합산
            current_sum = sum(self.calculate_rate(ev, mid_L, t) for ev in active_evs)
            
            if abs(current_sum - target_power) < epsilon:
                return mid_L
            
            if current_sum < target_power:
                low = mid_L # 전력이 남으면 L을 높여서 충전 속도를 높임 (L이 클수록 rate 커짐)
            else:
                high = mid_L # 전력이 초과되면 L을 낮춤
                
        return (low + high) / 2

    def step(self, t):
        """
        논문 Algorithm 1: Smoothed Least-Laxity-First Step
        """
        # 1) Update set of EVs (활성 EV 식별) 
        # 현재 충전소에 있고, 충전이 덜 된 차량들
        # 도착 시간 <= 현재 시간 < 출발 시간
        active_evs = [
            ev for ev in self.evs 
            if ev.a <= t < ev.d and ev.e_current > 1e-6
        ]

        if not active_evs:
            return {}, 0.0, 0.0 # rates, used_power, wasted_power

        current_P_limit = self.P_t(t)

        # 2) Obtain L(t) that solves Eq. (17) using bisection 
        optimal_L = self.solve_for_L(active_evs, t, current_P_limit)

        # 3) Charge according to rates r*_i(t) in Eq. (16)
        scheduled_rates = {}
        total_used_power = 0.0
        
        for ev in active_evs:
            rate = self.calculate_rate(ev, optimal_L, t)
            
            # 상태 업데이트 (충전량 차감)
            # 실제로는 rate * dt 만큼 차감해야 하지만 여기서는 dt=1시간 가정
            charged_amount = rate  
            
            ev.e_current = max(0, ev.e_current - charged_amount)
            ev.charged_log.append((t, charged_amount))
            scheduled_rates[ev.id] = charged_amount
            total_used_power += charged_amount
            
        wasted_power = current_P_limit - total_used_power
        
        return scheduled_rates, total_used_power, wasted_power


# --- 2. Main Simulation & Plotting Function ---


def constant_power_limit(t):
    return GRID_CAPACITY

def run_sllf_simulation(file_path):
    # 데이터 로드
    try:
        df = pd.read_csv(file_path)
    except FileNotFoundError:
        print("파일을 찾을 수 없습니다.")
        return

    # 스케줄러 초기화
    scheduler = sLLF_Scheduler(constant_power_limit)
    
    # EV 객체 생성 및 등록
    all_evs = []
    for _, row in df.iterrows():
        ev = EV(
            id1=int(row['ID']),
            arrival_time=int(row['Arrival']),
            departure_time=int(row['Departure']),
            energy_demand=row['Energy'],
            max_rate=MAX_RATE_PER_EV
        )
        scheduler.add_ev(ev)
        all_evs.append(ev)
    
    # 시뮬레이션 루프
    max_time_horizon = df['Departure'].max()
    grid_usage_history = []
    
    print("=== sLLF (Paper Logic) Simulation Start ===")
    
    for t in range(max_time_horizon + 1):
        rates, used, wasted = scheduler.step(t)
        
        # 기록용 (활성 EV가 없어도 기록)
        if not rates:
            # 활성 EV가 없으면 사용량 0, 낭비량 = 전체 용량
            # 단, 시뮬레이션 끝난 후 빈 공간 채우기 용도
            wasted = GRID_CAPACITY
            used = 0
             
        grid_usage_history.append((t, used, wasted))
        
    print("=== Simulation Finished ===")

    # --- 3. Plotting ---
    fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(14, 14), sharex=True, gridspec_kw={'height_ratios': [3, 1]})
    
    # EV Gantt Chart
    # ID 역순 정렬 (ID 1이 맨 위에 오도록 하려면 reverse=True 안 함. 여기선 큰 ID가 위로)
    # sorted_evs = sorted(all_evs, key=lambda x: x.id, reverse=True)
    sorted_evs = sorted(all_evs, key=lambda x: x.d, reverse=True)
    ids = [ev.id for ev in sorted_evs]
    y_positions = range(len(ids))
    
    for i, ev in enumerate(sorted_evs):
        arrival = ev.a
        duration = ev.d - ev.a
        
        # 상태 색상 (미스 여부 확인)
        # 시뮬레이션 끝난 시점에 e_current가 남아있으면 미스
        is_missed = ev.e_current > 0.01
        window_color = '#ffcccc' if is_missed else 'lightgray'
        edge_color = 'red' if is_missed else 'grey'

        # 배경 바
        ax1.broken_barh([(arrival, duration)], (i - 0.3, 0.6), facecolors=window_color, edgecolors=edge_color, alpha=0.5)
        
        # 충전 바
        charge_segments = [(t, 1) for t, amt in ev.charged_log]
        if charge_segments:
            ax1.broken_barh(charge_segments, (i - 0.3, 0.6), facecolors='dodgerblue', edgecolors='white')
            # 텍스트
            for t, amt in ev.charged_log:
                if amt > 0.1: # 너무 작은 값은 표시 생략 가능
                    ax1.text(t + 0.5, i, f"{amt:.1f}", ha='center', va='center', color='white', fontsize=7, fontweight='bold')
        
        # 미스 텍스트
        if is_missed:
            ax1.text(ev.d + 0.2, i, f"Missed: {ev.e_current:.2f}", va='center', color='red', fontweight='bold', fontsize=9)

    ax1.set_yticks(y_positions)
    ax1.set_yticklabels([f"ID {i}" for i in ids])
    
    # Y축 라벨 색상 변경
    for tick_label, ev in zip(ax1.get_yticklabels(), sorted_evs):
        if ev.e_current > 0.01:
            tick_label.set_color('red')

    ax1.set_title('sLLF Scheduling (Based on Provided Code Logic)')
    ax1.grid(True, axis='x', linestyle='--', alpha=0.5)
    
    # 범례
    gray_patch = mpatches.Patch(color='lightgray', label='Available Window')
    blue_patch = mpatches.Patch(color='dodgerblue', label='Charging')
    red_patch = mpatches.Patch(color='#ffcccc', label='Missed Deadline')
    ax1.legend(handles=[gray_patch, blue_patch, red_patch], loc='upper right')

    # Grid Usage Chart
    times = [t for t, u, w in grid_usage_history]
    used = [u for t, u, w in grid_usage_history]
    wasted = [w for t, u, w in grid_usage_history]
    
    ax2.bar(times, used, width=1.0, label='Used', color='dodgerblue', edgecolor='black', alpha=0.8, align='edge')
    ax2.bar(times, wasted, bottom=used, width=1.0, label='Wasted', color='lightgreen', edgecolor='black', alpha=0.4, align='edge', hatch='//')
    
    ax2.set_xlabel('Time (Hours)')
    ax2.set_ylabel('Energy (kWh)')
    ax2.set_title('Grid Usage')
    ax2.set_ylim(0, GRID_CAPACITY + 5)
    ax2.axhline(y=GRID_CAPACITY, color='red', linestyle='--', linewidth=2)
    
    # 낭비량 텍스트
    for t, u, w in grid_usage_history:
        if w > 1: 
            ax2.text(t + 0.5, u + w/2, f"{w:.1f}", ha='center', va='center', fontsize=8, color='green')

    ax2.legend(loc='lower right')
    ax2.grid(True, axis='y', linestyle='--', alpha=0.5)

    plt.tight_layout()
    plt.show()
    # plt.savefig('/users/jaewoo/data/ev/spc/results.png')

# 실행
run_sllf_simulation(CSV_FILE_NAME)