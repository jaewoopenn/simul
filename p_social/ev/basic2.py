import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.dates as mdates
import datetime

SAVE_DIR = '/users/jaewoo/data/acn'
OUTPUT_FIG=SAVE_DIR+'/results.png'

# ---------------------------------------------------------
# 1. 설정 (Configuration)
# ---------------------------------------------------------
MAX_CHARGING_RATE = 6.6  # kW (일반적인 완속 충전기 속도)
TIME_INTERVAL = 5        # 분 단위 (5분 간격)
CSV_FILENAME = SAVE_DIR+'/acn_data_caltech_20190901_20190907.csv' # 업로드된 파일명

# ---------------------------------------------------------
# 2. 데이터 로드 함수
# ---------------------------------------------------------
def get_ev_data():
    """
    업로드된 ACN-Data CSV 파일을 로드하고 전처리합니다.
    """
    try:
        # CSV 파일 읽기
        df = pd.read_csv(CSV_FILENAME)
        
        # 날짜/시간 컬럼을 datetime 객체로 변환 (Timezone 처리 포함)
        # format='mixed'는 다양한 날짜 형식을 유연하게 처리합니다.
        time_cols = ['connectionTime', 'disconnectTime', 'doneChargingTime']
        for col in time_cols:
            if col in df.columns:
                df[col] = pd.to_datetime(df[col], utc=True)
        
        # 필요한 경우 데이터 정렬
        df = df.sort_values(by='connectionTime')
        
        print(f"데이터 로드 성공: {len(df)}개의 충전 세션")
        print(f"데이터 기간: {df['connectionTime'].min()} ~ {df['disconnectTime'].max()}")
        return df
        
    except FileNotFoundError:
        print(f"오류: '{CSV_FILENAME}' 파일을 찾을 수 없습니다.")
        return pd.DataFrame()
    except Exception as e:
        print(f"데이터 로드 중 오류 발생: {e}")
        return pd.DataFrame()

# ---------------------------------------------------------
# 3. 비제어 충전(Uncoordinated) 부하 계산 로직
# ---------------------------------------------------------
def calculate_uncoordinated_load(ev_data):
    if ev_data.empty:
        return pd.Series()

    # 시뮬레이션 전체 시간 축 생성
    # 데이터의 시작일 00:00 부터 마지막 날 24:00 까지
    start_time = ev_data['connectionTime'].min().floor('D')
    end_time = ev_data['disconnectTime'].max().ceil('D')
    
    time_index = pd.date_range(start=start_time, end=end_time, freq=f'{TIME_INTERVAL}min')
    
    # 부하를 저장할 시리즈 (초기값 0)
    total_load = pd.Series(0.0, index=time_index)

    print(f"부하 계산 중... (기간: {start_time.date()} ~ {end_time.date()})")

    for _, row in ev_data.iterrows():
        arrival = row['connectionTime']
        energy_needed = row['kWhDelivered']
        
        # 데이터 유효성 검사
        if pd.isna(arrival) or pd.isna(energy_needed) or energy_needed <= 0:
            continue
            
        # 충전에 필요한 시간 계산 (시간 = 에너지 / 속도)
        duration_hours = energy_needed / MAX_CHARGING_RATE
        duration = pd.Timedelta(hours=duration_hours)
        
        finish_time = arrival + duration
        
        # 해당 시간 구간에 부하(kW) 더하기
        # 도착 시간부터 충전 완료 시간까지 Max Rate로 충전한다고 가정 (Uncoordinated)
        mask = (total_load.index >= arrival) & (total_load.index < finish_time)
        total_load[mask] += MAX_CHARGING_RATE

    return total_load

# ---------------------------------------------------------
# 4. 실행 및 시각화
# ---------------------------------------------------------
if __name__ == "__main__":
    # 데이터 가져오기
    ev_df = get_ev_data()
    
    if not ev_df.empty:
        # 부하 계산
        load_profile = calculate_uncoordinated_load(ev_df)
        
        if not load_profile.empty:
            # 피크 부하 확인
            peak_load = load_profile.max()
            peak_time = load_profile.idxmax().strftime('%m-%d %H:%M')
            print(f"\n[Result] Peak Load: {peak_load:.2f} kW at {peak_time}")

            # 그래프 그리기
            plt.figure(figsize=(12, 6))
            
            # 선 그래프
            plt.plot(load_profile.index, load_profile.values, 
                     label='Uncoordinated Charging (Baseline)', color='#d62728', linewidth=1.5)
            
            # 영역 채우기 (시각적 효과)
            plt.fill_between(load_profile.index, load_profile.values, color='#d62728', alpha=0.1)
            
            # 그래프 꾸미기
            plt.title(f'Baseline Load Profile ({ev_df["connectionTime"].dt.date.iloc[0]} ~ {ev_df["connectionTime"].dt.date.iloc[-1]})', fontsize=14)
            plt.xlabel('Date & Time', fontsize=12)
            plt.ylabel('Power Demand (kW)', fontsize=12)
            plt.grid(True, linestyle='--', alpha=0.7)
            plt.legend(fontsize=12)
            
            # X축 시간 포맷팅 (날짜가 포함되므로 포맷 변경)
            plt.gca().xaxis.set_major_formatter(mdates.DateFormatter('%m-%d %H:%M'))
            plt.gcf().autofmt_xdate() # X축 라벨 겹치지 않게 회전
            
            # 피크 지점 표시
            plt.annotate(f'Peak: {peak_load:.1f} kW', 
                         xy=(load_profile.idxmax(), peak_load), 
                         xytext=(10, 10), textcoords='offset points',
                         arrowprops=dict(arrowstyle='->', color='black', lw=1.5),
                         fontsize=10, fontweight='bold')

            plt.tight_layout()
            
            # 이미지 저장
            plt.savefig('baseline_load_profile.png', dpi=300)
            print("그래프가 'baseline_load_profile.png'로 저장되었습니다.")
            plt.show()
        else:
            print("부하 프로파일을 생성할 수 없습니다.")
    else:
        print("데이터프레임이 비어있습니다.")