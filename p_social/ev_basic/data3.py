'''
Created on 2025. 11. 28.

@author: jaewoo
'''
import pandas as pd
import numpy as np

SAVE_DIR = '/users/jaewoo/data/acn'

# ---------------------------------------------------------
# 1. 설정 (Configuration)
# ---------------------------------------------------------
CSV_FILENAME = SAVE_DIR+'/202410DatasetEVOfficeParking_v0.csv'

# 분석할 기간 설정 (2024년 9월)
TARGET_START_DATE = '2024-09-01'
TARGET_END_DATE = '2024-09-30'

# ---------------------------------------------------------
# 2. 데이터 분석 함수
# ---------------------------------------------------------
def analyze_dataset():
    try:
        # [수정] 데이터 로드 (구분자 ';')
        df = pd.read_csv(CSV_FILENAME, sep=';')
        
        # [수정] 컬럼명 매핑 (새 데이터셋 -> 표준 변수명)
        df.rename(columns={
            'start_datetime': 'connectionTime',
            'end_datetime': 'disconnectTime',
            'total_energy': 'kWhDelivered',
            'evse_uid': 'stationID' # 충전소 ID
        }, inplace=True)
        
        # 날짜 컬럼 변환
        time_cols = ['connectionTime', 'disconnectTime']
        for col in time_cols:
            if col in df.columns:
                df[col] = pd.to_datetime(df[col])
        
        # 숫자형 변환 (kWhDelivered가 문자열일 경우 콤마 처리)
        if df['kWhDelivered'].dtype == object:
             df['kWhDelivered'] = df['kWhDelivered'].astype(str).str.replace(',', '.').astype(float)

        # [수정] 기간 필터링
        mask = (df['connectionTime'] >= TARGET_START_DATE) & (df['connectionTime'] <= TARGET_END_DATE)
        df_filtered = df.loc[mask].copy()
        
        if df_filtered.empty:
            print("해당 기간의 데이터가 없습니다.")
            return

        # -----------------------------------------------------
        # 3. 핵심 통계량 계산
        # -----------------------------------------------------
        
        # 1) 기간 (Time Horizon)
        start_time = df_filtered['connectionTime'].min()
        end_time = df_filtered['disconnectTime'].max()
        duration_days = (end_time - start_time).days + 1
        
        # 2) 규모 (Scale)
        total_sessions = len(df_filtered)
        unique_stations = df_filtered['stationID'].nunique()
        
        # 3) 에너지 (Energy)
        total_energy = df_filtered['kWhDelivered'].sum()
        avg_energy = df_filtered['kWhDelivered'].mean()
        max_energy = df_filtered['kWhDelivered'].max()
        
        # 4) 시간적 특성 (Temporal) - 주차 시간
        df_filtered['parking_duration'] = (df_filtered['disconnectTime'] - df_filtered['connectionTime']).dt.total_seconds() / 3600
        avg_parking_duration = df_filtered['parking_duration'].mean()

        # -----------------------------------------------------
        # 4. 결과 출력
        # -----------------------------------------------------
        print("="*50)
        print("          EV Charging Data Basic Statistics")
        print("="*50)
        print(f"1. Filename      : {CSV_FILENAME}")
        print(f"2. Period        : {start_time.strftime('%Y-%m-%d %H:%M')} ~ {end_time.strftime('%Y-%m-%d %H:%M')}")
        print(f"3. Duration      : Approx. {duration_days} days")
        print("-" * 50)
        print(f"4. Total Sessions: {total_sessions} sessions")
        print(f"5. Charging Pts  : {unique_stations} EVSEs")
        print("-" * 50)
        print(f"6. Total Energy  : {total_energy:,.2f} kWh")
        print(f"7. Avg Energy    : {avg_energy:.2f} kWh/session")
        print(f"8. Max Energy    : {max_energy:.2f} kWh")
        print("-" * 50)
        print(f"9. Avg Parking   : {avg_parking_duration:.2f} hours")
        print("="*50)
        print("\n")

        # -----------------------------------------------------
        # 5. 논문 작성용 문구 생성 (영어)
        # -----------------------------------------------------
        print(">>> [Draft Text for Section 2.1 Data Description] <<<")
        print("-" * 60)
        paper_text = (
            f"The study analyzes a dataset of real-world electric vehicle charging sessions collected from an office parking facility. "
            f"The data used in this study covers a period of {duration_days} days, from {start_time.strftime('%B %d, %Y')} "
            f"to {end_time.strftime('%B %d, %Y')}. "
            f"During this period, a total of {total_sessions} charging sessions were recorded across {unique_stations} distinct charging points (EVSEs). "
            f"The total energy delivered was {total_energy:,.1f} kWh, with an average energy demand of {avg_energy:.2f} kWh per session. "
            f"The average parking duration was found to be {avg_parking_duration:.2f} hours, indicating a significant potential for load shifting flexibility."
        )
        print(paper_text)
        print("-" * 60)

    except FileNotFoundError:
        print("파일을 찾을 수 없습니다. 파일명을 확인해주세요.")
    except Exception as e:
        print(f"오류 발생: {e}")

if __name__ == "__main__":
    analyze_dataset()