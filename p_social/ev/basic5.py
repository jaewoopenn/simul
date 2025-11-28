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

# [중요] 스케줄링 효과를 보기 위한 전력망 용량 제한 (kW)
# 나중에 ASAP 피크의 60% 정도로 자동 설정됩니다.
GRID_CAPACITY_LIMIT = None 

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
# 3. 비제어 충전 (ASAP)
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
# 4. Offline Optimal (Valley Filling)
# ---------------------------------------------------------
def calculate_offline_optimal_load(ev_data, time_index):
    print("Offline Optimal (Valley Filling) 계산 중...")
    t_len = len(time_index)
    total_load_arr = np.zeros(t_len)
    
    ev_profiles = []
    
    for _, row in ev_data.iterrows():
        start_idx = time_index.searchsorted(row['connectionTime'])
        end_idx = time_index.searchsorted(row['disconnectTime'])
        
        if start_idx >= t_len or end_idx <= 0 or start_idx >= end_idx:
            continue
            
        start_idx = max(0, start_idx)
        end_idx = min(t_len, end_idx)
        
        req_energy = row['kWhDelivered']
        duration_slots = end_idx - start_idx
        # 초기화: 균등 분배
        avg_power = min(MAX_CHARGING_RATE, req_energy / (duration_slots * (TIME_INTERVAL/60)))
        profile = np.full(duration_slots, avg_power)
        
        ev_profiles.append({
            'start': start_idx, 'end': end_idx,
            'profile': profile, 'energy': req_energy
        })
        total_load_arr[start_idx:end_idx] += profile

    # Iterative Water-filling
    iterations = 15
    for it in range(iterations):
        for ev in ev_profiles:
            s, e = ev['start'], ev['end']
            old_profile = ev['profile']
            
            # 나를 뺀 배경 부하
            total_load_arr[s:e] -= old_profile
            background_load = total_load_arr[s:e]
            
            target_energy = ev['energy']
            low, high = np.min(background_load), np.max(background_load) + MAX_CHARGING_RATE
            best_profile = old_profile
            
            for _ in range(10):
                mid = (low + high) / 2
                proposed_power = np.clip(mid - background_load, 0, MAX_CHARGING_RATE)
                proposed_energy = np.sum(proposed_power) * (TIME_INTERVAL/60)
                
                if proposed_energy < target_energy:
                    low = mid
                else:
                    high = mid
                    best_profile = proposed_power
            
            ev['profile'] = best_profile
            total_load_arr[s:e] += best_profile
            
    return pd.Series(total_load_arr, index=time_index)

# ---------------------------------------------------------
# 5. EDF (Earliest Deadline First) - NEW!
# ---------------------------------------------------------
def calculate_edf_load(ev_data, time_index, grid_capacity):
    """
    EDF 알고리즘:
    - 매 시간마다 현재 연결된 차량을 확인
    - 출발 시간(Deadline)이 가장 빠른 순서대로 정렬
    - 전력망 용량(grid_capacity) 한도 내에서 충전 허용
    """
    print(f"EDF (Earliest Deadline First) 계산 중... (Grid Cap: {grid_capacity:.1f} kW)")
    
    t_len = len(time_index)
    total_load_arr = np.zeros(t_len)
    dt_hours = TIME_INTERVAL / 60.0
    
    # 시뮬레이션을 위해 각 차량의 상태를 추적할 객체 생성
    # 미리 인덱스를 계산해두어 속도 향상
    ev_states = []
    for _, row in ev_data.iterrows():
        ev_states.append({
            'arrival_idx': time_index.searchsorted(row['connectionTime']),
            'departure_idx': time_index.searchsorted(row['disconnectTime']),
            'remaining_energy': row['kWhDelivered'],
            'max_rate': MAX_CHARGING_RATE,
            'id': _ # 디버깅용
        })
        
    # Time-stepping Simulation
    for t in range(t_len):
        # 1. 현재 주차 중이고, 충전이 덜 된 차량 찾기 (Active EVs)
        active_evs = []
        for i, ev in enumerate(ev_states):
            if ev['arrival_idx'] <= t < ev['departure_idx']: # 주차 중
                if ev['remaining_energy'] > 0: # 에너지 필요함
                    active_evs.append(i)
        
        if not active_evs:
            continue
            
        # 2. 정렬: 출발 시간이 빠른 순서 (Earliest Deadline First)
        # departure_idx가 작을수록 마감이 임박한 것
        active_evs.sort(key=lambda i: ev_states[i]['departure_idx'])
        
        # 3. 전력 할당
        current_grid_load = 0
        
        for ev_idx in active_evs:
            # 전력망 여유분 확인
            if current_grid_load >= grid_capacity:
                break # 용량 초과, 나머지 차량은 대기(0kW)
            
            # 할당 가능한 최대 전력
            available_power = grid_capacity - current_grid_load
            
            # 배터리 잔량에 따른 필요 전력 (이번 step에 완충되는 경우 고려)
            energy_needed = ev_states[ev_idx]['remaining_energy']
            power_needed = energy_needed / dt_hours
            
            # 최종 할당 전력 = min(차량최대, 전력망여유, 잔량필요분)
            allocated_power = min(ev_states[ev_idx]['max_rate'], available_power, power_needed)
            
            # 상태 업데이트
            total_load_arr[t] += allocated_power
            current_grid_load += allocated_power
            ev_states[ev_idx]['remaining_energy'] -= (allocated_power * dt_hours)
            
            # 부동소수점 오차 보정
            if ev_states[ev_idx]['remaining_energy'] < 0.001:
                ev_states[ev_idx]['remaining_energy'] = 0

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
        
        # 1. ASAP 계산 (Baseline)
        load_asap = calculate_uncoordinated_load(ev_df, time_index)
        asap_peak = load_asap.max()
        
        # 2. 전력망 용량 제한 설정 (ASAP 피크의 60%로 설정하여 스케줄링 효과 확인)
        GRID_CAPACITY = asap_peak * 0.6
        
        # 3. EDF 계산 (New!)
        load_edf = calculate_edf_load(ev_df, time_index, GRID_CAPACITY)
        
        # 4. Offline Optimal 계산
        load_opt = calculate_offline_optimal_load(ev_df, time_index)
        
        # 결과 출력
        print(f"\n[Result] Grid Capacity Limit: {GRID_CAPACITY:.2f} kW")
        print(f"[Result] ASAP Peak: {asap_peak:.2f} kW")
        print(f"[Result] EDF Peak:  {load_edf.max():.2f} kW (제한값 준수)")
        print(f"[Result] Optimal Peak: {load_opt.max():.2f} kW")

        # 5. 그래프 그리기
        plt.figure(figsize=(12, 6))
        
        # ASAP (Uncoordinated) - 회색 배경으로 깔기
        plt.fill_between(load_asap.index, load_asap.values, color='gray', alpha=0.2, label='Uncoordinated (ASAP)')
        
        # Grid Capacity - 붉은 점선 (제약 조건)
        plt.axhline(y=GRID_CAPACITY, color='red', linestyle='--', linewidth=1.5, label='Grid Capacity Limit')
        
        # EDF - 파란색 실선 (주인공)
        plt.plot(load_edf.index, load_edf.values, color='blue', linewidth=2, label='EDF Scheduling')
        
        # Offline Optimal - 초록색 점선 (비교군)
        plt.plot(load_opt.index, load_opt.values, color='green', linestyle=':', linewidth=2, label='Offline Optimal')
        
        # 그래프 꾸미기
        plt.title('Scheduling Algorithm Comparison: ASAP vs EDF vs Optimal', fontsize=14)
        plt.xlabel('Date & Time', fontsize=12)
        plt.ylabel('Power Demand (kW)', fontsize=12)
        plt.grid(True, linestyle='--', alpha=0.6)
        plt.legend(fontsize=11, loc='upper right', frameon=True)
        
        # X축 포맷
        plt.gca().xaxis.set_major_formatter(mdates.DateFormatter('%m-%d %H:%M'))
        plt.gcf().autofmt_xdate()
        
        plt.tight_layout()
        plt.savefig(OUTPUT_FIG, dpi=300)
        print(f"그래프가 {OUTPUT_FIG}로 저장되었습니다.")
        plt.show()
    else:
        print("데이터프레임이 비어있습니다.")