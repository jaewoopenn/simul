'''
Created on 2025. 11. 28.

@author: jaewoo
'''
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import numpy as np

SAVE_DIR = '/users/jaewoo/data/acn'
OUTPUT_FIG=SAVE_DIR+'/arrival.png'
CSV_FILENAME = SAVE_DIR+'/acn_data_caltech_20190901_20190907.csv' # 업로드된 파일명

# ---------------------------------------------------------
# 1. 설정 (Configuration)
# ---------------------------------------------------------
FIGURE_FILENAME = SAVE_DIR+'/fig1_arrival_departure_histogram.png'

# ---------------------------------------------------------
# 2. 데이터 로드 및 전처리
# ---------------------------------------------------------
def load_and_process_data(filename):
    try:
        df = pd.read_csv(filename)
        
        # 날짜 컬럼 변환 (Timezone 처리 포함)
        time_cols = ['connectionTime', 'disconnectTime']
        for col in time_cols:
            if col in df.columns:
                # mixed format으로 다양한 날짜 형식 처리
                df[col] = pd.to_datetime(df[col], utc=True)
                
                # 시각화를 위해 Local Time (California/Los Angeles)으로 변환 권장
                # 여기서는 데이터에 포함된 offset(-07:00)을 그대로 반영하기 위해
                # 별도 변환 없이 시간(hour) 정보만 추출합니다.
        
        # 시간(Hour) 정보 추출 (0.0 ~ 23.99 형태로 변환하여 더 부드러운 히스토그램 생성)
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
    plt.title('Distribution of EV Arrival and Departure Times', fontsize=14)
    
    # 범례 설정
    plt.legend(fontsize=11)
    
    # 핵심 구간 강조 (선택 사항: 아침 8~9시 피크)
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
    df = load_and_process_data(CSV_FILENAME)
    plot_histogram(df)