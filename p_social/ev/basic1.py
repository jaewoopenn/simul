'''
Created on 2025. 11. 28.

@author: jaewoo
'''
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import datetime

# ---------------------------------------------------------
# 1. 설정 (Configuration)
# ---------------------------------------------------------
MAX_CHARGING_RATE = 6.6  # kW (일반적인 완속 충전기 속도)
TIME_INTERVAL = 5        # 분 단위 (5분 간격)
SIMULATION_DAYS = 1      # 시뮬레이션 기간 (일)

# ---------------------------------------------------------
# 2. 데이터 로드 또는 생성 함수
# ---------------------------------------------------------
def get_ev_data():
    """
    실제 ACN-Data JSON 파일이 있다면 이 함수를 수정하여 로드하세요.
    지금은 테스트를 위해 가상의 데이터를 생성합니다.
    """
    # --- [실제 데이터 사용 시] ---
    # import json
    # with open('acn_data.json', 'r') as f:
    #     data = json.load(f)
    # df = pd.DataFrame(data['_items'])
    # # 필요한 전처리 수행...
    
    # --- [가상 데이터 생성: 아침 8~9시에 몰리는 패턴] ---
    np.random.seed(42)
    n_evs = 50
    
    # 도착 시간: 오전 8시(8.0)를 중심으로 정규분포 생성
    arrival_hours = np.random.normal(loc=8.5, scale=1.0, size=n_evs)
    arrival_hours = np.clip(arrival_hours, 6.0, 20.0) # 06:00 ~ 20:00 사이로 제한
    
    # 충전 요구량: 평균 15kWh (약 2~3시간 충전 필요)
    energy_req = np.random.normal(loc=15.0, scale=5.0, size=n_evs)
    energy_req = np.clip(energy_req, 5.0, 60.0)

    # 데이터프레임 생성
    df = pd.DataFrame({
        'connectionTime': pd.to_datetime('2024-01-01') + pd.to_timedelta(arrival_hours, unit='h'),
        'kWhDelivered': energy_req
    })
    
    return df

# ---------------------------------------------------------
# 3. 비제어 충전(Uncoordinated) 부하 계산 로직
# ---------------------------------------------------------
def calculate_uncoordinated_load(ev_data):
    # 시뮬레이션 전체 시간 축 생성 (00:00 ~ 24:00)
    start_time = pd.to_datetime('2024-01-01 00:00:00')
    end_time = start_time + pd.Timedelta(days=SIMULATION_DAYS)
    time_index = pd.date_range(start=start_time, end=end_time, freq=f'{TIME_INTERVAL}min')
    
    # 부하를 저장할 시리즈 (초기값 0)
    total_load = pd.Series(0.0, index=time_index)

    print(f"총 {len(ev_data)}대 차량의 부하를 계산 중입니다...")

    for _, row in ev_data.iterrows():
        arrival = row['connectionTime']
        energy_needed = row['kWhDelivered']
        
        # 충전에 필요한 시간 계산 (시간 = 에너지 / 속도)
        # 예: 13.2kWh / 6.6kW = 2시간
        duration_hours = energy_needed / MAX_CHARGING_RATE
        duration = pd.Timedelta(hours=duration_hours)
        
        finish_time = arrival + duration
        
        # 해당 시간 구간에 부하(kW) 더하기
        # 도착 시간부터 충전 완료 시간까지 Max Rate로 충전한다고 가정
        mask = (total_load.index >= arrival) & (total_load.index < finish_time)
        total_load[mask] += MAX_CHARGING_RATE

    return total_load

# ---------------------------------------------------------
# 4. 실행 및 시각화
# ---------------------------------------------------------
if __name__ == "__main__":
    # 데이터 가져오기
    ev_df = get_ev_data()
    
    # 부하 계산
    load_profile = calculate_uncoordinated_load(ev_df)
    
    # 피크 부하 확인
    peak_load = load_profile.max()
    peak_time = load_profile.idxmax().strftime('%H:%M')
    print(f"\n[Result] Peak Load: {peak_load:.2f} kW at {peak_time}")

    # 그래프 그리기 (논문용 스타일)
    plt.figure(figsize=(10, 6))
    plt.plot(load_profile.index, load_profile.values, 
             label='Uncoordinated Charging (Baseline)', color='#d62728', linewidth=2)
    
    # 그래프 꾸미기
    plt.title('Baseline Load Profile (Uncoordinated Charging)', fontsize=14)
    plt.xlabel('Time of Day', fontsize=12)
    plt.ylabel('Power Demand (kW)', fontsize=12)
    plt.grid(True, linestyle='--', alpha=0.7)
    plt.legend(fontsize=12)
    
    # X축 시간 포맷팅
    import matplotlib.dates as mdates
    plt.gca().xaxis.set_major_formatter(mdates.DateFormatter('%H:%M'))
    
    # 피크 지점 표시
    plt.annotate(f'Peak: {peak_load:.1f} kW', 
                 xy=(pd.to_datetime(f'2024-01-01 {peak_time}'), peak_load), 
                 xytext=(10, 10), textcoords='offset points',
                 arrowprops=dict(arrowstyle='->', color='black'))

    plt.tight_layout()
    
    # 이미지 저장 (논문에 바로 쓸 수 있게)
    plt.savefig('baseline_load_profile.png', dpi=300)
    print("그래프가 'baseline_load_profile.png'로 저장되었습니다.")
    plt.show()