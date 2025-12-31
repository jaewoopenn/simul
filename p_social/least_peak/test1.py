'''
Created on 2025. 12. 20.

@author: jaewoo
'''
import numpy as np
import matplotlib.pyplot as plt

class EV:
    def __init__(self, id, arrival_e, deadline, max_rate=1.0):
        self.id = id
        self.e = arrival_e  # 남은 충전 필요량
        self.d = deadline   # 마감 시간 (타임스텝 단위)
        self.max_rate = max_rate
        self.history = []   # 충전 속도 기록
        self.completed = False

def minimax_allocation(evs, m, current_time):
    """
    제안된 방식: (e_i - x_i) / (d_i - l)의 최댓값을 최소화 (이분 탐색)
    """
    n = len(evs)
    if n == 0: return []
    
    # 각 EV의 분모 (남은 시간) 계산: d_i - current_time
    # l은 문제에서 현재 시각을 의미하므로 d_i - l
    durations = np.array([ev.d - current_time for ev in evs])
    remains = np.array([ev.e for ev in evs])
    
    # 이분 탐색으로 최적의 K (비율) 찾기
    low = -10.0 # 충분히 낮은 값
    high = 10.0 # 충분히 높은 값
    
    for _ in range(50): # 50번 반복 시 충분한 정밀도 확보
        mid = (low + high) / 2
        # x_i = e_i - mid * (d_i - l)
        x = remains - mid * durations
        x = np.clip(x, 0, 1) # 0 <= x_i <= 1 제약
        
        if np.sum(x) > m:
            low = mid # 전력을 너무 많이 씀 -> 비율 K를 높여야 함
        else:
            high = mid # 전력을 적게 씀 -> 비율 K를 낮춰야 함
            
    # 최종 K로 결정된 x_i들
    final_x = np.clip(remains - high * durations, 0, 1)
    
    # 전력이 남는 경우 처리 (Greedy하게 남는 전력 배분 가능하나 여기선 생략)
    return final_x

def sllf_allocation(evs, m, current_time):
    """
    SLLF 방식: Laxity = (남은 시간) - (남은 에너지 / max_rate)
    Laxity가 낮은 순서대로 우선순위 배분
    """
    n = len(evs)
    if n == 0: return []
    
    laxities = []
    for ev in evs:
        laxity = (ev.d - current_time) - (ev.e / ev.max_rate)
        laxities.append(laxity)
    
    # SLLF는 일반적으로 Laxity에 반비례하게 Smooth하게 할당하거나 
    # 정렬 후 Threshold 기반으로 할당함. 여기선 비교를 위해 
    # Laxity가 낮은 순서대로 전력을 우선 배분하는 방식을 채택.
    idx_sorted = np.argsort(laxities)
    x = np.zeros(n)
    remaining_m = m
    
    for i in idx_sorted:
        alloc = min(remaining_m, 1.0)
        # 만약 laxity가 너무 크면(여유가 많으면) 충전하지 않음 (SLLF의 특징)
        if laxities[i] > 5: # Threshold 예시
            alloc *= 0.2 # 최소한으로만 충전 (Smoothing)
            
        x[i] = min(alloc, evs[i].e)
        remaining_m -= x[i]
        
    return x

def run_simulation(algo_name, total_m, steps=20):
    # 시뮬레이션 환경 설정 (3대의 EV)
    # EV(id, 필요량, 마감시간)
    evs = [
        EV(1, 10.0, 15), # 빡빡한 일정
        EV(2, 5.0, 18),  # 보통
        EV(3, 8.0, 12)   # 매우 빡빡함
    ]
    
    total_charged = 0
    
    for t in range(steps):
        active_evs = [ev for ev in evs if ev.e > 0 and ev.d > t]
        if not active_evs: break
        
        if algo_name == "Minimax":
            rates = minimax_allocation(active_evs, total_m, t)
        else:
            rates = sllf_allocation(active_evs, total_m, t)
            
        for i, ev in enumerate(active_evs):
            rate = rates[i]
            actual_charge = min(rate, ev.e)
            ev.e -= actual_charge
            ev.history.append(actual_charge)
            
        # 충전하지 않은 시간은 0으로 기록
        for ev in evs:
            if ev not in active_evs:
                ev.history.append(0)

    return evs

# --- 실행 및 시각화 ---
m_limit = 1.4 # 전체 전력 제한

results_minimax = run_simulation("Minimax", m_limit)
results_sllf = run_simulation("SLLF", m_limit)

plt.figure(figsize=(12, 5))

# Minimax 결과 그래프
plt.subplot(1, 2, 1)
for ev in results_minimax:
    plt.plot(ev.history, label=f'EV {ev.id} (Rem: {ev.e:.1f})')
plt.title(f"Minimax Allocation (m={m_limit})")
plt.xlabel("Time Step")
plt.ylabel("Charging Rate (x_i)")
plt.legend()
plt.grid(True)

# SLLF 결과 그래프
plt.subplot(1, 2, 2)
for ev in results_sllf:
    plt.plot(ev.history, label=f'EV {ev.id} (Rem: {ev.e:.1f})')
plt.title(f"SLLF Allocation (m={m_limit})")
plt.xlabel("Time Step")
plt.ylabel("Charging Rate (x_i)")
plt.legend()
plt.grid(True)

plt.tight_layout()
plt.show()