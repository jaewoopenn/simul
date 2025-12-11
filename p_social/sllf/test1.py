import numpy as np

class EV:
    def __init__(self, id, arrival_time, departure_time, energy_demand, max_rate):
        self.id = id
        self.a = arrival_time       # a_i: 도착 시간
        self.d = departure_time     # d_i: 출발 시간
        self.e_target = energy_demand # e_i: 총 요구 전력량
        self.r_bar = max_rate       # \bar{r}_i: 최대 충전 속도
        
        self.e_current = energy_demand # e_i(t): 남은 충전 요구량 (초기값은 총 요구량)
        self.charged_log = []       # 충전 기록

    def get_laxity(self, t):
        """
        논문 식 (1) 및 Definition 4 구현:
        l_i(t) = [d_i - t]^+ - e_i(t) / r_bar_i
        """
        if t < self.a:
            return float('inf') # 아직 도착하지 않음
        
        remaining_time = max(0, self.d - t)
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
        low = min(laxities) - 2.0
        high = max(laxities) + 2.0
        
        # 이진 탐색 수행 (오차 허용 범위 epsilon)
        epsilon = 1e-5
        for _ in range(100): # 최대 반복 횟수 제한
            mid_L = (low + high) / 2
            
            # 현재 L값으로 모든 EV의 충전 속도 계산 후 합산
            current_sum = sum(self.calculate_rate(ev, mid_L, t) for ev in active_evs)
            
            if abs(current_sum - target_power) < epsilon:
                return mid_L
            
            if current_sum < target_power:
                low = mid_L # 전력이 남으면 L을 높여서 충전 속도를 높임
            else:
                high = mid_L # 전력이 초과되면 L을 낮춤
                
        return (low + high) / 2

    def step(self, t):
        """
        논문 Algorithm 1: Smoothed Least-Laxity-First
        """
        # 1) Update set of EVs (활성 EV 식별) 
        # 현재 충전소에 있고, 충전이 덜 된 차량들
        active_evs = [
            ev for ev in self.evs 
            if ev.a <= t < ev.d and ev.e_current > 1e-6
        ]

        if not active_evs:
            return {}

        current_P_limit = self.P_t(t)

        # 2) Obtain L(t) that solves Eq. (17) using bisection 
        optimal_L = self.solve_for_L(active_evs, t, current_P_limit)

        # 3) Charge according to rates r*_i(t) in Eq. (16) [cite: 165]
        scheduled_rates = {}
        for ev in active_evs:
            rate = self.calculate_rate(ev, optimal_L, t)
            
            # 상태 업데이트 (충전량 차감)
            ev.e_current = max(0, ev.e_current - rate)
            ev.charged_log.append((t, rate))
            scheduled_rates[ev.id] = rate
            
        return scheduled_rates

# --- 시뮬레이션 실행 예시 ---

# 전력 제한 함수 (예: 항상 10kW)
def constant_power_limit(t):
    return 10.0

# 스케줄러 생성
scheduler = sLLF_Scheduler(constant_power_limit)

# EV 생성 (ID, 도착, 출발, 요구량, 최대속도)
# 예: EV1은 0시에 도착, 10시에 떠남, 20kWh 필요, 최대 5kW 충전 가능
ev1 = EV(id=1, arrival_time=0, departure_time=10, energy_demand=20, max_rate=5)
ev2 = EV(id=2, arrival_time=2, departure_time=12, energy_demand=30, max_rate=6)

scheduler.add_ev(ev1)
scheduler.add_ev(ev2)

print(f"{'Time':<5} | {'EV1 Rate':<10} | {'EV2 Rate':<10} | {'L(t)':<10}")
print("-" * 45)

# 0~15 시간 동안 시뮬레이션
for t in range(15):
    rates = scheduler.step(t)
    
    r1 = rates.get(1, 0.0)
    r2 = rates.get(2, 0.0)
    
    # 현재 활성 차량이 있다면 L값 추정을 위해 다시 계산(출력용)
    active_evs = [ev for ev in [ev1, ev2] if ev.a <= t < ev.d and ev.e_current > 0]
    l_val = scheduler.solve_for_L(active_evs, t, 10.0) if active_evs else 0
    
    print(f"{t:<5} | {r1:<10.2f} | {r2:<10.2f} | {l_val:<10.2f}")