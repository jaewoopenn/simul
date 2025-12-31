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
    1단계: 이분 탐색으로 비율 K를 맞춤 (Minimax) - 단, 데드라인 차량은 강제 할당
    2단계: 남는 전력이 있다면 urgency(e_i/d_i)가 높은 순으로 Greedy하게 추가 배분
    """
    n = len(evs)
    if n == 0: return np.array([])
    
    # 데이터 배열 생성
    remains = np.array([ev.e for ev in evs])
    # 남은 시간이 1 이하라면 데드라인에 도달한 것임
    time_left = np.array([ev.d - current_time for ev in evs])
    durations = np.maximum(1, time_left)
    
    # --- [수정] Step 0: 데드라인 제약 조건 (Lower Bound) 설정 ---
    # 남은 시간이 1 이하인 경우, 물리적 한계(1.0)와 남은 양 중 작은 값을 반드시 충전해야 함
    lower_bounds = np.zeros(n)
    mask_deadline = time_left <= 1.0
    
    # 데드라인인 차량은 min(1.0, 남은 양) 만큼은 무조건 확보
    lower_bounds[mask_deadline] = np.minimum(1.0, remains[mask_deadline])
    
    # 만약 데드라인 차량들의 필수 합이 전체 전력 m보다 크다면, 
    # 어쩔 수 없이 m을 넘기게 되거나(Overload), 여기서 비율대로 줄여야 함.
    # 본 로직에서는 요청에 따라 '잔여 충전 필요량을 모두 충전'하는 것을 우선하여 lower_bounds를 유지합니다.
    
    # --- Step 1: Minimax (이분 탐색) ---
    low, high = -100.0, 100.0 # 탐색 범위 약간 확장
    
    # 이분 탐색으로 최적의 mid 값을 찾음
    # 목표: sum(x) <= m 이 되도록 하는 가장 작은 페널티(mid) 찾기
    best_x = lower_bounds.copy() # 초기값
    
    for _ in range(50):
        mid = (low + high) / 2
        
        # 수식: remains - mid * durations
        # 핵심 수정: 클리핑의 하한선을 0이 아닌 lower_bounds로 설정
        raw_val = remains - mid * durations
        x = np.clip(raw_val, lower_bounds, 1.0)
        
        if np.sum(x) > m:
            # 전력을 너무 많이 씀 -> mid(페널티)를 높여서 줄여야 함
            low = mid
        else:
            # 전력이 남거나 딱 맞음 -> mid를 낮춰서 더 줄 수 있는지 확인 (혹은 이 상태 저장)
            high = mid
            best_x = x # 가능한 해 저장

    # 이분 탐색이 끝난 후, high 값을 기준으로 x_minimax 확정
    # (안전하게 best_x 대신 high 기준 재계산하되, lower_bounds는 지킴)
    x_minimax = np.clip(remains - high * durations, lower_bounds, 1.0)
    
    # --- Step 2: Greedy Allocation (잔여 전력 소진) ---
    current_sum = np.sum(x_minimax)
    remaining_m = m - current_sum
    
    # 부동소수점 오차 고려하여 0보다 크면 실행
    if remaining_m > 1e-6:
        # 긴급도(필요 속도 = 남은양 / 남은시간)가 높은 순서대로 정렬
        urgency = remains / durations
        idx_sorted = np.argsort(urgency)[::-1]
        
        for i in idx_sorted:
            if remaining_m <= 1e-6: break
            
            current_x = x_minimax[i]
            # 이미 1.0이거나 남은 양을 다 채웠다면 패스
            if current_x >= 1.0 or current_x >= remains[i]:
                continue
                
            # 추가로 줄 수 있는 양: (1.0 혹은 잔여량 중 작은 것) - 현재 할당량
            max_addable = min(1.0, remains[i]) - current_x
            
            # 실제로 주는 양: 남은 m과 줄 수 있는 양 중 작은 것
            add_x = min(remaining_m, max(0, max_addable))
            
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
    # evs = [EV(1, 8.0, 12), EV(2, 6.0, 15), EV(3, 10.0, 14)]
    evs = [EV(1, 8.0, 15), EV(2, 9.0, 18), EV(3, 8.0, 13)]
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