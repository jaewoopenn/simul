'''
Created on 2025. 11. 28.

@author: jaewoo
'''
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import numpy as np

SAVE_DIR = '/users/jaewoo/data/acn'
FIGURE_FILENAME = SAVE_DIR + '/flexibility.png'
# [수정] 새 데이터 파일명
CSV_FILENAME =SAVE_DIR + '/202410DatasetEVOfficeParking_v0.csv'

# ---------------------------------------------------------
# 1. 설정 (Configuration)
# ---------------------------------------------------------
MAX_CHARGING_RATE = 6.6  # kW (일반적인 완속 충전 속도 가정, 필요시 수정)

# [수정] 분석할 기간 설정
TARGET_START_DATE = '2024-09-01'
TARGET_END_DATE = '2024-09-30'

# ---------------------------------------------------------
# 2. 데이터 로드 및 가공
# ---------------------------------------------------------
def load_and_process_data(filename):
    try:
        # [수정] 구분자 세미콜론(;) 지정
        df = pd.read_csv(filename, sep=';')
        
        # [수정] 컬럼명 매핑
        # start_datetime -> connectionTime
        # end_datetime -> disconnectTime
        # total_energy -> kWhDelivered (충전량)
        df.rename(columns={
            'start_datetime': 'connectionTime',
            'end_datetime': 'disconnectTime',
            'total_energy': 'kWhDelivered'
        }, inplace=True)
        
        # 날짜 컬럼 변환
        time_cols = ['connectionTime', 'disconnectTime']
        for col in time_cols:
            if col in df.columns:
                df[col] = pd.to_datetime(df[col])

        # [수정] 특정 기간(한 달) 데이터만 필터링
        mask = (df['connectionTime'] >= TARGET_START_DATE) & (df['connectionTime'] <= TARGET_END_DATE)
        df = df.loc[mask].copy()
        print(f"기간 필터링 완료 ({TARGET_START_DATE} ~ {TARGET_END_DATE}): 총 {len(df)}건")

        if df.empty:
            return pd.DataFrame()

        # 필요한 데이터 계산
        # 1. 주차 시간 (Parking Duration) = 떠난 시간 - 도착 시간 (단위: 시간)
        df['parking_duration_hours'] = (df['disconnectTime'] - df['connectionTime']).dt.total_seconds() / 3600
        
        # 2. 충전 필요 시간 (Required Charging Duration) = 충전량 / 충전속도
        # (실제 충전 완료 시간이 아니라, 물리적으로 필요한 최소 시간을 계산하여 유연성의 최대치를 보여줌)
        # kWhDelivered가 문자열이거나 콤마가 포함된 경우 처리 (유럽식 숫자 표기 대비)
        if df['kWhDelivered'].dtype == object:
             df['kWhDelivered'] = df['kWhDelivered'].astype(str).str.replace(',', '.').astype(float)
             
        df['charging_duration_hours'] = df['kWhDelivered'] / MAX_CHARGING_RATE
        
        # 데이터 정제 (이상치 제거)
        # 주차 시간이 0보다 작거나, 24시간을 넘는 경우 등 제외 (분석 목적에 따라 조절)
        df = df[df['parking_duration_hours'] > 0]
        df = df[df['charging_duration_hours'] > 0]
        
        # 주차 시간보다 충전 필요 시간이 더 긴 경우 (물리적으로 불가능하거나 급속충전인 경우) 제외
        # 논리적으로 Laxity >= 0 이어야 함
        df = df[df['parking_duration_hours'] >= df['charging_duration_hours']]
        
        return df
    except Exception as e:
        print(f"데이터 처리 중 오류 발생: {e}")
        return pd.DataFrame()

# ---------------------------------------------------------
# 3. 그래프 그리기
# ---------------------------------------------------------
def plot_scatter(df):
    if df.empty:
        print("데이터가 없습니다.")
        return

    plt.figure(figsize=(8, 8))
    sns.set_style("whitegrid")

    # 1. 메인 산점도 (Scatter Plot)
    # alpha: 투명도 (점이 겹쳐 보이는 빈도를 보여주기 위함)
    plt.scatter(df['parking_duration_hours'], df['charging_duration_hours'], 
                alpha=0.6, c='blue', edgecolors='w', s=60, label='EV Sessions')

    # 2. 기준선 (y=x) 그리기
    # 이 선 위에 있는 점은 "주차 시간 = 충전 시간"인 경우로, 여유(Laxity)가 0인 급한 차량들
    max_val = max(df['parking_duration_hours'].max(), df['charging_duration_hours'].max())
    # 축 범위를 적절히 제한 (예: 24시간 또는 데이터 최대값)
    display_limit = min(24, max_val + 1) 
    
    plt.plot([0, display_limit], [0, display_limit], 'r--', label='Zero Laxity Line (No Flexibility)')

    # 3. 영역 채우기 (Flexible Area)
    # y=x 선 아래 영역은 "주차 시간 > 충전 시간"인 경우로, 스케줄링이 가능한 영역
    plt.fill_between([0, display_limit], [0, display_limit], 0, color='green', alpha=0.1, label='Flexible Region (Slack Time)')

    # 4. 축 및 레이블 설정
    plt.xlim(0, 12) # 대부분 12시간 이내 주차하므로 12로 제한 (데이터 분포에 따라 조절 가능)
    plt.ylim(0, 12)
    plt.xlabel('Parking Duration (Hours)', fontsize=12)
    plt.ylabel('Required Charging Duration (Hours)', fontsize=12)
    plt.title(f'Flexibility Analysis ({TARGET_START_DATE}~)', fontsize=14)
    
    # 5. 평균값 표시 (선택 사항)
    avg_park = df['parking_duration_hours'].mean()
    avg_charge = df['charging_duration_hours'].mean()
    plt.scatter(avg_park, avg_charge, color='red', s=100, marker='X', label=f'Average ({avg_park:.1f}h park, {avg_charge:.1f}h charge)')

    plt.legend(loc='upper left', fontsize=10)
    plt.tight_layout()
    
    # 저장
    plt.savefig(FIGURE_FILENAME, dpi=300)
    print(f"그래프가 '{FIGURE_FILENAME}'로 저장되었습니다.")
    plt.show()

# ---------------------------------------------------------
# 4. 실행
# ---------------------------------------------------------
if __name__ == "__main__":
    df = load_and_process_data(CSV_FILENAME)
    plot_scatter(df)