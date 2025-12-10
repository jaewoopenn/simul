import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches

# --- 설정 ---
MAX_RATE = 6.6
GRID_CAPACITY = 21  # 이 값을 줄이면(예: 10) Deadline Miss가 발생합니다.

def simulate_and_plot_llf_with_misses(file_path):
    # 1. 데이터 로드
    try:
        df = pd.read_csv(file_path)
    except FileNotFoundError:
        print("파일을 찾을 수 없습니다.")
        return

    # 2. 초기화
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
    
    print("=== LLF 시뮬레이션 시작 ===")

    # 3. 시뮬레이션 루프
    while current_time <= max_time_horizon:
        
        # 활성 EV 식별 및 상태 업데이트
        active_evs = []
        
        for job in jobs:
            # 이미 완료된 경우
            if job['remaining_energy'] <= 0.001:
                job['status'] = 'finished'
                continue
            
            # Deadline Miss 체크 (시간 지남 & 에너지 남음)
            if current_time >= job['departure'] and job['remaining_energy'] > 0.001:
                job['status'] = 'missed'
                continue  # 더 이상 충전 불가하다고 가정
            
            # 충전 가능 시간대 (Arrival ~ Departure)
            if job['arrival'] <= current_time < job['departure']:
                active_evs.append(job)
        
        # 종료 조건: 모든 작업이 finished 또는 missed 상태
        non_pending = sum(1 for j in jobs if j['status'] in ['finished', 'missed'])
        if non_pending == len(jobs):
            print(f"[Time: {current_time}] 모든 EV 처리 완료. 종료.")
            break
            
        # LLF 스케줄링 및 충전
        if active_evs:
            for ev in active_evs:
                time_to_deadline = ev['departure'] - current_time
                time_needed = ev['remaining_energy'] / MAX_RATE
                ev['laxity'] = time_to_deadline - time_needed
            
            active_evs.sort(key=lambda x: x['laxity'])
            
            current_grid_load = 0
            for ev in active_evs:
                if current_grid_load >= GRID_CAPACITY:
                    break
                
                available_grid = GRID_CAPACITY - current_grid_load
                charge_amount = min(MAX_RATE, ev['remaining_energy'], available_grid)
                
                if charge_amount > 0:
                    ev['remaining_energy'] -= charge_amount
                    ev['charged_history'].append((current_time, charge_amount))
                    current_grid_load += charge_amount
        
        current_time += 1

    print("=== 그래프 생성 중 ===")

    # 4. 그래프 그리기
    fig, ax = plt.subplots(figsize=(12, 10))
    
    sorted_jobs = sorted(jobs, key=lambda x: x['id'], reverse=True)
    ids = [j['id'] for j in sorted_jobs]
    y_positions = range(len(ids))
    
    for i, job in enumerate(sorted_jobs):
        arrival = job['arrival']
        duration = job['departure'] - job['arrival']
        
        # 상태에 따른 색상 설정
        window_color = 'lightgray'
        edge_color = 'grey'
        
        # 남은 에너지가 있으면 Missed 처리 (붉은색)
        if job['remaining_energy'] > 0.01:
             window_color = '#ffcccc'  # 연한 빨강
             edge_color = 'red'

        # 배경 막대 (Available Window)
        ax.broken_barh([(arrival, duration)], (i - 0.3, 0.6), 
                       facecolors=window_color, edgecolors=edge_color, alpha=0.5)
        
        # 충전 막대 (Blue)
        charge_segments = [(t, 1) for t, amt in job['charged_history']]
        if charge_segments:
            ax.broken_barh(charge_segments, (i - 0.3, 0.6), 
                           facecolors='dodgerblue', edgecolors='white')
            
            # 충전량 텍스트
            for t, amt in job['charged_history']:
                ax.text(t + 0.5, i, f"{amt:.1f}", 
                        ha='center', va='center', color='white', fontsize=7, fontweight='bold')
        
        # Missed 텍스트 표시
        if job['remaining_energy'] > 0.01:
            ax.text(job['departure'] + 0.2, i, f"Missed: {job['remaining_energy']:.2f}kWh", 
                    va='center', color='red', fontweight='bold', fontsize=9)

    # 축 설정
    ax.set_yticks(y_positions)
    ax.set_yticklabels([f"ID {i}" for i in ids])
    
    # Y축 라벨 색상 변경 (Missed인 경우 빨강)
    for tick_label, job in zip(ax.get_yticklabels(), sorted_jobs):
        if job['remaining_energy'] > 0.01:
            tick_label.set_color('red')

    ax.set_xlabel('Time (Hours)')
    ax.set_title('EV Charging Schedule (LLF) - Red indicates Deadline Miss')
    ax.grid(True, axis='x', linestyle='--', alpha=0.5)
    
    # 범례
    gray_patch = mpatches.Patch(color='lightgray', label='Available (Success)')
    red_patch = mpatches.Patch(color='#ffcccc', label='Available (Missed)')
    blue_patch = mpatches.Patch(color='dodgerblue', label='Charging')
    ax.legend(handles=[gray_patch, red_patch, blue_patch], loc='upper right')

    plt.tight_layout()
    plt.show()

simulate_and_plot_llf_with_misses('/users/jaewoo/data/ev/spc/ev_jobs.csv')