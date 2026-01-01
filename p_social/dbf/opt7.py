import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches

# --- 상수 설정 ---
MAX_RATE = 5          # EV당 최대 충전 속도 (kW)
GRID_CAPACITY = 20      # 전체 전력망 용량 (kW)
CSV_FILE_NAME='/users/jaewoo/data/ev/spc/ev_jobs.csv'
SAVE_FILE_NAME='/users/jaewoo/data/ev/spc/results.png'
RESULTS_FN='/users/jaewoo/data/ev/spc/results.csv'
# MAX_RATE = 5          # EV당 최대 충전 속도 (kW)
# GRID_CAPACITY = 10      # 전체 전력망 용량 (kW)

def calculate_scheduling_priorities(current_time, active_evs, grid_capacity, max_rate):
    """
    Minimax + Greedy 알고리즘을 적용하여 EV 충전 스케줄링을 수행합니다.
    
    Args:
        current_time (int): 현재 시뮬레이션 시간
        active_evs (list): 현재 충전 가능한 EV 객체들의 리스트 (dict 형태 예상)
        grid_capacity (float): 전력망 전체 용량 (m)
        max_rate (float): EV 한 대의 최대 충전 속도 (물리적 한계)
        
    Returns:
        tuple: (배정된 충전량 리스트, 사용된 총 전력량)
    """
    if not active_evs:
        return [], 0

    n = len(active_evs)
    
    # 1. 데이터 추출 (List -> Numpy Array 변환)
    # EV 객체가 dict라고 가정하고 키값 접근
    ids = [ev['id'] for ev in active_evs]
    remains = np.array([ev['remaining_energy'] for ev in active_evs], dtype=float)
    departures = np.array([ev['departure'] for ev in active_evs], dtype=float)
    
    # 남은 시간 계산 (최소 1로 설정하여 0 나누기 방지)
    time_left = departures - current_time
    durations = np.maximum(1.0, time_left)

    # ---------------------------------------------------------
    # Step 0: 데드라인 제약 조건 (Lower Bound) 설정
    # ---------------------------------------------------------
    # 남은 시간이 1 이하라면, 'max_rate'와 '남은 에너지' 중 작은 값만큼은 필수로 할당
    lower_bounds = np.zeros(n)
    mask_deadline = time_left <= 1.0
    
    # 데드라인 차량의 하한선 = min(물리적한계, 잔여에너지)
    lower_bounds[mask_deadline] = np.minimum(max_rate, remains[mask_deadline])

    # ---------------------------------------------------------
    # Step 1: Minimax (이분 탐색)
    # ---------------------------------------------------------
    # 탐색 범위 설정 (전력 단위가 클 수 있으므로 범위를 넉넉하게 잡음)
    low, high = -1000.0, 1000.0
    
    # 이분 탐색 수행
    for _ in range(50):
        mid = (low + high) / 2
        
        # 공식: remains - K * duration
        raw_val = remains - mid * durations
        
        # Clip 범위: [lower_bounds, max_rate]
        # 하한선은 데드라인 보장을 위해, 상한선은 물리적 한계를 위해
        x = np.clip(raw_val, lower_bounds, max_rate)
        
        if np.sum(x) > grid_capacity:
            low = mid  # 할당량이 너무 많음 -> 페널티(mid)를 높여야 함
        else:
            high = mid # 할당 가능 -> 더 줄일 수 있는지(혹은 최적) 확인
            
    # 최적의 K(high)를 사용하여 1차 할당량 확정
    x_minimax = np.clip(remains - high * durations, lower_bounds, max_rate)
        
    # ---------------------------------------------------------
    # Step 2: Greedy Allocation (잔여 전력 소진)
    # ---------------------------------------------------------
    current_sum = np.sum(x_minimax)
    remaining_m = grid_capacity - current_sum

    # 부동소수점 오차 고려
    if remaining_m > 1e-6:
        # 긴급도(Urgency) = 남은 에너지 / 남은 시간
        urgency = remains / durations
        # 긴급도가 높은 순서대로 인덱스 정렬
        idx_sorted = np.argsort(urgency)[::-1]
        
        for i in idx_sorted:
            if remaining_m <= 1e-6: 
                break
            
            current_x = x_minimax[i]
            
            # 이 차량이 더 받을 수 있는 최대 물리적 한계
            # (max_rate를 넘을 수 없고, 남은 에너지보다 많이 받을 필요 없음)
            real_limit = min(max_rate, remains[i])
            
            # 이미 한계까지 받았다면 패스
            if current_x >= real_limit:
                continue
            
            # 추가로 줄 수 있는 양
            can_give = real_limit - current_x
            add_x = min(remaining_m, can_give)
            
            x_minimax[i] += add_x
            remaining_m -= add_x

    # ---------------------------------------------------------
    # Step 3: 결과 포맷팅 (Target Function 요구사항)
    # ---------------------------------------------------------
    charging_decisions = []
    current_grid_load = 0.0
    
    for i, val in enumerate(x_minimax):
        # 0보다 큰 충전량만 결정 목록에 포함
        if val > 1e-6:
            charging_decisions.append({
                'id': ids[i],
                'charge': float(val),  # numpy float -> python float 변환
                'ev_obj': active_evs[i] # 원본 객체 참조 유지
            })
            current_grid_load += val
            
    return charging_decisions, current_grid_load



# (가정) calculate_scheduling_priorities, GRID_CAPACITY, MAX_RATE, SAVE_FILE_NAME 등은 
# 외부에서 정의되어 있다고 가정하고 함수 내부만 수정합니다.

def simulate_and_plot_modified_with_csv(file_path, output_csv_name="charge_history.csv"):
    # 1. 데이터 로드
    try:
        df = pd.read_csv(file_path)
    except FileNotFoundError:
        print("파일을 찾을 수 없습니다.")
        return

    # 2. 시뮬레이션 초기화
    jobs = []
    for _, row in df.iterrows():
        jobs.append({
            'id': int(row['ID']),
            'arrival': int(row['Arrival']),
            'departure': int(row['Departure']),
            'required_energy': row['Energy'],
            'remaining_energy': row['Energy'],
            'charged_history': [],
            'status': 'pending'
        })
    
    max_time_horizon = df['Departure'].max()
    current_time = 0
    grid_usage_history = []  
    
    # [추가됨] 1. CSV 저장을 위한 리스트 초기화
    charge_log = []

    print("=== Modified Scheduling 시뮬레이션 시작 ===")

    # 3. 시간별 시뮬레이션 루프
    total = 0
    tot_amt = 0
    
    # 총 필요 에너지 계산
    # for job in jobs:
        # tot_amt += job['remaining_energy']
        
    while current_time <= max_time_horizon:
        active_evs = [job for job in jobs if job['arrival'] <= current_time < job['departure'] and job['remaining_energy'] > 0.001]
        
        # 분리된 로직 함수 호출 (외부 함수라고 가정)
        decisions, total_load = calculate_scheduling_priorities(current_time, active_evs, GRID_CAPACITY, MAX_RATE)
        total_load=0
        # 결과 반영
        for action in decisions:
            ev = action['ev_obj']
            amount = min(action['charge'],ev['remaining_energy'])
            tot_amt+=amount
            total_load+=amount
            ev['remaining_energy'] -= amount
            ev['charged_history'].append((current_time, amount))
            
            # [추가됨] 2. 충전 기록 수집
            # 시간, 차량ID, 충전량, 충전 후 남은 필요량 등을 기록
            charge_log.append({
                'Time': current_time,
                'EV_ID': ev['id'],
                'Charged_Amount': amount,
                'Remaining_Requirement': max(0, ev['remaining_energy']) # 음수 방지
            })
            
        # 낭비 전력 기록
        wasted = GRID_CAPACITY - total_load
        total += total_load
        grid_usage_history.append((current_time, total_load, wasted))
        
        # (로그 출력이 너무 많으면 주석 처리 가능)
        print(f"Time: {current_time}, Wasted: {wasted:.1f}")
        
        current_time += 1
        
    print(f"Total Charged: {total:.1f} / Target: {tot_amt:.1f}")
    print("=== 시뮬레이션 완료 ===")
    
    # [추가됨] 3. 수집된 기록을 DataFrame으로 변환 후 CSV 저장
    if charge_log:
        log_df = pd.DataFrame(charge_log)
        # 보기 좋게 정렬 (시간 -> ID 순)
        log_df = log_df.sort_values(by=['Time', 'EV_ID'])
        log_df.to_csv(output_csv_name, index=False)
        print(f"=== 충전 기록 저장 완료: {output_csv_name} ===")
    else:
        print("=== 충전 기록이 없습니다 (저장 건너뜀) ===")

    # 4. 그래프 그리기 (기존 코드 유지)
    fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(14, 14), sharex=True, gridspec_kw={'height_ratios': [3, 1]})
    
    # --- [상단] EV 간트 차트 ---
    sorted_jobs = sorted(jobs, key=lambda x: x['departure'], reverse=True)

    ids = [j['id'] for j in sorted_jobs]
    y_positions = range(len(ids))
    
    for i, job in enumerate(sorted_jobs):
        arrival = job['arrival']
        duration = job['departure'] - job['arrival']
        
        window_color = 'lightgray'
        edge_color = 'grey'
        if job['remaining_energy'] > 0.01:
             window_color = '#ffcccc'
             edge_color = 'red'

        # A. 대기 가능 시간 바
        ax1.broken_barh([(arrival, duration)], (i - 0.3, 0.6), 
                       facecolors=window_color, edgecolors=edge_color, alpha=0.5)
        
        # B. 실제 충전 시간 바
        charge_segments = [(t, 1) for t, amt in job['charged_history']]
        if charge_segments:
            ax1.broken_barh(charge_segments, (i - 0.3, 0.6), 
                           facecolors='dodgerblue', edgecolors='white')
            
            for t, amt in job['charged_history']:
                ax1.text(t + 0.5, i, f"{amt:.1f}", 
                        ha='center', va='center', color='white', fontsize=7, fontweight='bold')
        
        # C. 미스 텍스트 표시
        if job['remaining_energy'] > 0.01:
            ax1.text(job['departure'] + 0.2, i, f"Missed: {job['remaining_energy']:.2f}", 
                    va='center', color='red', fontweight='bold', fontsize=9)

    # Y축 설정
    ax1.set_yticks(y_positions)
    ax1.set_yticklabels([f"ID {i}" for i in ids])
    
    for tick_label, job in zip(ax1.get_yticklabels(), sorted_jobs):
        if job['remaining_energy'] > 0.01:
            tick_label.set_color('red')

    ax1.set_title('EV Charging Schedule (LLF)')
    ax1.grid(True, axis='x', linestyle='--', alpha=0.5)
    
    gray_patch = mpatches.Patch(color='lightgray', label='Available Window')
    blue_patch = mpatches.Patch(color='dodgerblue', label='Charging')
    red_patch = mpatches.Patch(color='#ffcccc', label='Missed Deadline')
    ax1.legend(handles=[gray_patch, blue_patch, red_patch], loc='upper right')

    # --- [하단] Grid Usage (Stacked Bar) ---
    times = [t for t, u, w in grid_usage_history]
    used = [u for t, u, w in grid_usage_history]
    wasted = [w for t, u, w in grid_usage_history]
    
    ax2.bar(times, used, width=1.0, label='Used Energy', color='dodgerblue', edgecolor='black', alpha=0.8, align='edge')
    ax2.bar(times, wasted, bottom=used, width=1.0, label='Available (Wasted)', color='lightgreen', edgecolor='black', alpha=0.4, align='edge', hatch='//')
    
    ax2.set_xlabel('Time (Hours)')
    ax2.set_ylabel('Energy (kWh)')
    ax2.set_title('Grid Energy Usage vs Available Capacity')
    ax2.set_ylim(0, GRID_CAPACITY + 5)
    ax2.axhline(y=GRID_CAPACITY, color='red', linestyle='--', linewidth=2, label='Grid Capacity Limit')
    
    for t, u, w in grid_usage_history:
        if w > 1: 
            ax2.text(t + 0.5, u + w/2, f"{w:.1f}", ha='center', va='center', fontsize=8, color='green')

    ax2.legend(loc='lower right')
    ax2.grid(True, axis='y', linestyle='--', alpha=0.5)

    plt.tight_layout()
    plt.savefig(SAVE_FILE_NAME)
    print(f"Graph saved to {SAVE_FILE_NAME}")

# 사용 예시:
# simulate_and_plot_modified_with_csv('ev_jobs.csv', 'results.csv')
# 실행
simulate_and_plot_modified_with_csv(CSV_FILE_NAME,RESULTS_FN)