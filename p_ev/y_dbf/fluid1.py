import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

# --- 상수 정의 ---
ALPHA = 1.0
MAX_RATE = 5            # EV당 최대 충전 속도 (kW)
GRID_CAPACITY = 18      # 전체 전력망 용량 (kW)
PATH = '/users/jaewoo/data/ev/peak/'
CSV_FILE_NAME = PATH + 'ev_jobs.csv'
SAVE_FILE_NAME = PATH + 'results.png'

class FluidLLFScheduler:
    """
    이론적 Speedup Bound (1 + R_max/P)를 증명하기 위한 
    가장 단순한 형태의 Work-Conserving 유체 스케줄러.
    """
    def __init__(self, grid_capacity, max_rate, alpha=1.0):
        self.P_total = grid_capacity * alpha
        self.R_max = max_rate
        
    def calculate_laxity(self, job, t):
        # Laxity = (Deadline - Current Time) - (Remaining Energy / Max Rate)
        remaining_time = job['Departure'] - t
        required_time = job['RemainingEnergy'] / self.R_max
        return remaining_time - required_time

    def schedule_step(self, active_jobs, t):
        """
        단순 Greedy 전략: Laxity가 낮은 순서대로 전력을 최대(R_max)로 할당함.
        """
        if active_jobs.empty:
            return {}

        # 1. 모든 활성 작업의 Laxity 계산 및 정렬
        active_jobs = active_jobs.copy()
        active_jobs['Laxity'] = active_jobs.apply(lambda row: self.calculate_laxity(row, t), axis=1)
        
        # Laxity 오름차순 정렬 (가장 급한 차량 우선)
        sorted_jobs = active_jobs.sort_values(by='Laxity')

        rates = {idx: 0.0 for idx in active_jobs.index}
        remaining_capacity = self.P_total

        # 2. Greedy Allocation (Work-Conserving)
        for idx, job in sorted_jobs.iterrows():
            if remaining_capacity <= 0:
                break
                
            # 할당량 = min(최대 속도, 남은 에너지, 남은 그리드 용량)
            allocation = min(self.R_max, job['RemainingEnergy'], remaining_capacity)
            rates[idx] = allocation
            remaining_capacity -= allocation

        return rates

def run_simulation(data, grid_p, max_r, alpha=1.0):
    df = data.copy()
    df['RemainingEnergy'] = df['Energy']
    
    scheduler = FluidLLFScheduler(grid_p, max_r, alpha)
    time_horizon = int(df['Departure'].max())
    
    # 시각화를 위한 로그 기록
    history = {
        'time': [],
        'total_usage': [],
        'active_counts': []
    }
    
    for t in range(time_horizon):
        active_mask = (df['Arrival'] <= t) & (df['Departure'] > t) & (df['RemainingEnergy'] > 0)
        active_jobs = df[active_mask]
        
        total_power_at_t = 0
        if not active_jobs.empty:
            current_rates = scheduler.schedule_step(active_jobs, t)
            for idx, rate in current_rates.items():
                df.at[idx, 'RemainingEnergy'] -= rate
                total_power_at_t += rate
        
        # 로그 저장
        history['time'].append(t)
        history['total_usage'].append(total_power_at_t)
        history['active_counts'].append(len(active_jobs))
    
    missed_energy = df['RemainingEnergy'].sum()
    return missed_energy, history, df

def plot_results(history, grid_capacity, alpha, save_path):
    """
    시뮬레이션 결과를 시각화하여 저장함.
    """
    plt.figure(figsize=(12, 6))
    
    # 전력 사용량 그래프
    plt.subplot(2, 1, 1)
    plt.step(history['time'], history['total_usage'], where='post', label='Total Power Usage', color='blue')
    plt.axhline(y=grid_capacity * alpha, color='red', linestyle='--', label='Grid Capacity (Limit)')
    plt.title(f"Grid Power Usage Over Time (Alpha={alpha})")
    plt.ylabel("Power (kW)")
    plt.legend()
    plt.grid(True, alpha=0.3)

    # 활성 차량 수 그래프
    plt.subplot(2, 1, 2)
    plt.step(history['time'], history['active_counts'], where='post', label='Active EVs', color='green')
    plt.title("Number of Active EVs Over Time")
    plt.xlabel("Time (Hour/Step)")
    plt.ylabel("Count")
    plt.legend()
    plt.grid(True, alpha=0.3)

    plt.tight_layout()
    plt.savefig(save_path)
    print(f"Result plot saved to: {save_path}")
    plt.show()

# --- 실행부 ---
try:
    df_jobs = pd.read_csv(CSV_FILE_NAME)
    missed, history, final_df = run_simulation(df_jobs, GRID_CAPACITY, MAX_RATE, alpha=ALPHA)
    
    print("-" * 30)
    print(f"Simulation Result (Alpha={ALPHA})")
    print(f"Total Missed Energy: {missed:.2f} kWh")
    print("-" * 30)
    
    # 시각화 실행
    plot_results(history, GRID_CAPACITY, ALPHA, SAVE_FILE_NAME)

except FileNotFoundError:
    print(f"Error: CSV file not found at {CSV_FILE_NAME}. Please check the path.")
except Exception as e:
    print(f"An error occurred: {e}")