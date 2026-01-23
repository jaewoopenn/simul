import pandas as pd
import numpy as np
import matplotlib.pyplot as plt


PATH = '/users/jaewoo/data/ev/peak/'
CSV_FILE_NAME = PATH + 'ev_jobs2.csv'
SAVE_FILE_NAME = PATH + 'demand_supply_plot.png'

# 파일 이름과 MAX_RATE 설정
MAX_RATE = 5.0
GRID_CAPACITY = 19

def plot_dbf(file_path, max_rate):
    # 1. 데이터 로드
    try:
        df = pd.read_csv(file_path)
    except FileNotFoundError:
        print(f"오류: '{file_path}' 파일을 찾을 수 없습니다.")
        return

    # 2. 시간 축 설정 (0부터 가장 늦은 데드라인 + 여유분까지)
    max_deadline = df['Departure'].max()
    t_values = np.linspace(0, max_deadline + 2, 1000)
    
    # DBF 값을 저장할 배열 초기화
    total_dbf = np.zeros_like(t_values, dtype=float)

    # 3. 각 태스크별 Demand 계산 및 합산
    # 공식:
    #   0 <= t <= (d - e_time): 0
    #   (d - e_time) < t <= d : 기울기 max_rate로 증가
    #   t > d                 : 에너지 총량(e)으로 고정
    
    for _, row in df.iterrows():
        energy = row['Energy']       # 총 에너지 요구량 (e)
        deadline = row['Departure']  # 데드라인 (d)
        
        # 최대 충전 속도(max_rate)일 때 소요되는 시간 (e_time)
        execution_time = energy / max_rate
        
        # 램프 함수가 시작되는 시점 (Latest Start Time)
        start_ramp = deadline - execution_time
        
        # 벡터화 연산을 사용하여 모든 시간 t에 대해 해당 태스크의 수요 계산
        # 로직: rate * (t - start_time)을 계산하되, 0보다 작으면 0, energy보다 크면 energy로 제한
        task_demand = np.maximum(0.0, max_rate * (t_values - start_ramp))
        task_demand = np.minimum(energy, task_demand)
        
        # 전체 DBF에 누적
        total_dbf += task_demand

    # 4. 그래프 그리기
    plt.figure(figsize=(10, 6))
    plt.plot(t_values, total_dbf, label=f'Total DBF (Max Rate={max_rate})', color='blue', linewidth=2)
    
    # 그래프 스타일 설정
    plt.xlabel('Time (t)')
    plt.ylabel('Demand (Energy)')
    plt.title('Demand Bound Function (DBF) for EV Jobs')
    plt.grid(True, linestyle='--', alpha=0.7)
    plt.legend()
    
    # 그래프 표시
    plt.show()

# 실행 예시
if __name__ == "__main__":

    
    plot_dbf(CSV_FILE_NAME, MAX_RATE)