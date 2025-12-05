'''
Created on 2025. 11. 28.

@author: jaewoo
'''
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import numpy as np

SAVE_DIR = '/users/jaewoo/data/acn'
FIGURE_FILENAME = SAVE_DIR + '/arrival.png'
# [수정] 사용자가 업로드한 새 파일명으로 변경
CSV_FILENAME = SAVE_DIR +'/202410DatasetEVOfficeParking_v0.csv'

# ---------------------------------------------------------
# 1. 설정 (Configuration)
# ---------------------------------------------------------

# [수정] 분석할 기간 설정 (예: 2024년 9월 한 달)
TARGET_START_DATE = '2024-09-01'
TARGET_END_DATE = '2024-09-30'

# ---------------------------------------------------------
# 2. 데이터 로드 및 전처리
# ---------------------------------------------------------
def load_and_process_data(filename):
    try:
        # [수정] 구분자가 세미콜론(;)이므로 sep=';' 옵션 추가
        df = pd.read_csv(filename, sep=';')
        
        # [수정] 컬럼명 매핑 (새 데이터셋 -> 기존 코드 변수명)
        # start_datetime -> connectionTime
        # end_datetime -> disconnectTime
        df.rename(columns={
            'start_datetime': 'connectionTime',
            'end_datetime': 'disconnectTime'
        }, inplace=True)
        
        # 날짜 컬럼 변환 (Timezone 처리 포함)
        time_cols = ['connectionTime', 'disconnectTime']
        for col in time_cols:
            if col in df.columns:
                # mixed format으로 다양한 날짜 형식 처리
                # 새 데이터셋은 로컬 시간인 것으로 추정되어 utc=False로 처리하거나
                # 필요시 .dt.tz_localize() 등을 사용해야 함. 일단 단순 변환.
                df[col] = pd.to_datetime(df[col])
        
        # [수정] 특정 기간(한 달) 데이터만 필터링
        mask = (df['connectionTime'] >= TARGET_START_DATE) & (df['connectionTime'] <= TARGET_END_DATE)
        df = df.loc[mask].copy()
        print(f"기간 필터링 완료 ({TARGET_START_DATE} ~ {TARGET_END_DATE}): 총 {len(df)}건")
        
        if df.empty:
            print("해당 기간의 데이터가 없습니다. 날짜 범위를 확인해주세요.")
            return pd.DataFrame()

        # 시간(Hour) 정보 추출 (0.0 ~ 23.99 형태로 변환)
        # 예: 08:30 -> 8.5
        df['arrival_hour'] = df['connectionTime'].dt.hour + df['connectionTime'].dt.minute / 60
        df['departure_hour'] = df['disconnectTime'].dt.hour + df['disconnectTime'].dt.minute / 60
        
        return df
    except Exception as e:
        print(f"데이터 로드 중 오류 발생: {e}")
        return pd.DataFrame()

# ---------------------------------------------------------
# 3. 그래프 그리기 (Figure 1 스타일)
# ---------------------------------------------------------
def plot_histogram(df):
    if df.empty:
        print("데이터가 없어 그래프를 그릴 수 없습니다.")
        return

    plt.figure(figsize=(10, 6))
    
    # 스타일 설정 (학술적 느낌)
    sns.set_style("whitegrid")
    
    # 히스토그램 그리기
    # binwidth=1 : 1시간 단위로 묶음
    # kde=True : 밀도 곡선을 같이 보여줌 (트렌드 파악 용이)
    
    sns.histplot(df['arrival_hour'], bins=24, binrange=(0, 24), 
                 color='blue', alpha=0.5, label='Arrival Time', kde=True, element="step")
    
    sns.histplot(df['departure_hour'], bins=24, binrange=(0, 24), 
                 color='red', alpha=0.5, label='Departure Time', kde=True, element="step")

    # 축 및 레이블 설정
    plt.xlim(0, 24)
    plt.xticks(np.arange(0, 25, 2)) # 0, 2, 4, ... 24시 표시
    plt.xlabel('Time of Day (Hour)', fontsize=12)
    plt.ylabel('Number of EVs (Frequency)', fontsize=12)
    plt.title(f'Distribution of EV Arrival and Departure Times ({TARGET_START_DATE}~)', fontsize=14)
    
    # 범례 설정
    plt.legend(fontsize=11)
    
    # 핵심 구간 강조 (선택 사항)
    # plt.axvspan(8, 10, color='yellow', alpha=0.2, label='Morning Peak')
    
    plt.tight_layout()
    
    # 저장 및 출력
    plt.savefig(FIGURE_FILENAME, dpi=300)
    print(f"그래프가 '{FIGURE_FILENAME}'로 저장되었습니다.")
    plt.show()

# ---------------------------------------------------------
# 4. 실행
# ---------------------------------------------------------
if __name__ == "__main__":
    # 파일 경로가 실제 환경과 맞는지 확인 필요 (여기서는 현재 디렉토리의 파일명 사용)
    df = load_and_process_data(CSV_FILENAME)
    plot_histogram(df)