import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches

# --- 상수 설정 ---
MAX_RATE = 6.6          # EV당 최대 충전 속도 (kW)
GRID_CAPACITY = 24      # 전체 전력망 용량 (kW)
CSV_FILE_NAME='/users/jaewoo/data/ev/spc/ev_jobs.csv'
SAVE_FILE_NAME='/users/jaewoo/data/ev/spc/results.png'
# MAX_RATE = 5          # EV당 최대 충전 속도 (kW)
# GRID_CAPACITY = 10      # 전체 전력망 용량 (kW)

def simulate_and_plot_modified(file_path):
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

    print("=== Modified Scheduling 시뮬레이션 시작 ===")

    # 3. 시간별 시뮬레이션 루프
    while current_time <= max_time_horizon:
        
        # 활성 EV 식별 (도착함 & 미완료 & 마감 전)
        active_evs = []
        for job in jobs:
            if job['remaining_energy'] <= 0.001:
                job['status'] = 'finished'
                continue
            
            if current_time >= job['departure'] and job['remaining_energy'] > 0.001:
                job['status'] = 'missed'
                continue
            
            if job['arrival'] <= current_time < job['departure']:
                active_evs.append(job)
        
        # --- [수정된 로직 적용 부분] ---
        if active_evs:
            # (1) 기본 Laxity 계산 (정렬 기준용)
            for ev in active_evs:
                time_to_deadline = ev['departure'] - current_time
                time_needed = ev['remaining_energy'] / MAX_RATE
                ev['laxity'] = time_needed/time_to_deadline 
                print(f"{ev['id']}: {ev['laxity']:.2f} {time_needed:.2f}/{time_to_deadline:d}")
            # (2) 가장 빠른 Deadline 찾기
            min_deadline = min(ev['departure'] for ev in active_evs)
            
            # (3) 그룹 분리
            earliest_group = [ev for ev in active_evs if ev['departure'] == min_deadline]
            other_group = [ev for ev in active_evs if ev['departure'] != min_deadline]
            
            # (4) 정렬: Earliest 그룹 내부도 Laxity 순, 나머지 그룹도 Laxity 순(기존 LLF 유지)
            # earliest_group.sort(key=lambda x: x['laxity'])
            other_group.sort(key=lambda x: -x['laxity'])
            
            # (5) 우선순위 통합 (Earliest 그룹 먼저)
            prioritized_evs = earliest_group + other_group
            
            # (6) 충전 수행
            current_grid_load = 0
            for ev in prioritized_evs:
                if current_grid_load >= GRID_CAPACITY:
                    break
                
                available_grid = GRID_CAPACITY - current_grid_load
                
                # 충전 속도(Rate) 결정 로직
                if ev['departure'] == min_deadline:
                    # 요청하신 로직: 남은 에너지 / 남은 시간
                    time_remaining = ev['departure'] - current_time
                    if time_remaining < 1: time_remaining = 1 # 0으로 나누기 방지
                    
                    calculated_rate = ev['remaining_energy'] / time_remaining
                    
                    # 물리적 한계(MAX_RATE) 적용
                    target_rate = min(MAX_RATE, calculated_rate)
                else:
                    # 그 외 차량은 최대 속도로 충전 (기존 방식)
                    target_rate = MAX_RATE
                
                # 최종 충전량 = min(목표속도, 남은에너지, 남은그리드용량)
                charge_amount = min(target_rate, ev['remaining_energy'], available_grid)
                
                if charge_amount > 0:
                    ev['remaining_energy'] -= charge_amount
                    ev['charged_history'].append((current_time, charge_amount))
                    current_grid_load += charge_amount
            
            # 낭비 전력 기록
            wasted = GRID_CAPACITY - current_grid_load
            grid_usage_history.append((current_time, current_grid_load, wasted))
            
        else:
            # 활성 EV 없음
            grid_usage_history.append((current_time, 0, GRID_CAPACITY))

        current_time += 1
        if current_time > max_time_horizon: break

    print("=== 시뮬레이션 완료 ===")
    # 4. 그래프 그리기 (2개 서브플롯: 위=간트차트, 아래=전력사용량)
    fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(14, 14), sharex=True, gridspec_kw={'height_ratios': [3, 1]})
    
    # --- [상단] EV 간트 차트 ---
    # sorted_jobs = sorted(jobs, key=lambda x: x['id'], reverse=True)
    sorted_jobs = sorted(jobs, key=lambda x: x['departure'], reverse=True)

    ids = [j['id'] for j in sorted_jobs]
    y_positions = range(len(ids))
    
    for i, job in enumerate(sorted_jobs):
        arrival = job['arrival']
        duration = job['departure'] - job['arrival']
        
        # 기본 배경색 (회색), 미스 발생 시 (붉은색)
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
            
            # 막대 위 충전량 텍스트
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
    
    # 미스난 차량 ID 빨간색 처리
    for tick_label, job in zip(ax1.get_yticklabels(), sorted_jobs):
        if job['remaining_energy'] > 0.01:
            tick_label.set_color('red')

    ax1.set_title('EV Charging Schedule (LLF)')
    ax1.grid(True, axis='x', linestyle='--', alpha=0.5)
    
    # 범례
    gray_patch = mpatches.Patch(color='lightgray', label='Available Window')
    blue_patch = mpatches.Patch(color='dodgerblue', label='Charging')
    red_patch = mpatches.Patch(color='#ffcccc', label='Missed Deadline')
    ax1.legend(handles=[gray_patch, blue_patch, red_patch], loc='upper right')

    # --- [하단] Grid Usage (Stacked Bar) ---
    times = [t for t, u, w in grid_usage_history]
    used = [u for t, u, w in grid_usage_history]
    wasted = [w for t, u, w in grid_usage_history]
    
    # 누적 막대 그래프 (아래: 사용량, 위: 낭비량)
    ax2.bar(times, used, width=1.0, label='Used Energy', color='dodgerblue', edgecolor='black', alpha=0.8, align='edge')
    ax2.bar(times, wasted, bottom=used, width=1.0, label='Available (Wasted)', color='lightgreen', edgecolor='black', alpha=0.4, align='edge', hatch='//')
    
    ax2.set_xlabel('Time (Hours)')
    ax2.set_ylabel('Energy (kWh)')
    ax2.set_title('Grid Energy Usage vs Available Capacity')
    ax2.set_ylim(0, GRID_CAPACITY + 5)
    ax2.axhline(y=GRID_CAPACITY, color='red', linestyle='--', linewidth=2, label='Grid Capacity Limit')
    
    # 낭비된 양 텍스트 표시 (공간 여유 있을 때만)
    for t, u, w in grid_usage_history:
        if w > 1: 
            ax2.text(t + 0.5, u + w/2, f"{w:.1f}", ha='center', va='center', fontsize=8, color='green')

    ax2.legend(loc='lower right')
    ax2.grid(True, axis='y', linestyle='--', alpha=0.5)

    plt.tight_layout()
    plt.savefig(SAVE_FILE_NAME)

# 실행
simulate_and_plot_modified(CSV_FILE_NAME)