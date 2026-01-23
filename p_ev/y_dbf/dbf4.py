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
GRID_CAPACITY = 16

def cal_dbf(df,t_values):
    max_rate=MAX_RATE
    # --- DBF 계산 (기존 동일) ---
    total_dbf = np.zeros_like(t_values, dtype=float)
    for _, row in df.iterrows():
        energy = row['Energy']
        deadline = row['Departure']
        duration = energy / max_rate
        start_ramp = deadline - duration
        task_demand = np.minimum(energy, np.maximum(0.0, max_rate * (t_values - start_ramp)))
        total_dbf += task_demand
    return total_dbf

def cal_sbf(df,t_values):
    max_rate=MAX_RATE
    dt = t_values[1] - t_values[0]  # 시간 간격(Delta t)
    # --- SBF 계산 (순차적 로직 적용) ---
    departures = df['Departure'].values[:, np.newaxis]
    arrivals = df['Arrival'].values[:, np.newaxis]
    energies = df['Energy'].values[:, np.newaxis]
    t_grid = t_values[np.newaxis, :]
    
    # 1. 순간 공급률(Rate) 계산
    # 활성 조건: 도착함(>=) AND 아직 안 떠남(<)
    active_mask = (t_grid >= arrivals) & (t_grid < departures)
    active_counts = active_mask.sum(axis=0)
    supply_rates = np.minimum(active_counts * max_rate, GRID_CAPACITY)
    
    # 2. 에너지 총량 제한(Bound) 계산
    # 조건: 현재 시간 이전에 도착한 모든 태스크의 에너지 합
    arrived_mask = (arrivals <= t_grid)
    energy_bound = (arrived_mask * energies).sum(axis=0)
    
    # 3. SBF 순차 계산 (Iterative Calculation)
    total_sbf = np.zeros_like(t_values)
    current_sbf_val = 0.0
    
    # 초기값 설정
    total_sbf[0] = 0.0 
    
    # 루프를 돌며 이전 값(t)을 바탕으로 다음 값(t+1) 결정
    for i in range(1, len(t_values)):
        # 잠재적 SBF = 이전 시점 SBF + (현재 시점 공급률 * dt)
        potential_next_val = current_sbf_val + (supply_rates[i] * dt)
        
        # 한계 적용: 잠재적 값과 에너지 한계 중 작은 값 선택
        # 여기서 제한된(Clamped) 값이 다음 루프의 current_sbf_val이 됨
        current_sbf_val = min(potential_next_val, energy_bound[i])
        if current_sbf_val==energy_bound[i]:
            print(f'{i}:{current_sbf_val}')
        total_sbf[i] = current_sbf_val

    return total_sbf

def plot(t_values,total_dbf,total_sbf):
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
    plt.title(f'DBF vs SBF (Grid Cap={GRID_CAPACITY}, Max Rate={MAX_RATE})')
    plt.grid(True, linestyle='--', alpha=0.7)
    plt.legend()
    plt.savefig(SAVE_FILE_NAME, dpi=300)
    # plt.show()    
    
# 실행
def run():
    # 1. 데이터 로드
    try:
        df = pd.read_csv(CSV_FILE_NAME)
    except FileNotFoundError:
        print("파일을 찾을 수 없습니다.")
        return
    max_deadline = df['Departure'].max()
    t_values = np.linspace(0, max_deadline + 2, 1000)

    total_dbf=cal_dbf(df,t_values)
    total_sbf=cal_sbf(df,t_values)
    plot(t_values,total_dbf,total_sbf)    

if __name__ == "__main__":
    run()
