import math

def calculate_power(current_time, active_evs, grid_capacity, max_ev_power, time_step):
    """
    FS-BC (Forward-Scan Bottleneck Clearing) Algorithm Implementation
    
    논의된 핵심 로직:
    1. Forward Scan: 현재~미래 방향으로 누적 부하율(R_k)을 계산하여 Feasibility 진단.
    2. Rule 1 (Individual Limit Defense): 밀도(Density)가 물리적 한계에 도달한 차량 우선 할당.
    3. Rule 2 (Forward EDF): 남은 전력을 EDF 순서대로 몰아주어 미래 병목 선제 제거.

    Args:
        current_time (float): 현재 시뮬레이션 시간
        active_evs (list): EV 객체 리스트 (속성: ev_id, remaining, deadline, max_power 등)
                           * ev.max_power가 없으면 max_ev_power 인자 사용
        grid_capacity (float): 충전소 전체 가용 전력 (Total Power, kW)
        max_ev_power (float): EV 개별 최대 충전 속도 (Global limit if ev.max_power not set)
        time_step (float): 제어 주기 (시간 단위, 예: 1분 = 1/60)

    Returns:
        list: 입력된 active_evs 순서와 매칭되는 할당 전력(kW) 리스트
    """
    
    # 0. 예외 처리 및 초기화
    if not active_evs:
        return []
        
    EPSILON = 1e-6
    allocations = {ev.ev_id: 0.0 for ev in active_evs}
    
    # 각 EV의 개별 물리적 한계(P_i_max) 확인 (객체에 없으면 글로벌 값 사용)
    # 또한 계산 효율을 위해 필요한 정보 미리 추출
    ev_info = []
    for ev in active_evs:
        p_max = getattr(ev, 'max_power', max_ev_power)
        # 잔여 시간이 time_step보다 작으면 즉시 처리해야 함 (0으로 나누기 방지)
        time_to_deadline = max(EPSILON, ev.deadline - current_time)
        
        ev_info.append({
            'ev': ev,
            'id': ev.ev_id,
            'rem_energy': ev.remaining,
            'deadline': ev.deadline,
            'p_max': p_max,
            'time_to_deadline': time_to_deadline
        })

    # -----------------------------------------------------
    # Step 1. EDF 정렬 (Earliest Deadline First)
    # -----------------------------------------------------
    # 데드라인 오름차순 정렬 (동일 데드라인일 경우 ID나 도착시간 등으로 2차 정렬 가능)
    sorted_evs = sorted(ev_info, key=lambda x: x['deadline'])
    
    # -----------------------------------------------------
    # Step 2. 전방향 Feasibility 스캔 (Forward Scan)
    # -----------------------------------------------------
    # 역방향(Backward) 계산 없이, 1번부터 k번까지 누적 부하율을 체크하여
    # 시스템이 감당 가능한지(Feasible) 검증합니다.
    
    accumulated_energy = 0.0
    is_feasible = True
    
    for k, info in enumerate(sorted_evs):
        accumulated_energy += info['rem_energy']
        # 현재 시점(t)부터 k번째 차량의 데드라인(d_k)까지 남은 시간
        # 주의: 모든 차량의 데드라인이 현재보다 미래라고 가정
        duration = info['time_to_deadline']
        
        # R_k(t): 상위 k개 차량을 데드라인 안에 처리하기 위한 최소 시스템 평균 전력
        required_system_rate = accumulated_energy / duration
        
        if required_system_rate > grid_capacity + EPSILON:
            # 이론적으로 스케줄링 불가능한 상태 (Infeasible)
            # 실제 구현에서는 경고를 띄우거나 최선(Best-effort)으로 처리해야 함
            # print(f"[Warning] Infeasible State at rank {k}: Required {required_system_rate:.2f} > Capacity {grid_capacity}")
            is_feasible = False
            # 여기서는 알고리즘의 로직대로 진행하되, 물리적 한계 내에서 최선을 다함
            
    # -----------------------------------------------------
    # Step 3. 선제적 자원 할당 (Bottleneck Clearing Allocation)
    # -----------------------------------------------------
    
    remaining_grid_cap = grid_capacity
    processed_ids = set()
    
    # [Rule 1] 개별 한계 방어 (Urgent Limit Defense)
    # 밀도(Density)가 개별 최대 속도(P_i_max)에 근접한 차량은 
    # EDF 순서와 상관없이 즉시 할당해야 함.
    
    for info in sorted_evs:
        density = info['rem_energy'] / info['time_to_deadline']
        
        # 밀도가 P_max에 근접하거나 초과하면 '긴급(Critical)' 상태
        # 부동소수점 오차 고려하여 약간의 마진(0.999...)을 둠, 혹은 >= 사용
        if density >= info['p_max'] - EPSILON:
            # 할당량 = min(개별최대속도, 이번 턴에 필요한 에너지/dt, 남은 그리드 용량)
            # 단, 긴급 차량이므로 그리드 용량이 허락하는 한 P_max를 줘야 함
            
            # 1. 물리적 최대치
            alloc = info['p_max']
            
            # 2. 이번 턴에 완충되는 경우 과충전 방지 (Energy -> Power)
            energy_limit = info['rem_energy'] / time_step
            alloc = min(alloc, energy_limit)
            
            # 3. 그리드 용량 한계 (시스템 과부하시 어쩔 수 없이 삭감)
            alloc = min(alloc, remaining_grid_cap)
            
            allocations[info['id']] = alloc
            remaining_grid_cap -= alloc
            processed_ids.add(info['id'])
            
            # 그리드 용량 소진 시 루프 종료 가능 (단, 다른 Critical이 있으면 문제됨 - 여기선 순차처리)
            if remaining_grid_cap <= EPSILON:
                remaining_grid_cap = 0.0
                break

    # [Rule 2] EDF 몰아주기 (Forward EDF Filling)
    # Rule 1에서 처리되지 않은 차량들에게 남은 전력을 EDF 순서대로 '꽉 채워' 할당
    # 목적: 잉여 전력을 현재 소진하여 미래 구간의 부하(Load)를 0으로 만듦 (Hole 생성)
    
    if remaining_grid_cap > EPSILON:
        for info in sorted_evs:
            if info['id'] in processed_ids:
                continue
                
            # 할당 가능한 최대 전력
            # 1. 개별 물리적 한계
            alloc = info['p_max']
            
            # 2. 과충전 방지
            energy_limit = info['rem_energy'] / time_step
            alloc = min(alloc, energy_limit)
            
            # 3. 남은 그리드 용량 (여기가 핵심: 남은 걸 다 줌)
            alloc = min(alloc, remaining_grid_cap)
            
            allocations[info['id']] = alloc
            remaining_grid_cap -= alloc
            processed_ids.add(info['id'])
            
            if remaining_grid_cap <= EPSILON:
                break
                
    # -----------------------------------------------------
    # 결과 반환 (입력 리스트 순서 유지)
    # -----------------------------------------------------
    result_powers = []
    for ev in active_evs:
        power = allocations.get(ev.ev_id, 0.0)
        result_powers.append(power)
        
    return result_powers

