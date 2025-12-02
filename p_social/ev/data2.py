import pandas as pd
import numpy as np

# 기초통

SAVE_DIR = '/users/jaewoo/data/acn'
# ---------------------------------------------------------
# 1. 설정 (Configuration)
# ---------------------------------------------------------
CSV_FILENAME = SAVE_DIR+'/acn_data.csv' # 업로드된 파일명


# ---------------------------------------------------------
# 2. 데이터 분석 함수
# ---------------------------------------------------------
def analyze_dataset():
    try:
        # 데이터 로드
        df = pd.read_csv(CSV_FILENAME)
        
        # 날짜 컬럼 변환
        time_cols = ['connectionTime', 'disconnectTime']
        for col in time_cols:
            if col in df.columns:
                df[col] = pd.to_datetime(df[col], utc=True)
        
        # -----------------------------------------------------
        # 3. 핵심 통계량 계산
        # -----------------------------------------------------
        
        # 1) 기간 (Time Horizon)
        start_time = df['connectionTime'].min()
        end_time = df['disconnectTime'].max()
        duration_days = (end_time - start_time).days + 1
        
        # 2) 규모 (Scale)
        total_sessions = len(df)
        unique_stations = df['stationID'].nunique()
        
        # 3) 에너지 (Energy)
        total_energy = df['kWhDelivered'].sum()
        avg_energy = df['kWhDelivered'].mean()
        max_energy = df['kWhDelivered'].max()
        
        # 4) 시간적 특성 (Temporal) - 주차 시간
        # (이 수치가 충전 시간보다 길다는 것이 유연성의 근거가 됨)
        df['parking_duration'] = (df['disconnectTime'] - df['connectionTime']).dt.total_seconds() / 3600
        avg_parking_duration = df['parking_duration'].mean()

        # -----------------------------------------------------
        # 4. 결과 출력
        # -----------------------------------------------------
        print("="*50)
        print("          ACN-Data 기초 통계 요약")
        print("="*50)
        print(f"1. 데이터 파일명 : {CSV_FILENAME}")
        print(f"2. 수집 기간     : {start_time.strftime('%Y-%m-%d %H:%M')} ~ {end_time.strftime('%Y-%m-%d %H:%M')}")
        print(f"3. 총 기간 (일)  : 약 {duration_days}일")
        print("-" * 50)
        print(f"4. 총 세션 수    : {total_sessions} 건")
        print(f"5. 충전소 개수   : {unique_stations} 개 (EVSEs)")
        print("-" * 50)
        print(f"6. 총 전력 소비량: {total_energy:.2f} kWh")
        print(f"7. 평균 충전량   : {avg_energy:.2f} kWh/session")
        print(f"8. 최대 충전량   : {max_energy:.2f} kWh")
        print("-" * 50)
        print(f"9. 평균 주차 시간: {avg_parking_duration:.2f} 시간")
        print("="*50)
        print("\n")

        # -----------------------------------------------------
        # 5. 논문 작성용 문구 생성 (영어)
        # -----------------------------------------------------
        print(">>> [논문용 문구 제안 - Data Description 섹션] <<<")
        print("-" * 60)
        paper_text = (
            f"The study utilizes real-world EV charging data collected from the Caltech Adaptive Charging Network (ACN) [1]. "
            f"The dataset covers a period of {duration_days} days, from {start_time.strftime('%B %d, %Y')} "
            f"to {end_time.strftime('%B %d, %Y')}. "
            f"A total of {total_sessions} charging sessions were recorded across {unique_stations} distinct electric vehicle supply equipment (EVSE) units. "
            f"The total energy delivered during this period was {total_energy:,.1f} kWh, with an average energy demand of {avg_energy:.2f} kWh per session. "
            f"Notably, the average parking duration was {avg_parking_duration:.2f} hours, which provides significant temporal flexibility for scheduling compared to the actual charging time."
        )
        print(paper_text)
        print("-" * 60)

    except FileNotFoundError:
        print("파일을 찾을 수 없습니다. 파일명을 확인해주세요.")
    except Exception as e:
        print(f"오류 발생: {e}")

if __name__ == "__main__":
    analyze_dataset()