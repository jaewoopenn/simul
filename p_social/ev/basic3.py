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
# 3. 비제어 충전(Uncoordinated/ASAP) 부하 계산
# ---------------------------------------------------------
def calculate_uncoordinated_load(ev_data):
    if ev_data.empty:
        return pd.Series()

    # 시뮬레이션 전체 시간 축 생성
    start_time = ev_data['connectionTime'].min().floor('D')
    end_time = ev_data['disconnectTime'].max().ceil('D')
    
    time_index = pd.date_range(start=start_time, end=end_time, freq=f'{TIME_INTERVAL}min')
    total_load = pd.Series(0.0, index=time_index)

    print(f"ASAP(Uncoordinated) 부하 계산 중...")

    for _, row in ev_data.iterrows():
        arrival = row['connectionTime']
        energy_needed = row['kWhDelivered']
        
        if pd.isna(arrival) or pd.isna(energy_needed) or energy_needed <= 0:
            continue
            
        # 충전 필요 시간
        duration_hours = energy_needed / MAX_CHARGING_RATE
        duration = pd.Timedelta(hours=duration_hours)
        
        # ASAP: 도착하자마자 시작
        start_charging = arrival
        finish_charging = arrival + duration
        
        # 해당 시간 구간에 부하(kW) 더하기
        mask = (total_load.index >= start_charging) & (total_load.index < finish_charging)
        total_load[mask] += MAX_CHARGING_RATE

    return total_load

# ---------------------------------------------------------
# 4. ALAP (As Late As Possible) 부하 계산 - 추가된 비교군
# ---------------------------------------------------------
def calculate_alap_load(ev_data):
    """
    떠나기 직전에 충전을 완료하도록 최대한 늦게 충전하는 방식 (Valley Filling 효과 비교용)
    """
    if ev_data.empty:
        return pd.Series()

    # 시간 축 생성 (ASAP와 동일하게)
    start_time = ev_data['connectionTime'].min().floor('D')
    end_time = ev_data['disconnectTime'].max().ceil('D')
    time_index = pd.date_range(start=start_time, end=end_time, freq=f'{TIME_INTERVAL}min')
    total_load = pd.Series(0.0, index=time_index)

    print(f"ALAP(As Late As Possible) 부하 계산 중...")

    for _, row in ev_data.iterrows():
        arrival = row['connectionTime']
        departure = row['disconnectTime']
        energy_needed = row['kWhDelivered']
        
        if pd.isna(arrival) or pd.isna(departure) or pd.isna(energy_needed) or energy_needed <= 0:
            continue
            
        duration_hours = energy_needed / MAX_CHARGING_RATE
        duration = pd.Timedelta(hours=duration_hours)
        
        # ALAP: 떠나는 시간에 딱 맞춰서 끝내도록 시작 시간 계산
        finish_charging = departure
        start_charging = departure - duration
        
        # 예외 처리: 만약 주차 시간이 충전 필요 시간보다 짧다면? -> 도착하자마자 충전(ASAP)로 처리
        if start_charging < arrival:
            start_charging = arrival
            finish_charging = arrival + duration

        mask = (total_load.index >= start_charging) & (total_load.index < finish_charging)
        total_load[mask] += MAX_CHARGING_RATE

    return total_load

# ---------------------------------------------------------
# 5. 실행 및 시각화
# ---------------------------------------------------------
if __name__ == "__main__":
    # 데이터 가져오기
    ev_df = get_ev_data()
    
    if not ev_df.empty:
        # 1. ASAP (Uncoordinated) 부하 계산
        load_asap = calculate_uncoordinated_load(ev_df)
        
        # 2. ALAP (As Late As Possible) 부하 계산 - 추가됨
        load_alap = calculate_alap_load(ev_df)
        
        if not load_asap.empty:
            # 피크 부하 확인
            peak_asap = load_asap.max()
            peak_alap = load_alap.max()
            
            print(f"\n[Result] ASAP Peak: {peak_asap:.2f} kW")
            print(f"[Result] ALAP Peak: {peak_alap:.2f} kW")

            # 그래프 그리기
            plt.figure(figsize=(12, 6))
            
            # ASAP 그래프 (빨강)
            plt.plot(load_asap.index, load_asap.values, 
                     label='Uncoordinated (ASAP)', color='#d62728', linewidth=1.5)
            plt.fill_between(load_asap.index, load_asap.values, color='#d62728', alpha=0.1)

            # ALAP 그래프 (파랑, 점선) - 추가됨
            plt.plot(load_alap.index, load_alap.values, 
                     label='Delayed (ALAP)', color='#1f77b4', linewidth=1.5, linestyle='--')
            
            # 그래프 꾸미기
            plt.title(f'Load Profile Comparison: ASAP vs ALAP ({ev_df["connectionTime"].dt.date.iloc[0]} ~ {ev_df["connectionTime"].dt.date.iloc[-1]})', fontsize=14)
            plt.xlabel('Date & Time', fontsize=12)
            plt.ylabel('Power Demand (kW)', fontsize=12)
            plt.grid(True, linestyle='--', alpha=0.7)
            plt.legend(fontsize=12, loc='upper right')
            
            # X축 포맷팅
            plt.gca().xaxis.set_major_formatter(mdates.DateFormatter('%m-%d %H:%M'))
            plt.gcf().autofmt_xdate()
            
            plt.tight_layout()
            
            # 이미지 저장
            plt.savefig(OUTPUT_FIG, dpi=300)
            print("그래프가 'baseline_comparison_load_profile.png'로 저장되었습니다.")
            plt.show()
        else:
            print("부하 프로파일을 생성할 수 없습니다.")
    else:
        print("데이터프레임이 비어있습니다.")