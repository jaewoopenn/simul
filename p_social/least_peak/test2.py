import numpy as np
import matplotlib.pyplot as plt

class EV:
    def __init__(self, id, arrival_e, deadline, max_rate=1.0):
        self.id = id
        self.e = arrival_e  # 남은 충전 필요량 (kWh)
        self.d = deadline   # 마감 시간 (Time Step)
        self.max_rate = max_rate
        self.history = []
        self.e_history = []

def minimax_plus_greedy(evs, m, current_time):
    """
    1단계: 이분 탐색으로 비율 K를 맞춤 (Minimax)
    2단계: 남는 전력이 있다면 e_i/(d_i-l) 비율이 높은 순으로 Greedy하게 추가 배분
    """
    n = len(evs)
    if n == 0: return np.array([])
    
    remains = np.array([ev.e for ev in evs])
    durations = np.array([max(1, ev.d - current_time) for ev in evs])
    
    # --- Step 1: Minimax (이분 탐색) ---
    low, high = -10.0, 10.0
    for _ in range(50):
        mid = (low + high) / 2
        x = np.clip(remains - mid * durations, 0, 1)
        if np.sum(x) > m: low = mid
        else: high = mid
    
    x_minimax = np.clip(remains - high * durations, 0, 1)
    
    # --- Step 2: Greedy Allocation (잔여 전력 소진) ---
    remaining_m = m - np.sum(x_minimax)
    if remaining_m > 1e-6:
        # 긴급도(필요 속도)가 높은 순서대로 정렬
        urgency = remains / durations
        idx_sorted = np.argsort(urgency)[::-1]
        
        for i in idx_sorted:
            if remaining_m <= 0: break
            current_x = x_minimax[i]
            # 추가로 줄 수 있는 양: (1.0 또는 남은 에너지) 중 최소값 - 이미 준 값
            can_give = min(1.0, remains[i]) - current_x
            add_x = min(remaining_m, max(0, can_give))
            x_minimax[i] += add_x
            remaining_m -= add_x
            
    return x_minimax

def sllf_allocation(evs, m, current_time):
    """
    SLLF 구현: Laxity에 따라 충전 속도를 0~1 사이로 Smoothing
    f(Laxity) = 1.0 (L < 0), 1 - L/Threshold (0 <= L <= Threshold), 0 (L > Threshold)
    """
    n = len(evs)
    if n == 0: return np.array([])
    
    # 1. 각 EV의 Laxity 계산
    laxities = np.array([(ev.d - current_time) - (ev.e / ev.max_rate) for ev in evs])
    threshold = 5.0 # Smoothing 구간 (Laxity가 5 이하로 떨어지면 충전 시작)
    
    # 2. Smoothing Function 적용 (충전 요구 지수)
    desired_rates = []
    for L in laxities:
        if L <= 0:
            rate = 1.0
        elif L >= threshold:
            rate = 0.05 # 최소 유지 전류 (완전 끄지 않음)
        else:
            rate = 1.0 - (L / threshold) # 선형적으로 증가
        desired_rates.append(rate)
    
    desired_rates = np.array(desired_rates)
    
    # 3. 가용 전력 m에 맞춰 스케일링 (전력 제한 준수)
    sum_desired = np.sum(desired_rates)
    if sum_desired > m:
        # 요구량 합이 m보다 크면 비율대로 깎음
        final_x = desired_rates * (m / sum_desired)
    else:
        # 요구량 합이 m보다 작으면 그대로 주거나 남는 전력을 분배 (여기선 그대로 할당)
        final_x = desired_rates
        
    # 4. 개별 EV의 물리적 한계(max_rate=1, 남은 에너지) 체크
    for i in range(n):
        final_x[i] = min(final_x[i], 1.0, evs[i].e)
        
    return final_x

# --- 시뮬레이션 환경 ---
def run_sim(algo_func, m_limit):
    evs = [EV(1, 8.0, 12), EV(2, 6.0, 15), EV(3, 10.0, 14)]
    for t in range(20):
        active = [ev for ev in evs if ev.e > 0.01 and ev.d > t]
        if not active: 
            for ev in evs: ev.history.append(0)
            continue
            
        rates = algo_func(active, m_limit, t)
        
        active_ids = [ev.id for ev in active]
        for ev in evs:
            if ev.id in active_ids:
                idx = active_ids.index(ev.id)
                r = rates[idx]
                ev.e -= r
                ev.history.append(r)
            else:
                ev.history.append(0)
            ev.e_history.append(max(0, ev.e))
    return evs

m_val = 1.5
res_mini = run_sim(minimax_plus_greedy, m_val)
res_sllf = run_sim(sllf_allocation, m_val)

# 시각화
fig, ax = plt.subplots(2, 2, figsize=(15, 10))

for i, (res, title) in enumerate([(res_mini, "Minimax + Greedy"), (res_sllf, "SLLF (Smoothed)")]):
    # Rate 그래프
    for ev in res:
        ax[0, i].plot(ev.history, label=f'EV {ev.id} Rate', marker='o', markersize=3)
    ax[0, i].set_title(title + " - Charging Rate")
    ax[0, i].legend()
    
    # 남은 에너지 그래프
    for ev in res:
        ax[1, i].plot(ev.e_history, label=f'EV {ev.id} Energy', linestyle='--')
    ax[1, i].set_title(title + " - Remaining Energy")
    ax[1, i].legend()

plt.tight_layout()
plt.show()