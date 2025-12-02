import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.dates as mdates

# 충전기 그래프 

SAVE_DIR = '/users/jaewoo/data/acn'
OUTPUT_FIG=SAVE_DIR+'/stat.png'
# ---------------------------------------------------------
# 1. 설정 (Configuration)
# ---------------------------------------------------------
CSV_FILENAME = SAVE_DIR+'/acn_data_1week.csv' # 업로드된 파일명



# ---------------------------------------------------------
# 2. 데이터 로드 및 전처리
# ---------------------------------------------------------
def load_data():
    try:
        df = pd.read_csv(CSV_FILENAME)
        # 날짜/시간 컬럼 변환
        time_cols = ['connectionTime', 'disconnectTime']
        for col in time_cols:
            # 먼저 UTC로 변환
            df[col] = pd.to_datetime(df[col], utc=True)
            
            # 시각화를 위해 로컬 시간(캘리포니아 - America/Los_Angeles)으로 변환
            df[col] = df[col].dt.tz_convert('America/Los_Angeles')
            
        return df
    except FileNotFoundError:
        print(f"오류: '{CSV_FILENAME}' 파일을 찾을 수 없습니다.")
        return pd.DataFrame()

# ---------------------------------------------------------
# 3. 그래프 그리기 (타임라인/간트 차트)
# ---------------------------------------------------------
def plot_station_timeline(df):
    if df.empty:
        print("데이터가 없습니다.")
        return

    # 충전소 ID 정렬 (그래프 y축에 순서대로 표시하기 위함)
    stations = sorted(df['stationID'].unique())
    
    # 그래프 크기 설정 (세로 길이는 충전소 개수에 비례하게 조정)
    fig_height = max(6, len(stations) * 0.3)
    fig, ax = plt.subplots(figsize=(12, fig_height))

    # 각 충전소별로 세션 그리기
    # hlines(y, xmin, xmax): 수평선 그리기 함수 사용
    for i, station in enumerate(stations):
        # 해당 충전소의 데이터 필터링
        station_data = df[df['stationID'] == station]
        
        # 주차 시작(connect)부터 종료(disconnect)까지 선 그리기
        ax.hlines(y=[i] * len(station_data), 
                  xmin=station_data['connectionTime'], 
                  xmax=station_data['disconnectTime'], 
                  linewidth=5, color='skyblue', alpha=0.8)

    # -----------------------------------------------------
    # 4. 축 및 레이블 설정
    # -----------------------------------------------------
    
    # Y축: Station ID 표시
    ax.set_yticks(range(len(stations)))
    ax.set_yticklabels(stations, fontsize=8)
    ax.set_ylabel('Station ID', fontsize=12)

    # X축: 날짜 포맷 설정 (월-일 시:분)
    # tz=None을 주어 로컬 타임존의 시간을 그대로 표시하도록 함
    ax.xaxis.set_major_formatter(mdates.DateFormatter('%m-%d %H:%M', tz=df['connectionTime'].dt.tz))
    ax.xaxis.set_major_locator(mdates.DayLocator(interval=1)) # 1일 간격 눈금
    plt.xticks(rotation=45)
    ax.set_xlabel('Time (PST/PDT)', fontsize=12)

    # 타이틀 및 그리드
    # 타이틀 날짜도 로컬 시간 기준으로 표시
    start_date = df["connectionTime"].min().date()
    end_date = df["disconnectTime"].max().date()
    ax.set_title(f'Parking Duration Timeline per Station ({start_date} ~ {end_date})', fontsize=14)
    ax.grid(True, axis='x', linestyle='--', alpha=0.5) # 시간축 그리드만 표시

    plt.tight_layout()
    
    # 저장 및 출력
    plt.savefig(OUTPUT_FIG, dpi=300)
    print(f"그래프가 '{OUTPUT_FIG}'로 저장되었습니다.")
    plt.show()

if __name__ == "__main__":
    df = load_data()
    plot_station_timeline(df)