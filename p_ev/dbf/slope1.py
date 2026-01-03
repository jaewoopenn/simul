import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import numpy as np

CSV_FILE_NAME='/users/jaewoo/data/ev/spc/ev_jobs.csv'
SAVE_FILE_NAME='/users/jaewoo/data/ev/spc/slope.png'
RESULTS_FN='/users/jaewoo/data/ev/spc/results.csv'

def plot_slope_changes(ev_jobs_file, results_file, output_file):
    """
    ev_jobs.csv(목표)와 results.csv(기록)를 비교하여
    시간별 Slope(남은 에너지 / 남은 시간) 변화를 그래프로 그립니다.
    """
    
    # 1. 데이터 로드
    try:
        ev_jobs = pd.read_csv(ev_jobs_file)
        results = pd.read_csv(results_file)
    except FileNotFoundError as e:
        print(f"파일을 찾을 수 없습니다: {e}")
        return

    # 2. 시간별 상태 재구성 (Reconstruction)
    history_records = []

    # 각 차량별로 순회
    for _, ev in ev_jobs.iterrows():
        ev_id = ev['ID']
        arrival = int(ev['Arrival'])
        departure = int(ev['Departure'])
        initial_energy = ev['Energy']
        
        # 해당 차량의 충전 기록 필터링 (Time을 인덱스로 하여 빠른 조회)
        # results.csv에 없는 시간대(충전 안 함)는 0으로 처리하기 위함
        ev_charges = results[results['EV_ID'] == ev_id].set_index('Time')['Charged_Amount']
        
        current_energy_needed = initial_energy
        
        # 도착 시간부터 출발 시간 전까지 루프 (Departure 시간에는 충전 불가하므로 제외하거나 포함 가능)
        # 여기서는 Departure 직전까지의 슬로프를 계산합니다.
        for t in range(arrival, departure):
            remaining_time = departure - t
            
            # 슬로프 계산 (0으로 나누기 방지)
            if remaining_time > 0:
                slope = current_energy_needed / remaining_time
            else:
                slope = 0 
            
            # 기록 저장
            history_records.append({
                'EV_ID': ev_id,
                'Time': t,
                'Remaining_Energy': current_energy_needed,
                'Remaining_Time': remaining_time,
                'Slope': slope
            })
            
            # 다음 시간(t+1)을 위해 현재 시간(t)의 충전량만큼 차감
            charged_amount = ev_charges.get(t, 0.0)
            current_energy_needed -= charged_amount
            
            # (옵션) 이미 충전이 완료되어 음수가 되는 경우 0으로 보정
            if current_energy_needed < 0:
                current_energy_needed = 0

    # DataFrame 변환
    df_slope = pd.DataFrame(history_records)

    # 3. 그래프 그리기
    plt.figure(figsize=(14, 8))
    
    # seaborn lineplot을 사용하여 EV ID별로 색상을 다르게 표시
    # 'tab10' 팔레트는 색상 구분이 뚜렷함
    sns.lineplot(
        data=df_slope, 
        x='Time', 
        y='Slope', 
        hue='EV_ID', 
        palette='tab10', 
        marker='o',
        linewidth=2
    )

    # 그래프 꾸미기
    plt.title('Change in Slope (Required Energy / Remaining Time) over Time', fontsize=16)
    plt.ylabel('Slope (kW) - Higher is more urgent', fontsize=12)
    plt.xlabel('Time (Hour)', fontsize=12)
    plt.axhline(0, color='black', linewidth=0.8) # 0 기준선
    plt.grid(True, linestyle='--', alpha=0.6)
    
    # 범례 위치 조정 (그래프 밖으로)
    plt.legend(title='EV ID', bbox_to_anchor=(1.02, 1), loc='upper left')
    plt.tight_layout()

    # 저장 및 출력
    plt.savefig(output_file, dpi=300)
    print(f"그래프가 {output_file}로 저장되었습니다.")
    plt.show()

# --- 실행 예시 ---
# 파일 경로가 맞는지 확인 후 실행하세요.
plot_slope_changes(CSV_FILE_NAME, RESULTS_FN,SAVE_FILE_NAME)

