'''
Created on 2025. 11. 29.

@author: jaewoo
'''
import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.dates as mdates



SAVE_DIR = '/users/jaewoo/data/acn'
OUTPUT_FIG = SAVE_DIR + '/top10_station_timeline.png'
# [수정] 새 데이터 파일명 cleaned 
CSV_FILENAME = SAVE_DIR + '/cleaned_ev_data_2024_09.csv'

# ---------------------------------------------------------
# 1. 설정 (Configuration)
# ---------------------------------------------------------
# 분석 기간 (7일간)
TARGET_START_DATE = '2024-09-01'
TARGET_END_DATE = '2024-09-07'

# 보여줄 충전소 개수
TOP_N_STATIONS = 10

# ---------------------------------------------------------
# 2. 데이터 로드 및 전처리
# ---------------------------------------------------------
def load_data():
    try:
        # 구분자 세미콜론(;)
        df = pd.read_csv(CSV_FILENAME, sep=',')
        print (df.head())
        # # 날짜 변환
        for col in ['connectionTime', 'disconnectTime']:
            df[col] = pd.to_datetime(df[col])
            
        return df
    except FileNotFoundError:
        print(f"오류: '{CSV_FILENAME}' 파일을 찾을 수 없습니다.")
        return pd.DataFrame()

# ---------------------------------------------------------
# 3. 그래프 그리기
# ---------------------------------------------------------
def plot_top_stations(df):
    if df.empty:
        print("데이터가 없습니다.")
        return

    # 1. 기간 필터링 (7일)
    mask = (df['connectionTime'] >= TARGET_START_DATE) & (df['connectionTime'] <= f"{TARGET_END_DATE} 23:59:59")
    df_period = df.loc[mask].copy()
    
    if df_period.empty:
        print(f"해당 기간({TARGET_START_DATE} ~ {TARGET_END_DATE})의 데이터가 없습니다.")
        return

    # 2. 가장 바쁜 Top 10 충전소 선정 (세션 수 기준)
    top_stations = df_period['stationID'].value_counts().nlargest(TOP_N_STATIONS).index.tolist()
    
    # Top 10 충전소 데이터만 남기기
    df_filtered = df_period[df_period['stationID'].isin(top_stations)].copy()
    
    # 시각화를 위해 충전소 ID 정렬 (그래프 위에서부터 1위가 오도록 역순 정렬 추천)
    # value_counts 순서(바쁜 순)대로 정렬
    top_stations.reverse() # 그래프 y축은 아래에서 위로 올라가므로, 1위를 맨 위에 두려면 리스트를 뒤집어야 함

    print(f"분석 기간: {TARGET_START_DATE} ~ {TARGET_END_DATE}")
    print(f"선정된 Top {TOP_N_STATIONS} 충전소: {top_stations[::-1]}") # 출력은 다시 원래 순서로

    # 3. 그래프 그리기
    fig, ax = plt.subplots(figsize=(12, 8))

    # 각 충전소별로 막대(Gantt chart) 그리기
    # y값은 top_stations 리스트의 인덱스로 매핑
    station_y_map = {station: i for i, station in enumerate(top_stations)}
    
    # 색상 설정 (시각적 구분)
    colors = plt.cm.tab10(range(TOP_N_STATIONS))

    # 데이터 순회하며 그리기
    for station in top_stations:
        station_data = df_filtered[df_filtered['stationID'] == station]
        y_pos = station_y_map[station]
        
        # 선 그리기 (hlines)
        # alpha=0.8로 약간 투명하게 하여 겹침 확인 가능하도록 함
        ax.hlines(y=[y_pos] * len(station_data), 
                  xmin=station_data['connectionTime'], 
                  xmax=station_data['disconnectTime'], 
                  linewidth=8, color='royalblue', alpha=0.7)

    # -----------------------------------------------------
    # 4. 축 및 레이블 설정
    # -----------------------------------------------------
    
    # Y축: Station ID 표시
    ax.set_yticks(range(len(top_stations)))
    ax.set_yticklabels(top_stations, fontsize=10)
    ax.set_ylabel('Station ID (Top 10 Busy)', fontsize=12, fontweight='bold')

    # X축: 날짜 포맷 설정 (일-시간)
    # 7일치 데이터이므로 1일 단위 눈금 + 시간 표시
    ax.xaxis.set_major_formatter(mdates.DateFormatter('%m-%d %H:%M'))
    ax.xaxis.set_major_locator(mdates.DayLocator(interval=1))
    ax.xaxis.set_minor_locator(mdates.HourLocator(byhour=[12])) # 정오(12시)에 보조 눈금
    
    plt.xticks(rotation=30)
    ax.set_xlabel('Time', fontsize=12)

    # 타이틀 및 그리드
    ax.set_title(f'Parking Schedule of Top {TOP_N_STATIONS} Busy Stations ({TARGET_START_DATE} ~ {TARGET_END_DATE})', fontsize=14)
    ax.grid(True, axis='x', linestyle='--', alpha=0.5, which='both')
    
    # 주석: 바쁜 정도 설명
    total_sessions_shown = len(df_filtered)
    plt.figtext(0.99, 0.01, f'Total Sessions in View: {total_sessions_shown}', horizontalalignment='right')

    plt.tight_layout()
    
    # 저장
    plt.savefig(OUTPUT_FIG, dpi=300)
    print(f"그래프가 'top10_station_timeline.png'로 저장되었습니다.")
    plt.show()

if __name__ == "__main__":
    df = load_data()
    plot_top_stations(df)