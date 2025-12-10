import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches

# --- 상수 설정 ---
# MAX_RATE = 6.6          # EV당 최대 충전 속도 (kW)
# GRID_CAPACITY = 24      # 전체 전력망 용량 (kW)
MAX_RATE = 5          # EV당 최대 충전 속도 (kW)
GRID_CAPACITY = 10      # 전체 전력망 용량 (kW)

def simulate_and_plot_llf_wasted(file_path):
    # 1. 데이터 로드
    try:
        df = pd.read_csv(file_path)
    except FileNotFoundError:
        print("파일을 찾을 수 없습니다.")
        return

    # 2. 시뮬레이션 초기화
    # DataFrame을 딕셔너리 리스트로 변환하여 상태 관리
    jobs = []
    for _, row in df.iterrows():
        jobs.append({
            'id': int(row['ID']),
            'arrival': int(row['Arrival']),
            'departure': int(row['Departure']),
            'required_energy': row['Energy'],
            'remaining_energy': row['Energy'],
            'charged_history': [],  # (time, amount) 저장용
            'status': 'pending'     # 상태: pending, active, finished, missed
        })
    
    max_time_horizon = df['Departure'].max()
    current_time = 0
    
    # 시간대별 전력망 사용량 기록 [(time, used_energy, wasted_energy)]
    grid_usage_history = []  
    
    print("=== LLF 시뮬레이션 시작 ===")

    # 3. 시간별 시뮬레이션 루프
    while current_time <= max_time_horizon:
        
        # (1) 활성 EV 식별 및 상태 업데이트
        active_evs = []
        
        for job in jobs:
            # 이미 완료된 경우
            if job['remaining_energy'] <= 0.001:
                job['status'] = 'finished'
                continue
            
            # 마감기한 미스 체크 (시간은 지났는데 에너지가 남음)
            if current_time >= job['departure'] and job['remaining_energy'] > 0.001:
                job['status'] = 'missed'
                continue
            
            # 충전 가능 시간대 (Arrival ~ Departure) 확인
            if job['arrival'] <= current_time < job['departure']:
                active_evs.append(job)
        
        # (2) LLF 스케줄링: Laxity 계산 및 정렬
        current_grid_load = 0
        
        if active_evs:
            # Laxity = (남은 시간) - (충전완료까지 필요한 최소 시간)
            for ev in active_evs:
                time_to_deadline = ev['departure'] - current_time
                time_needed = ev['remaining_energy'] / MAX_RATE
                ev['laxity'] = time_to_deadline - time_needed
            
            # Laxity 오름차순 정렬 (급한 순서)
            active_evs.sort(key=lambda x: x['laxity'])
            
            # (3) 충전 수행
            for ev in active_evs:
                if current_grid_load >= GRID_CAPACITY:
                    break
                
                available_grid = GRID_CAPACITY - current_grid_load
                # 충전량 결정: min(최대속도, 남은필요량, 남은그리드용량)
                charge_amount = min(MAX_RATE, ev['remaining_energy'], available_grid)
                
                if charge_amount > 0:
                    ev['remaining_energy'] -= charge_amount
                    ev['charged_history'].append((current_time, charge_amount))
                    current_grid_load += charge_amount
            
            # 낭비된(남은) 전력 계산
            wasted = GRID_CAPACITY - current_grid_load
            grid_usage_history.append((current_time, current_grid_load, wasted))
            
        else:
            # 활성 EV가 없는 경우 (모두 완료되었거나 대기 중)
            # 아직 시뮬레이션 시간이 남았다면 낭비된 전력은 전체 용량과 같음
            grid_usage_history.append((current_time, 0, GRID_CAPACITY))

        current_time += 1
        
        # 모든 차량 처리가 끝났더라도 그래프를 위해 max_time_horizon까지 루프를 돌릴 수 있음
        # 여기서는 max_time_horizon까지 계속 진행

    print("=== 시뮬레이션 완료, 그래프 생성 중 ===")

    # 4. 그래프 그리기 (2개 서브플롯: 위=간트차트, 아래=전력사용량)
    fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(14, 14), sharex=True, gridspec_kw={'height_ratios': [3, 1]})
    
    # --- [상단] EV 간트 차트 ---
    sorted_jobs = sorted(jobs, key=lambda x: x['id'], reverse=True)
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
    plt.savefig('/users/jaewoo/data/ev/spc/results.png')

# 함수 실행 (경로 수정 필요)
simulate_and_plot_llf_wasted('/users/jaewoo/data/ev/spc/ev_jobs2.csv')