def calculate_power(current_time, active_evs, grid_capacity, max_ev_power, time_step):
    """
    Simplified Robust Abstract Scheduler (S-RAS)
    - 목적: Feasibility Optimal 보장 + 미래 병목(Bottleneck) 선제적 제거
    - 전략: 
      1. Backward Analysis로 '지금 당장 안 하면 죽는 양(Must Load)' 계산
      2. 남는 용량(Surplus)은 EDF(마감 임박) 순으로 '꽉 채워' 할당하여 미래 용량 확보
    """
    if not active_evs:
        return []

    EPSILON = 1e-6
    
    # -----------------------------------------------------
    # Step 1. 구간(Segment) 생성 (Time Discretization)
    # -----------------------------------------------------
    # 첫 번째 구간은 '현재 제어 주기(TimeStep)'로 고정하여 정밀도 확보
    segments = []
    
    # [Segment 0]: 현재 제어 주기 (이곳에 할당됨 = Must Load)
    first_seg_end = current_time + time_step
    segments.append({
        "start": current_time, 
        "end": first_seg_end, 
        "capacity": grid_capacity * time_step, # Energy
        "index": 0
    })
    
    # [Segment 1~K]: 그 이후의 데드라인들
    # 데드라인 정렬 및 필터링 (현재 주기 이후인 것만)
    deadlines = sorted(list(set(ev.deadline for ev in active_evs)))
    deadlines = [d for d in deadlines if d > first_seg_end]
    
    start_t = first_seg_end
    for d in deadlines:
        duration = d - start_t
        if duration > EPSILON:
            segments.append({
                "start": start_t, 
                "end": d, 
                "capacity": grid_capacity * duration, # Energy
                "index": 1 # 0이 아니면 미래 구간임
            })
            start_t = d

    # -----------------------------------------------------
    # Step 2. 해석적 역방향 분석 (Analytic Backward Filling)
    # -----------------------------------------------------
    # 목적: 각 차량별 'Must Energy(지금 당장 충전해야 하는 최소량)' 도출
    # 전략: 데드라인이 늦은 차량부터, 가장 미래 구간부터 채워 넣음 (EDL 원리)
    
    # Must Load 저장소 (Energy 단위)
    must_load_energy = {ev.ev_id: 0.0 for ev in active_evs}
    
    # 역방향 채우기를 위해 데드라인이 늦은 순(내림차순) 정렬
    # (주의: 복사본을 정렬하여 원본 순서 영향 없음)
    sorted_evs_backward = sorted(active_evs, key=lambda x: x.deadline, reverse=True)
    
    for ev in sorted_evs_backward:
        energy_needed = ev.remaining
        
        # 미래 구간부터 역순으로 탐색
        for seg in reversed(segments):
            if energy_needed <= EPSILON:
                break
            
            # 내 데드라인보다 늦은 구간은 사용 불가
            if seg["start"] >= ev.deadline:
                continue
                
            # 할당 가능량 = min(필요량, 구간 잔여용량, 물리적 속도 한계)
            duration = seg["end"] - seg["start"]
            phys_limit = max_ev_power * duration
            
            fill = min(energy_needed, seg["capacity"], phys_limit)
            
            if fill > EPSILON:
                seg["capacity"] -= fill
                energy_needed -= fill
                
                # 핵심: '첫 번째 구간(index 0)'에 할당된 양이 바로 Must Load
                if seg["index"] == 0:
                    must_load_energy[ev.ev_id] += fill

    # -----------------------------------------------------
    # Step 3. 하이브리드 배분 (Hybrid Allocation)
    # -----------------------------------------------------
    # 전략: 
    # 1. Must Load (생존) 확보
    # 2. 남는 전력(Surplus)은 '여유 시간(Laxity)이 부족한 순'으로 배분
    #    -> EDF의 맹점(Long Deadline, High Energy 방치)을 해결
    
    total_must_load_power = sum(must_load_energy.values()) / time_step
    global_surplus_power = max(0.0, grid_capacity - total_must_load_power)
    
    final_allocations = {}
    
    # [변경점] 정렬 기준을 Deadline -> Laxity로 변경
    # Laxity = (Deadline - CurrentTime) - (RemainingEnergy / MaxPower)
    # Laxity가 작을수록 위험한 차량임 -> Surplus를 먼저 줘서 안전하게 만듦
    def calculate_laxity(ev):
        time_to_deadline = ev.deadline - current_time
        time_to_charge = ev.remaining / max_ev_power
        return time_to_deadline - time_to_charge

    # Laxity 오름차순 (작은게 급한것) 정렬
    sorted_evs_hybrid = sorted(active_evs, key=lambda x: calculate_laxity(x))
    
    for ev in sorted_evs_hybrid:
        # 3.1. 기본 할당: Must Load
        must_p = must_load_energy[ev.ev_id] / time_step
        
        # 3.2. 보너스 할당 가능량
        max_p_phys = min(max_ev_power, ev.remaining / time_step)
        room_for_bonus = max(0.0, max_p_phys - must_p)
        
        # 3.3. Surplus 지급 (Laxity 낮은 순)
        bonus_p = min(global_surplus_power, room_for_bonus)
        
        # 3.4. 최종 할당
        total_p = must_p + bonus_p
        final_allocations[ev.ev_id] = total_p
        
        global_surplus_power = max(0.0, global_surplus_power - bonus_p)

    return [final_allocations.get(ev.ev_id, 0.0) for ev in active_evs]