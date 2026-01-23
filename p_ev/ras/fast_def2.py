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

# ... (Step 1, Step 2 Must Load 계산은 그대로 유지) ...

    # -----------------------------------------------------
    # Step 3. 에너지 가중치 기반 공유 (Energy-Weighted Sharing)
    # -----------------------------------------------------
    # 전략: 
    # 1. Must Load는 생존을 위한 최소한의 바닥(Floor) -> 무조건 할당
    # 2. 잉여 전력(Surplus)은 '모든' 차량에게 나누어 주되(Smoothing),
    # 3. "잔여 에너지가 많고 시간이 없는(Heavy & Urgent)" 차에 더 큰 비중을 둠
    
    total_must_load_power = sum(must_load_energy.values()) / time_step
    global_surplus_power = max(0.0, grid_capacity - total_must_load_power)
    
    final_allocations = {}
    
    # [가중치 계산]
    # 우리가 원하는 것: Laxity가 작을수록, 그리고 Remaining Energy가 클수록 급함
    # Metric: Risk Density = Remaining Energy / (Laxity + epsilon)
    # 이 값이 클수록 잉여 전력을 많이 받아야 함
    
    ev_weights = {}
    total_weight = 0.0
    
    for ev in active_evs:
        # 현재 Laxity 계산 (이번 턴에 Must Load만 받는다고 가정했을 때)
        must_p = must_load_energy[ev.ev_id] / time_step
        remaining_after_must = max(0.0, ev.remaining - must_p * time_step)
        
        # Laxity = (남은 시간) - (충전 필요 시간)
        # 남은 시간 = Deadline - (Current + Step)
        time_to_deadline = ev.deadline - (current_time + time_step)
        time_to_charge = remaining_after_must / max_ev_power
        laxity = max(0.0, time_to_deadline - time_to_charge)
        
        # 가중치 설정: (잔여 에너지) / (Laxity)
        # Laxity가 0에 가까우면 가중치가 무한대로 발산 -> 최우선 공급
        # 에너지가 클수록 가중치 증가 -> 돼지 차량 우선 공급
        weight = remaining_after_must / (laxity + 0.1)  # 0.1은 0 나누기 방지
        
        ev_weights[ev.ev_id] = weight
        total_weight += weight

    # [잉여 전력 비례 배분]
    assigned_surplus = 0.0
    
    # 1차 배분 (비례 배분)
    if total_weight > EPSILON and global_surplus_power > EPSILON:
        for ev in active_evs:
            must_p = must_load_energy[ev.ev_id] / time_step
            weight = ev_weights[ev.ev_id]
            
            # 내 지분만큼 Surplus 가져가기
            share = global_surplus_power * (weight / total_weight)
            
            # 물리적 한계 체크 (이미 Must Load는 확보했으므로 추가분만 고려)
            max_p_phys = min(max_ev_power, ev.remaining / time_step)
            room = max(0.0, max_p_phys - must_p)
            
            actual_bonus = min(share, room)
            
            final_allocations[ev.ev_id] = must_p + actual_bonus
            assigned_surplus += actual_bonus
            
        # [잔반 처리]
        # 일부 차량이 물리적 한계(max_power) 때문에 자기 몫을 다 못 먹고 남긴 전력이 있을 수 있음
        # 남은 전력(residual)은 가장 급한(가중치 높은) 순서대로 싹 긁어서 줌 (Greedy)
        residual_surplus = max(0.0, global_surplus_power - assigned_surplus)
        
        if residual_surplus > EPSILON:
            # 가중치 높은 순 정렬
            sorted_evs_risk = sorted(active_evs, key=lambda x: ev_weights[x.ev_id], reverse=True)
            
            for ev in sorted_evs_risk:
                if residual_surplus <= EPSILON: break
                
                current_p = final_allocations[ev.ev_id]
                max_p_phys = min(max_ev_power, ev.remaining / time_step)
                room = max(0.0, max_p_phys - current_p)
                
                bonus = min(residual_surplus, room)
                final_allocations[ev.ev_id] += bonus
                residual_surplus -= bonus
    else:
        # Surplus가 없거나 줄 대상이 없으면 Must Load만 할당
        for ev in active_evs:
            final_allocations[ev.ev_id] = must_load_energy[ev.ev_id] / time_step

    return [final_allocations.get(ev.ev_id, 0.0) for ev in active_evs]