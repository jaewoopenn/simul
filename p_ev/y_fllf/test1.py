'''
Created on 2026. 1. 22.

@author: jaewoo
'''
import random
import pandas as pd

def simplified_sllf_step(active_evs, total_capacity, max_rate=1.0):
    """
    한 타임 스텝에서의 sLLF 전력 분배 로직 (Water-filling)
    """
    # 1. Laxity 오름차순 정렬 (가장 급한 차가 0번 인덱스)
    sorted_evs = sorted(active_evs, key=lambda x: x['laxity'])

    # 초기화
    for ev in sorted_evs:
        ev['rate'] = 0.0
    
    remaining_power = total_capacity
    
    # 활성 EV가 없으면 리턴
    if not sorted_evs:
        return []

    # 2. Water-filling 알고리즘
    while remaining_power > 1e-6:
        # 현재 충전 속도를 반영했을 때의 '미래 Laxity' 예측
        # 미래 Laxity = 현재 Laxity - 1 + rate
        current_futures = [ev['laxity'] - 1.0 + ev['rate'] for ev in sorted_evs]
        
        # 아직 최대 속도(1.0)에 도달하지 않은 차들 중 가장 급한(미래 Laxity가 작은) 그룹 찾기
        min_future_val = min(current_futures)
        target_group = []
        
        for i, ev in enumerate(sorted_evs):
            # rate < 1.0 이고, 최소 laxity 그룹에 속하는 경우
            if ev['rate'] < max_rate and abs(current_futures[i] - min_future_val) < 1e-6:
                target_group.append(ev)
        
        if not target_group:
            break # 모든 차가 이미 풀속도(1.0)임

        # 다음 목표 레벨(Laxity) 찾기 (현재 그룹보다 바로 위의 Laxity를 가진 차)
        next_levels = [val for val in current_futures if val > min_future_val + 1e-6]
        
        if next_levels:
            gap = min(next_levels) - min_future_val
        else:
            gap = remaining_power # 더 이상 위의 차가 없으면 남은 전력 소진할 때까지

        # 이번 턴 분배량 결정
        num_active = len(target_group)
        # 그룹 내 각 차량이 더 받을 수 있는 최대치
        max_allocatable = max_rate - target_group[0]['rate'] 
        
        # 갭을 채우거나, 최대 속도까지만 채움
        fill_amount = min(gap, max_allocatable)
        
        # 총 필요 전력량
        total_needed = fill_amount * num_active
        
        if total_needed <= remaining_power:
            # 충분하면 목표만큼 다 채워줌
            for ev in target_group:
                ev['rate'] += fill_amount
            remaining_power -= total_needed
        else:
            # 부족하면 남은 전력을 공평하게 나눔 (1/N)
            share = remaining_power / num_active
            for ev in target_group:
                ev['rate'] += share
            remaining_power = 0
            
    return sorted_evs

def run_simulation(num_evs_range=(5, 10), max_time=20):
    # 1. 랜덤 시나리오 생성
    num_evs = random.randint(*num_evs_range)
    # 총 용량을 전체 차량 수의 60%로 제한하여 '경쟁' 유도
    total_capacity = num_evs * 0.6  
    
    ev_list = []
    for i in range(num_evs):
        energy = random.randint(3, 10) # 필요 에너지
        buffer = random.randint(0, 5)  # 여유 시간 (Laxity 초기값과 연관)
        deadline = energy + buffer     # Arrival은 0이므로 Deadline = Energy + Buffer
        
        ev_list.append({
            'id': f'EV{i+1}',
            'arrival': 0,
            'deadline': deadline,
            'energy_needed': float(energy),
            'initial_energy': float(energy),
            'charged': 0.0,
            'history': []
        })

    print(f"=== 시뮬레이션 시작: {num_evs}대 차량, 총 용량: {total_capacity:.2f} ===")
    print(f"{'ID':<5} {'필요에너지':<10} {'데드라인':<10} {'초기 Laxity':<10}")
    for ev in ev_list:
        laxity = ev['deadline'] - ev['energy_needed']
        print(f"{ev['id']:<5} {ev['energy_needed']:<10.1f} {ev['deadline']:<10} {laxity:<10.1f}")
    print("-" * 60)

    # 2. 시간 흐름 루프
    time_step = 0
    sim_history = []
    
    while time_step < max_time:
        # 현재 충전 가능한 차량 필터링
        active_evs_step = []
        for ev in ev_list:
            # 아직 데드라인 전이고, 에너지가 더 필요한 경우
            if time_step < ev['deadline'] and ev['energy_needed'] > 1e-6:
                remaining_time = ev['deadline'] - time_step
                current_laxity = remaining_time - ev['energy_needed']
                
                # 계산용 임시 딕셔너리 생성
                active_evs_step.append({
                    'id': ev['id'],
                    'original_ref': ev,
                    'laxity': current_laxity,
                    'rate': 0.0
                })
        
        # 종료 조건: 모든 차가 충전을 마쳤거나 데드라인을 넘김
        unfinished_future = [ev for ev in ev_list if ev['energy_needed'] > 1e-6 and ev['deadline'] > time_step]
        if not active_evs_step and not unfinished_future:
            break
        
        # 알고리즘 실행
        if active_evs_step:
            simplified_sllf_step(active_evs_step, total_capacity)
        
        # 결과 기록 및 상태 업데이트
        step_log = {'time': time_step}
        for active in active_evs_step:
            rate = active['rate']
            ev = active['original_ref']
            
            ev['energy_needed'] -= rate
            ev['charged'] += rate
            step_log[ev['id']] = rate
            
        sim_history.append(step_log)
        time_step += 1

    # 결과 출력
    df = pd.DataFrame(sim_history).set_index('time').fillna(0.0)
    print("\n[시간대별 충전 스케줄 (충전 속도)]")
    print(df.round(2))
    
    print("\n[최종 결과]")
    for ev in ev_list:
        status = "성공" if ev['energy_needed'] < 1e-6 else "실패(미완충)"
        print(f"{ev['id']}: {status} (남은 필요량: {ev['energy_needed']:.2f})")

# 시뮬레이션 실행
run_simulation()