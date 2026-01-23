import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

PATH = '/users/jaewoo/data/ev/peak/'
CSV_FILE_NAME = PATH + 'ev_jobs.csv'
# CSV_FILE_NAME = PATH + 'ev_jobs2.csv'
SAVE_FILE_NAME = PATH + 'demand_supply_plot.png'

# 파일 이름과 MAX_RATE 설정
MAX_RATE = 5.0
GRID_CAPACITY = 18

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


def cal_sbf(df, t_values):
    max_rate = MAX_RATE
    dt = t_values[1] - t_values[0]
    
    departures = df['Departure'].values
    arrivals = df['Arrival'].values
    energies = df['Energy'].values
    
    # 에너지 총량 제한(Bound) 미리 계산 (벡터화 유지)
    # (이 부분은 고정값이므로 루프 밖에서 계산하는 것이 효율적입니다)
    t_grid = t_values[np.newaxis, :] # Shape: (1, T)
    arr_grid = arrivals[:, np.newaxis] # Shape: (N, 1)
    
    arrived_mask = (arr_grid <= t_grid)
    energy_bound = (arrived_mask * energies[:, np.newaxis]).sum(axis=0)
    
    # SBF 계산을 위한 변수 초기화
    total_sbf = np.zeros_like(t_values)
    current_sbf_val = 0.0
    
    # [핵심 변경] 마지막으로 모든 에너지가 충족된(SBF == Bound) 태스크들의 도착 시간
    # 초기값은 -1 (아직 아무것도 완료 안 됨)
    last_cleared_arrival_time = -1.0
    
    total_sbf[0] = 0.0
    
    for i in range(1, len(t_values)):
        t_curr = t_values[i]
        
        # 1. 동적 Active Count 계산
        # 조건: 
        # (1) 이미 완료된 시점 이후에 도착했고 (arrivals > last_cleared_arrival_time)
        # (2) 현재 시간 기준 도착해 있으며 (arrivals <= t_curr)
        # (3) 아직 떠나지 않음 (departures > t_curr)
        
        # 주의: 루프 안에서 마스킹을 매번 수행하므로 태스크 수가 매우 많으면 느려질 수 있음
        current_active_mask = (
            (arrivals > last_cleared_arrival_time) & 
            (arrivals <= t_curr) & 
            (departures > t_curr)
        )
        
        active_count = current_active_mask.sum()
        
        # 공급률 계산
        current_supply_rate = min(active_count * max_rate, GRID_CAPACITY)
        
        # 2. SBF 누적 및 한계 적용
        potential_next_val = current_sbf_val + (current_supply_rate * dt)
        
        # Bound와 비교 (Clamping)
        # 부동소수점 오차를 고려해 약간의 여유(epsilon)를 두거나 np.isclose 등을 고려할 수 있음
        current_bound = energy_bound[i]
        
        if potential_next_val >= current_bound:
            # 에너지가 Bound에 도달하거나 초과함 -> 모든 활성 태스크 완료
            current_sbf_val = current_bound
            
            # [핵심] 현재 시점까지 도착한 모든 태스크는 "완료" 처리
            # 이후 루프부터는 이 시간 이전에 도착한 태스크는 active_count에서 제외됨
            last_cleared_arrival_time = t_curr
            
            # (디버깅용) 완료 지점 출력
            # print(f'{i} (t={t_curr:.2f}): Fully Satisfied. Resetting active tasks.')
            
        else:
            current_sbf_val = potential_next_val
            
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
    print(f"{SAVE_FILE_NAME}으로 저장")
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
