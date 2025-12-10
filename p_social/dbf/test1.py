import pandas as pd

def simulate_ev_arrivals(file_path):
    # 1. CSV 파일 읽기
    try:
        df = pd.read_csv(file_path)
    except FileNotFoundError:
        print("파일을 찾을 수 없습니다.")
        return

    # 2. 시뮬레이션 범위 설정 (0부터 가장 늦은 도착 시간까지)
    max_arrival_time = df['Arrival'].max()

    print("=== EV 도착 시뮬레이션 시작 ===")

    # 3. Time을 0부터 max_arrival_time까지 1씩 증가
    for current_time in range(max_arrival_time + 1):
        
        # 현재 시간에 도착한 EV 필터링
        arriving_evs = df[df['Arrival'] == current_time]
        
        # 도착한 EV가 있다면 정보 출력
        if not arriving_evs.empty:
            print(f"\n[Time: {current_time}]")
            for index, row in arriving_evs.iterrows():
                print(f"  ->  EV 도착: ID={int(row['ID'])}, "
                      f"필요 에너지={row['Energy']:.2f}, "
                      f"출발 예정={int(row['Departure'])}")
        
        # 실제 시뮬레이션처럼 보이게 하려면 아래 주석을 해제하여 딜레이를 줄 수 있습니다.
        # import time
        # time.sleep(1) 

    print("\n=== 모든 EV 도착 완료 ===")

# 코드 실행
simulate_ev_arrivals('/users/jaewoo/data/ev/spc/ev_jobs.csv')