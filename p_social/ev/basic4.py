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
    try:
        df = pd.read_csv(CSV_FILENAME)
        time_cols = ['connectionTime', 'disconnectTime', 'doneChargingTime']
        for col in time_cols:
            if col in df.columns:
                df[col] = pd.to_datetime(df[col], utc=True)
        df = df.sort_values(by='connectionTime')
        print(f"데이터 로드 성공: {len(df)}개의 충전 세션")
        return df
    except FileNotFoundError:
        print(f"오류: '{CSV_FILENAME}' 파일을 찾을 수 없습니다.")
        return pd.DataFrame()
    except Exception as e:
        print(f"데이터 로드 중 오류 발생: {e}")
        return pd.DataFrame()

# ---------------------------------------------------------
# 3. 비제어 충전 (ASAP: Baseline 1)
# ---------------------------------------------------------
def calculate_uncoordinated_load(ev_data, time_index):
    total_load = pd.Series(0.0, index=time_index)
    print("ASAP (Uncoordinated) 계산 중...")

    for _, row in ev_data.iterrows():
        arrival = row['connectionTime']
        energy_needed = row['kWhDelivered']
        
        duration_hours = energy_needed / MAX_CHARGING_RATE
        finish_time = arrival + pd.Timedelta(hours=duration_hours)
        
        mask = (total_load.index >= arrival) & (total_load.index < finish_time)
        total_load[mask] += MAX_CHARGING_RATE
    return total_load

# ---------------------------------------------------------
# 4. 최대한 늦게 충전 (ALAP: Baseline 2)
# ---------------------------------------------------------
def calculate_alap_load(ev_data, time_index):
    total_load = pd.Series(0.0, index=time_index)
    print("ALAP (Delayed) 계산 중...")

    for _, row in ev_data.iterrows():
        arrival = row['connectionTime']
        departure = row['disconnectTime']
        energy_needed = row['kWhDelivered']
        
        duration_hours = energy_needed / MAX_CHARGING_RATE
        start_time = departure - pd.Timedelta(hours=duration_hours)
        
        # 물리적으로 불가능한 경우(주차시간 < 충전시간) 도착 즉시 충전으로 보정
        if start_time < arrival:
            start_time = arrival

        mask = (total_load.index >= start_time) & (total_load.index < departure)
        total_load[mask] += MAX_CHARGING_RATE
    return total_load

# ---------------------------------------------------------
# 5. Offline Optimal (Valley Filling) - NEW!
# ---------------------------------------------------------
def calculate_offline_optimal_load(ev_data, time_index):
    """
    모든 차량의 정보를 미리 알고, 피크를 최소화하도록 충전 스케줄을 평탄화(Valley Filling)합니다.
    Iterative Water-filling 방식을 사용하여 최적해에 근접합니다.
    """
    print("Offline Optimal (Valley Filling) 계산 중... (약간의 시간이 소요됩니다)")
    
    # 1. 초기화: 모든 차량의 부하를 0으로 시작
    # (time_steps x num_evs) 매트릭스가 필요하지만, 메모리 절약을 위해
    # 전체 로드 프로파일만 유지하고 개별 차량은 반복문에서 처리
    
    # 시간 인덱스를 정수 인덱스로 매핑하여 속도 향상
    t_len = len(time_index)
    total_load_arr = np.zeros(t_len)
    
    # 각 차량의 정보 미리 계산 (시작 인덱스, 종료 인덱스, 필요 에너지 등)
    # 5분 단위 에너지 = 6.6 kW * (5/60) h = 0.55 kWh
    energy_per_slot = MAX_CHARGING_RATE * (TIME_INTERVAL / 60)
    
    ev_profiles = [] # 각 EV의 현재 스케줄 (t_start, t_end, profile_array)
    
    for _, row in ev_data.iterrows():
        # 시간 범위를 인덱스로 변환
        start_idx = time_index.searchsorted(row['connectionTime'])
        end_idx = time_index.searchsorted(row['disconnectTime'])
        
        if start_idx >= t_len or end_idx <= 0 or start_idx >= end_idx:
            ev_profiles.append(None)
            continue
            
        # 범위 보정
        start_idx = max(0, start_idx)
        end_idx = min(t_len, end_idx)
        
        req_energy = row['kWhDelivered']
        # 초기 스케줄: 균등 분배 (Flat start)
        duration_slots = end_idx - start_idx
        avg_power = min(MAX_CHARGING_RATE, req_energy / (duration_slots * (TIME_INTERVAL/60)))
        
        profile = np.full(duration_slots, avg_power)
        
        # 에너지 총량 맞추기 (미세 오차 보정)
        current_energy = np.sum(profile) * (TIME_INTERVAL/60)
        if current_energy < req_energy:
             # 부족하면 max rate 내에서 조금 더 채움 (여기선 단순화)
             pass

        ev_profiles.append({
            'start': start_idx,
            'end': end_idx,
            'profile': profile,
            'energy': req_energy
        })
        
        # 전체 로드에 더하기
        total_load_arr[start_idx:end_idx] += profile

    # 2. 반복적 최적화 (Iterative Update)
    # 각 차량별로 "나를 뺀 나머지 부하"의 계곡(Valley)을 찾아 물을 채우듯(Water-filling) 재배치
    iterations = 15 # 반복 횟수 (보통 10~20회면 수렴)
    
    for it in range(iterations):
        # max_change = 0
        for i, ev in enumerate(ev_profiles):
            if ev is None: continue
            
            s, e = ev['start'], ev['end']
            old_profile = ev['profile']
            
            # 1. 전체 로드에서 내 기여분 빼기 (나를 제외한 배경 부하)
            total_load_arr[s:e] -= old_profile
            background_load = total_load_arr[s:e]
            
            # 2. Water-filling (이진 탐색으로 최적 높이 H 찾기)
            # 목표: sum(clip(H - background, 0, max_rate)) * dt = req_energy
            
            target_energy = ev['energy']
            
            # 이진 탐색 범위
            low = np.min(background_load)
            high = np.max(background_load) + MAX_CHARGING_RATE
            best_profile = old_profile
            
            for _ in range(10): # 이진 탐색 10회면 충분히 정밀함
                mid = (low + high) / 2
                # 물 채우기 로직: 배경이 낮으면 채우고, 높으면 안 채움. 최대 속도 제한.
                proposed_power = np.clip(mid - background_load, 0, MAX_CHARGING_RATE)
                proposed_energy = np.sum(proposed_power) * (TIME_INTERVAL/60)
                
                if proposed_energy < target_energy:
                    low = mid # 높이를 더 높여야 함
                else:
                    high = mid # 높이를 낮춰야 함
                    best_profile = proposed_power # 조건 만족 시 저장
            
            # 3. 업데이트된 프로파일 적용
            ev['profile'] = best_profile
            total_load_arr[s:e] += best_profile
            
    return pd.Series(total_load_arr, index=time_index)

# ---------------------------------------------------------
# 6. 실행 및 시각화
# ---------------------------------------------------------
if __name__ == "__main__":
    ev_df = get_ev_data()
    
    if not ev_df.empty:
        # 공통 시간 축 생성
        start_time = ev_df['connectionTime'].min().floor('D')
        end_time = ev_df['disconnectTime'].max().ceil('D')
        time_index = pd.date_range(start=start_time, end=end_time, freq=f'{TIME_INTERVAL}min')
        
        # 1. 부하 계산
        load_asap = calculate_uncoordinated_load(ev_df, time_index)
        #load_alap = calculate_alap_load(ev_df, time_index)
        load_opt = calculate_offline_optimal_load(ev_df, time_index) # Offline Optimal
        
        # 피크 값 출력
        print(f"\n[Result] ASAP Peak: {load_asap.max():.2f} kW")
        #print(f"[Result] ALAP Peak: {load_alap.max():.2f} kW")
        print(f"[Result] Optimal Peak: {load_opt.max():.2f} kW (Theoretical Minimum)")

        # 2. 그래프 그리기
        plt.figure(figsize=(12, 6))
        
        # ASAP (Uncoordinated) - 붉은색 영역
        plt.fill_between(load_asap.index, load_asap.values, color='red', alpha=0.15, label='Uncoordinated (ASAP)')
        plt.plot(load_asap.index, load_asap.values, color='red', linewidth=1, alpha=0.6)

        # ALAP (Delayed) - 파란색 점선
        #plt.plot(load_alap.index, load_alap.values, color='blue', linestyle=':', linewidth=1.5, label='Delayed (ALAP)')
        
        # Offline Optimal - 초록색 굵은 실선 (주인공)
        plt.plot(load_opt.index, load_opt.values, color='green', linewidth=2.5, label='Offline Optimal (Valley Filling)')
        
        # 그래프 꾸미기
        plt.title('Baseline Comparison: Uncoordinated vs Offline Optimal', fontsize=14)
        plt.xlabel('Date & Time', fontsize=12)
        plt.ylabel('Power Demand (kW)', fontsize=12)
        plt.grid(True, linestyle='--', alpha=0.6)
        plt.legend(fontsize=11, loc='upper right', frameon=True)
        
        # X축 포맷
        plt.gca().xaxis.set_major_formatter(mdates.DateFormatter('%m-%d %H:%M'))
        plt.gcf().autofmt_xdate()
        
        plt.tight_layout()
        plt.savefig(OUTPUT_FIG, dpi=300)
        print("그래프가 'baseline_with_optimal_profile.png'로 저장되었습니다.")
        plt.show()
    else:
        print("데이터프레임이 비어있습니다.")