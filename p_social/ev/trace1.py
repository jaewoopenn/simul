'''
Created on 2025. 11. 28.

@author: jaewoo
'''
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from datetime import datetime, timedelta

# ---------------------------------------------------------
# 1. 데이터 로드 (여기서는 예시를 위해 가상 데이터 생성)
# 실제 연구 시: pd.read_json('acn_data.json') 활용
# ---------------------------------------------------------
def generate_mock_data(n=1000):
    data = []
    # 데이터 생성 규칙: 주로 아침 8~9시, 저녁 18~19시에 몰리는 패턴 가정
    for _ in range(n):
        if np.random.rand() > 0.5:
            hour = int(np.random.normal(9, 2)) % 24 # 출근 시간대
        else:
            hour = int(np.random.normal(18, 2)) % 24 # 퇴근 시간대
        
        minute = np.random.randint(0, 60)
        arrival_time = f"2024-11-27 {hour:02d}:{minute:02d}:00"
        
        # 충전 양 (kWh) 및 체류 시간 (Minutes)
        kwh = round(np.random.normal(15, 5), 2) 
        duration = int(np.random.normal(120, 30))
        
        data.append({
            "connectionTime": arrival_time,
            "kWhDelivered": abs(kwh),
            "durationMinutes": abs(duration)
        })
    return pd.DataFrame(data)

# 데이터 프레임 생성
df = generate_mock_data(2000)

# ---------------------------------------------------------
# 2. 데이터 전처리 (시간대 추출)
# ---------------------------------------------------------
# 문자열을 datetime 객체로 변환
df['connectionTime'] = pd.to_datetime(df['connectionTime'])

# '도착 시각(Hour)' 추출 (0 ~ 23)
df['arrival_hour'] = df['connectionTime'].dt.hour

# ---------------------------------------------------------
# 3. 확률 분포 분석 및 시각화 (핵심: 논문에 들어갈 그래프)
# ---------------------------------------------------------
plt.figure(figsize=(10, 6))

# 히스토그램과 커널 밀도 추정(KDE) 그리기
sns.histplot(df['arrival_hour'], bins=24, kde=True, stat="probability", color="skyblue")

plt.title("Probability Distribution of EV Arrival Times (Simulation Input)", fontsize=14)
plt.xlabel("Hour of Day (0-23)", fontsize=12)
plt.ylabel("Probability", fontsize=12)
plt.grid(axis='y', linestyle='--', alpha=0.7)
plt.xticks(range(0, 24))

# 그래프 출력
plt.show()

# ---------------------------------------------------------
# 4. 시뮬레이터(SUMO/AnyLogic)에 넣을 입력값 추출
# ---------------------------------------------------------
# 시간대별 확률 계산 (딕셔너리 형태)
prob_distribution = df['arrival_hour'].value_counts(normalize=True).sort_index().to_dict()

print("--- 시뮬레이션 입력 파라미터 (시간대별 도착 확률) ---")
for hour in range(0, 24):
    prob = prob_distribution.get(hour, 0.0)
    print(f"[{hour}시] : {prob:.4f} ({prob*100:.2f}%)")