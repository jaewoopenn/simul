import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches

# --- 상수 설정 ---
MAX_RATE = 6.6          # EV당 최대 충전 속도 (kW)
GRID_CAPACITY = 23      # 전체 전력망 용량 (kW)
CSV_FILE_NAME='/users/jaewoo/data/ev/spc/ev_jobs.csv'
SAVE_FILE_NAME='/users/jaewoo/data/ev/spc/results.png'
# MAX_RATE = 5          # EV당 최대 충전 속도 (kW)
# GRID_CAPACITY = 10      # 전체 전력망 용량 (kW)


def calculate_scheduling_priorities(current_time, active_evs, grid_capacity, max_rate):
    """
    수정된 로직: 
    1. Deadline 순으로 정렬 (EDF).
    2. [계획] 이전 차량 마감 이후~본인 마감 구간을 우선 활용하되, 부족 시 현재부터 충전(Smoothing).
    3. [실행] 계획된 양 우선 배분 후, 잔여 용량은 마감 임박 차량에게 추가 배정.
    """
    if not active_evs:
        return [], 0

    # 1. 마감 시간(Departure) 순으로 정렬
    sorted_evs = sorted(active_evs, key=lambda x: x['departure'])
    
    planned_rates = {}
    # 계획용 타임라인 추적 (이전 차량의 마감 시간을 저장)
    prev_deadline = current_time    
    prev_h=0
    cum_d=0
    # --- [Step 1: 계획 단계 (Planning)] ---
    for ev in sorted_evs:
        ev_id = ev['id']
        deadline = ev['departure']
        energy_needed = ev['remaining_energy']
        req=0
        sup=0 
        # 전용 구간 설정: [max(현재, 이전 차량 마감), 본인 마감]
        exclusive_start = max(current_time, prev_deadline)
        exclusive_duration = deadline - exclusive_start  
        nonexcl_duration=exclusive_start-current_time      
        duration=deadline-current_time
        if prev_h==0:
            planned_rates[ev_id]=energy_needed/duration
        else:
            
            sup=prev_h*nonexcl_duration+min(prev_h,MAX_RATE)*exclusive_duration
            req=cum_d+energy_needed-sup
            if req<=0:
                planned_rates[ev_id]=0
            else:
                planned_rates[ev_id]=req/nonexcl_duration
        print(f"{req:.2f}=  {cum_d:.2f}+{energy_needed:.2f} -{sup}")
        print(f"{duration}=  {nonexcl_duration}+{exclusive_duration}")
        print(f"{current_time} {ev_id} {planned_rates[ev_id]:.2f} {prev_h:.2f} {duration}" )
        prev_h+=planned_rates[ev_id]
        cum_d+=energy_needed
        # 다음 차량을 위해 기준 마감 시간 업데이트
        prev_deadline = max(prev_deadline, deadline)        

    # --- [Step 2: 실행 단계 (Execution)] ---
    charging_decisions = []
    current_grid_load = 0
    
    # (A) 계획된 최소 충전량만큼 1차 배분
    for ev in sorted_evs:
        target_rate = planned_rates.get(ev['id'], 0)
        # 물리적 한계, 남은 에너지, 그리드 용량을 고려하여 최종 결정
        amt = min(target_rate, max_rate, grid_capacity - current_grid_load)
        amt = max(0, amt)
        
        if amt > 0:
            charging_decisions.append({'id': ev['id'], 'charge': amt, 'ev_obj': ev})
            current_grid_load += amt

    # (B) 그리드 용량이 남는 경우, Earliest Deadline 차량부터 추가 배분
    # remaining_grid = grid_capacity - current_grid_load
    # if remaining_grid > 0:
    #     # 이미 결정된 목록을 관리하기 위한 맵
    #     decision_map = {d['id']: d for d in charging_decisions}
    #
    #     for ev in sorted_evs:
    #         if remaining_grid <= 0: break
    #
    #         current_alloc = decision_map[ev['id']]['charge'] if ev['id'] in decision_map else 0
    #         # 추가 가능한 최대치 = min(MAX_RATE, 남은에너지) - 이미 계획 배정된 양
    #         limit = min(max_rate, ev['remaining_energy'])
    #         possible_extra = limit - current_alloc
    #
    #         if possible_extra > 0:
    #             extra = min(possible_extra, remaining_grid)
    #             if ev['id'] in decision_map:
    #                 decision_map[ev['id']]['charge'] += extra
    #             else:
    #                 new_dec = {'id': ev['id'], 'charge': extra, 'ev_obj': ev}
    #                 charging_decisions.append(new_dec)
    #                 decision_map[ev['id']] = new_dec
    #
    #             current_grid_load += extra
    #             remaining_grid -= extra

    return charging_decisions, current_grid_load

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
        active_evs = [job for job in jobs if job['arrival'] <= current_time < job['departure'] and job['remaining_energy'] > 0.001]
        
        # 분리된 로직 함수 호출
        decisions, total_load = calculate_scheduling_priorities(current_time, active_evs, GRID_CAPACITY, MAX_RATE)
        
        # 결과 반영
        for action in decisions:
            ev = action['ev_obj']
            amount = action['charge']
            ev['remaining_energy'] -= amount
            ev['charged_history'].append((current_time, amount))
            
        # 낭비 전력 기록
        wasted = GRID_CAPACITY - total_load
        grid_usage_history.append((current_time, total_load, wasted))
        
        current_time += 1
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