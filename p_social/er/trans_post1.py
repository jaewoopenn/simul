import pandas as pd

file_path = '/users/jaewoo/data/israel/simulation_input_final.csv'
output = '/users/jaewoo/data/israel/test.txt'

try:
    # CSV 파일 읽기
    df = pd.read_csv(file_path, encoding='utf-8')
    
except FileNotFoundError:
    print(f"파일을 찾을 수 없습니다: {file_path}")
    

print(df.head())

# 2. Arrival_Time을 문자열에서 날짜(datetime) 객체로 변환
df['Arrival_Time'] = pd.to_datetime(df['Arrival_Time'])
df['Deadline_Time'] = pd.to_datetime(df['Deadline_Time'])

# 3. 기준 시점 설정 (가장 빠른 날짜의 자정 00:00:00)
# .min()으로 가장 빠른 시간을 찾고, .normalize()로 시간 정보를 제거(00:00:00으로 맞춤)합니다.
base_time = df['Arrival_Time'].min().normalize()
print(f"기준 시간(0): {base_time}")


# 4. 정수 변환 (기준 시점과의 차이를 '초' 단위로 계산)
# dt.total_seconds()는 차이를 초(float)로 반환하므로 int로 변환합니다.
df['Arrival_Time'] = ((df['Arrival_Time'] - base_time).dt.total_seconds() / 60).astype(int)
df['Deadline_Time'] = ((df['Deadline_Time'] - base_time).dt.total_seconds() / 60).astype(int)

# 데이터 출력 (상위 5줄만 보고 싶다면 df.head() 사용)
print(df.head())
    
with open(output, "w", encoding='utf-8') as f:
    # DataFrame의 각 행을 반복(iterate)
    for _, row in df.iterrows():
        patient_id = row['Patient_ID']
        criticality = row['Criticality']
        current_time = row['Arrival_Time']       # 계산된 도착 시간(분)
        deadline = row['Deadline_Time'] # 계산된 마감 기한(분)
        service_time = row['Execution_Time']     # 실행 시간(정수)
        deadline_duration=deadline-current_time
        # 요청하신 포맷
        line = f"{patient_id} {criticality} {int(current_time)} {int(deadline_duration)} {int(service_time)}\n"
        f.write(line)

print(f"'{output}' 파일이 생성되었습니다.")

