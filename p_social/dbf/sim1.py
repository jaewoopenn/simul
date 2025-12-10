import pandas as pd

# 설정 상수 (이전 코드와 동일하게 유지)
MAX_RATE = 6.6
GRID_CAPACITY = 21

def simulate_llf_charging(file_path):
    # 1. 데이터 로드
    try:
        df = pd.read_csv(file_path)
    except FileNotFoundError:
        print("파일을 찾을 수 없습니다.")
        return

    # 시뮬레이션을 위한 상태 관리 리스트 생성
    # DataFrame을 직접 수정하기보다 dict 리스트로 관리하는 것이 시뮬레이션에 용이합니다.
    jobs = []
    for _, row in df.iterrows():
        jobs.append({
            'id': int(row['ID']),
            'arrival': int(row['Arrival']),
            'departure': int(row['Departure']),
            'required_energy': row['Energy'],
            'remaining_energy': row['Energy'], # 남은 충전량
            'status': 'waiting' # waiting, active, finished, missed
        })

    # 시뮬레이션 종료 시간 설정 (가장 늦은 departure)
    max_time_horizon = df['Departure'].max()
    current_time = 0

    print(f"=== LLF(Least Laxity First) Charging Simulation Start ===")
    print(f"MAX_RATE: {MAX_RATE}, GRID_CAPACITY: {GRID_CAPACITY}")

    # 모든 작업이 끝날 때까지 혹은 최대 시간까지 루프
    while current_time <= max_time_horizon:
        
        # 1. 현재 충전 가능한(Active) EV 식별
        active_evs = []
        finished_count = 0
        
        for job in jobs:
            # 완료된 작업 카운트
            if job['remaining_energy'] <= 0.001:
                job['status'] = 'finished'
                finished_count += 1
                continue
            
            # 도착했으나 아직 떠날 시간이 안 된 EV
            if job['arrival'] <= current_time < job['departure']:
                active_evs.append(job)
            
            # 마감기한을 넘긴 경우 (Missed)
            elif current_time >= job['departure'] and job['remaining_energy'] > 0.001:
                job['status'] = 'missed'

        # 모든 EV가 충전 완료되었으면 조기 종료
        if finished_count == len(jobs):
            print(f"\n[Time: {current_time}] 모든 EV 충전 완료!")
            break

        if not active_evs:
            # 충전할 차량이 없으면 시간만 흐름
            # print(f"[Time: {current_time}] Idle (충전 대기 차량 없음)")
            current_time += 1
            continue

        # 2. LLF 스케줄링: Laxity 계산 및 정렬
        # Laxity = (남은 시간) - (충전완료까지 필요한 최소 시간)
        # Laxity가 작을수록 여유가 없으므로 우선순위가 높음
        for ev in active_evs:
            time_until_deadline = ev['departure'] - current_time
            time_needed_to_charge = ev['remaining_energy'] / MAX_RATE
            
            ev['laxity'] = time_until_deadline - time_needed_to_charge
        
        # Laxity 오름차순 정렬 (작은 값이 먼저)
        active_evs.sort(key=lambda x: x['laxity'])

        # 3. 충전 수행 (Grid Capacity 내에서)
        current_grid_load = 0
        charged_info = []

        for ev in active_evs:
            # 그리드 용량이 꽉 찼으면 더 이상 충전 불가
            if current_grid_load >= GRID_CAPACITY:
                break
            
            # 이번 턴(1시간)에 충전할 양 결정
            # min(최대충전속도, 남은에너지, 남은그리드용량)
            available_grid = GRID_CAPACITY - current_grid_load
            charge_amount = min(MAX_RATE, ev['remaining_energy'], available_grid)
            
            if charge_amount > 0:
                ev['remaining_energy'] -= charge_amount
                current_grid_load += charge_amount
                
                # 출력용 정보 저장
                charged_info.append(f"ID{ev['id']}({charge_amount:.2f}k)")

        # 4. 현재 시간의 충전 결과 출력
        if charged_info:
            print(f"\n[Time: {current_time}] LLF Charging:")
            print(f"  -> Grid Load: {current_grid_load:.2f} / {GRID_CAPACITY}")
            print(f"  -> Charged EVs: {', '.join(charged_info)}")
            # 디버깅용: Laxity 정보도 보고 싶으면 아래 주석 해제
            # print(f"  -> Priorities(ID:Laxity): {[(e['id'], round(e['laxity'],2)) for e in active_evs]}")

        # 다음 시간으로 이동
        current_time += 1

    print("\n=== Simulation End ===")
    
    # 최종 결과 요약
    print("\n[Final Status]")
    missed_evs = [j['id'] for j in jobs if j['remaining_energy'] > 0.001]
    if missed_evs:
        print(f"  -> Missed Deadlines (IDs): {missed_evs}")
    else:
        print("  -> Success: 모든 EV가 마감기한 내 충전 완료")

# 실행
simulate_llf_charging('/users/jaewoo/data/ev/spc/ev_jobs.csv')
