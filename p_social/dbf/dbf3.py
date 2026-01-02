import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from scipy.integrate import cumulative_trapezoid

PATH = '/users/jaewoo/data/ev/peak/'
CSV_FILE_NAME = PATH + 'ev_jobs.csv'
# CSV_FILE_NAME = PATH + 'ev_jobs2.csv'
SAVE_FILE_NAME = PATH + 'demand_supply_plot.png'

# 파일 이름과 MAX_RATE 설정
MAX_RATE = 5.0
GRID_CAPACITY = 19

def plot_dbf_sbf(file_path, max_rate, grid_capacity):
    # 1. 데이터 로드
    try:
        df = pd.read_csv(file_path)
    except FileNotFoundError:
        print("파일을 찾을 수 없습니다.")
        return

    # 2. 시간 축 설정
    max_deadline = df['Departure'].max()
    t_values = np.linspace(0, max_deadline + 2, 1000)

    # --- DBF 계산 ---
    total_dbf = np.zeros_like(t_values, dtype=float)
    for _, row in df.iterrows():
        energy = row['Energy']
        deadline = row['Departure']
        
        duration = energy / max_rate
        start_ramp = deadline - duration
        
        # 램프 함수 적용 및 합산
        task_demand = np.minimum(energy, np.maximum(0.0, max_rate * (t_values - start_ramp)))
        total_dbf += task_demand

    # --- SBF 계산 ---
    # 각 시점별 활성 태스크(Active Task) 수 계산 (Broadcasting 활용)
    departures = df['Departure'].values[:, np.newaxis]  # (N, 1)
    t_grid = t_values[np.newaxis, :]                    # (1, M)
    
    # 활성 상태: 현재 시간이 데드라인 이전인 경우 (t < Departure)
    active_mask = departures > t_grid
    active_counts = active_mask.sum(axis=0)  # 시점별 활성 태스크 개수

    # 순간 공급률 계산: min(활성 수 * 5, 19)
    supply_rates = np.minimum(active_counts * max_rate, grid_capacity)

    # 공급률을 적분하여 누적 공급량(SBF) 계산
    total_sbf = cumulative_trapezoid(supply_rates, t_values, initial=0)

    # --- 그래프 그리기 ---
    plt.figure(figsize=(10, 6))
    
    plt.plot(t_values, total_dbf, label='DBF (Demand)', color='blue', linewidth=2)
    plt.plot(t_values, total_sbf, label='SBF (Supply)', color='green', linewidth=2)
    
    # 부족 구간(Risk) 하이라이트
    plt.fill_between(t_values, total_dbf, total_sbf, 
                     where=(total_dbf > total_sbf), 
                     color='red', alpha=0.1, label='Risk Area')

    plt.xlabel('Time (t)')
    plt.ylabel('Energy')
    plt.title(f'DBF vs SBF (Grid Cap={grid_capacity}, Max Rate={max_rate})')
    plt.grid(True, linestyle='--', alpha=0.7)
    plt.legend()
    plt.show()

# 실행
if __name__ == "__main__":
    plot_dbf_sbf(CSV_FILE_NAME, MAX_RATE, GRID_CAPACITY)