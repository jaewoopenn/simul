import pandas as pd
import numpy as np

MAX_RATE = 6.6

def simulate_ev_demand(file_path):
    # 데이터 로드
    try:
        df = pd.read_csv(file_path)
    except FileNotFoundError:
        print("파일을 찾을 수 없습니다.")
        return

    # 초기 설정
    max_arrival_time = df['Arrival'].max()
    demand = {}  # {Deadline: Energy (개별)}

    print("=== EV Demand Vector Simulation Start ===")

    # 0부터 마지막 도착 시간까지 시뮬레이션
    for current_time in range(max_arrival_time + 1):
        
        # 1. 현재 시간에 도착한 EV 확인 및 Demand 업데이트
        arriving_evs = df[df['Arrival'] == current_time]
        
        if not arriving_evs.empty:
            for _, row in arriving_evs.iterrows():
                deadline = int(row['Departure'])
                energy = row['Energy']
                
                # 같은 마감기한이 있으면 에너지 추가, 없으면 신규 생성
                if deadline in demand:
                    demand[deadline] += energy
                else:
                    demand[deadline] = energy
            
            # Demand Vector 계산 (누적 에너지)
            # 1. Deadline 순으로 정렬
            sorted_deadlines = sorted(demand.keys())
            
            # 2. 누적 합 계산
            cumulative_demand = []
            running_total = 0.0
            for d in sorted_deadlines:
                running_total += demand[d]
                cumulative_demand.append((d, running_total))
            
            # 3. 출력
            print(f"\n[Time: {current_time}] 새로운 EV 도착")
            print("  -> Demand Vector (Deadline, Cumulative Energy):")
            print("[")
            for d, e in cumulative_demand:
                # e가 numpy.float64일 수 있으므로 float()로 변환하여 출력 포맷 지정
                print(f"  ({d}, {float(e):.2f}),")
            print("]")

    print("\n=== Simulation End ===")

# 코드 실행 (로컬 파일 경로 예시)
simulate_ev_demand('/users/jaewoo/data/ev/spc/ev_jobs.csv')
