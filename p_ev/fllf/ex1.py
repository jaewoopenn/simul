import pandas as pd
import matplotlib.pyplot as plt
import copy

# ---------------------------------------------------------
# 1. 알고리즘 구현
# ---------------------------------------------------------

# (1) Optimized sLLF (Slope Sweeping) - 님의 알고리즘
def optimized_sllf_step(active_evs, total_capacity, max_rate=1.0):
    if not active_evs: return
    
    # 이벤트 생성 및 정렬
    events = []
    for ev in active_evs:
        start = ev['laxity'] - 1.0
        cap = min(max_rate, ev['energy_needed'])
        end = start + cap
        events.append((start, 1))
        events.append((end, -1))
    events.sort(key=lambda x: x[0])
    
    # 스위핑
    current_slope = 0
    current_power = 0.0
    prev_L = events[0][0]
    final_L = events[-1][0]
    
    for L, slope_change in events:
        dist = L - prev_L
        added_power = current_slope * dist
        if current_power + added_power > total_capacity + 1e-9:
            needed = total_capacity - current_power
            final_L = prev_L + (needed / current_slope) if current_slope > 0 else prev_L
            break
        current_power += added_power
        current_slope += slope_change
        prev_L = L
        
    # 할당
    for ev in active_evs:
        cap = min(max_rate, ev['energy_needed'])
        raw = final_L - (ev['laxity'] - 1.0)
        ev['rate'] = max(0.0, min(cap, raw))

# (2) EDF (Earliest Deadline First) - 비교군
def edf_step(active_evs, total_capacity, max_rate=1.0):
    # 데드라인이 빠른 순서로 정렬 (작은게 우선)
    sorted_evs = sorted(active_evs, key=lambda x: x['deadline'])
    remaining = total_capacity
    
    for ev in sorted_evs:
        # 줄 수 있는 만큼 다 줌 (Greedy)
        needed = min(max_rate, ev['energy_needed'])
        given = min(needed, remaining)
        ev['rate'] = given
        remaining -= given

# ---------------------------------------------------------
# 2. 시뮬레이션 실행기
# ---------------------------------------------------------
def run_comparison():
    # 시나리오: 3대의 차가 동시에 도착, 전력은 부족함
    # EV1: 아주 급함 (Deadline 5)
    # EV2: 조금 급함 (Deadline 7)
    # EV3: 덜 급함   (Deadline 9)
    # 전력: 1.5 (동시에 1.5대 분량만 충전 가능, 즉 모두 풀충전 불가)
    
    base_evs = [
        {'id': 'EV1', 'deadline': 5, 'energy_needed': 4.0, 'color': 'red'},
        {'id': 'EV2', 'deadline': 7, 'energy_needed': 4.0, 'color': 'green'},
        {'id': 'EV3', 'deadline': 9, 'energy_needed': 4.0, 'color': 'blue'}
    ]
    capacity = 1.5
    max_time = 10
    
    history = {'sLLF': [], 'EDF': []}
    
    for algo_name, algo_func in [('sLLF', optimized_sllf_step), ('EDF', edf_step)]:
        evs = copy.deepcopy(base_evs)
        
        for t in range(max_time):
            # Active Check
            active = []
            for ev in evs:
                if t < ev['deadline'] and ev['energy_needed'] > 1e-4:
                    ev['laxity'] = (ev['deadline'] - t) - ev['energy_needed']
                    active.append(ev)
            
            # Run Algorithm
            if active:
                algo_func(active, capacity)
            
            # Record
            step_data = {'time': t}
            for ev in evs:
                # 할당된 rate 기록 (없으면 0)
                rate = ev.get('rate', 0.0)
                step_data[ev['id']] = rate
                # 에너지 차감
                if t < ev['deadline']:
                    ev['energy_needed'] -= rate
                ev['rate'] = 0.0 # 리셋
            history[algo_name].append(step_data)

    return history

# ---------------------------------------------------------
# 3. 결과 시각화
# ---------------------------------------------------------
data = run_comparison()

fig, axes = plt.subplots(1, 2, figsize=(12, 5), sharey=True)

for i, (algo, steps) in enumerate(data.items()):
    df = pd.DataFrame(steps).set_index('time')
    # 스택드 바 차트 (누가 전력을 가져갔는지)
    df.plot(kind='bar', stacked=True, ax=axes[i], color=['red', 'green', 'blue'], alpha=0.7)
    
    axes[i].set_title(f"Algorithm: {algo}")
    axes[i].set_xlabel("Time Step")
    axes[i].set_ylim(0, 2.0)
    axes[i].axhline(y=1.5, color='black', linestyle='--', label='Max Capacity (1.5)')
    axes[i].legend(loc='upper right')

axes[0].set_ylabel("Charging Rate (kW)")
plt.tight_layout()
plt.show()

# 최종 상태 텍스트 출력
print("=== 최종 결과 비교 (누가 살았나?) ===")
# (간단히 시뮬레이션 다시 돌려서 잔여량 확인 로직 생략, 그래프로 확인 가능)