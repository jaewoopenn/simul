'''
Created on 2025. 11. 28.

@author: jaewoo
'''
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.dates as mdates
import datetime

SAVE_DIR = '/users/jaewoo/data/acn'
OUTPUT_FIG = SAVE_DIR + '/load_profile_comparison.png'
# [수정] 새 데이터 파일명
CSV_FILENAME = SAVE_DIR +'/cleaned_ev_data_2024_09.csv'

# ---------------------------------------------------------
# 1. 설정 (Configuration)
# ---------------------------------------------------------
MAX_CHARGING_RATE = 6.6  # kW
TIME_INTERVAL = 15       # 분 단위 (새 데이터가 촘촘하지 않을 수 있으므로 15분으로 조정 가능, 필요시 5분 유지)

# 분석할 기간 (9월 한 달)
TARGET_START_DATE = '2024-09-01'
TARGET_END_DATE = '2024-09-30'

# 전력망 용량 제한 (나중에 ASAP 피크의 60% 등으로 자동 설정됨)
GRID_CAPACITY_LIMIT = None 

# ---------------------------------------------------------
# 2. 데이터 로드 및 전처리
# ---------------------------------------------------------
def get_ev_data(filename):
    try:
        # 구분자 세미콜론(;)
        df = pd.read_csv(filename, sep=',')
        
        # 날짜 변환
        for col in ['connectionTime', 'disconnectTime']:
            if col in df.columns:
                df[col] = pd.to_datetime(df[col])
        
        # 숫자형 변환 (쉼표 처리)
        if df['kWhDelivered'].dtype == object:
             df['kWhDelivered'] = df['kWhDelivered'].astype(str).str.replace(',', '.').astype(float)

        # 기간 필터링
        mask = (df['connectionTime'] >= TARGET_START_DATE) & (df['connectionTime'] <= TARGET_END_DATE)
        df = df.loc[mask].copy()
        
        # 정렬
        df = df.sort_values(by='connectionTime')
        print(f"데이터 로드 성공: {len(df)}개의 충전 세션 ({TARGET_START_DATE} ~ {TARGET_END_DATE})")
        return df
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
        
        if pd.isna(arrival) or energy_needed <= 0:
            continue

        # 충전 시간 계산
        duration_hours = energy_needed / MAX_CHARGING_RATE
        finish_time = arrival + pd.Timedelta(hours=duration_hours)
        
        # 부하 더하기
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
    
    # 시간 단위 에너지 (kWh)
    energy_per_slot = MAX_CHARGING_RATE * (TIME_INTERVAL / 60.0)
    
    ev_profiles = []
    
    for _, row in ev_data.iterrows():
        # 인덱스 변환
        start_idx = time_index.searchsorted(row['connectionTime'])
        end_idx = time_index.searchsorted(row['disconnectTime'])
        
        if start_idx >= t_len or end_idx <= 0 or start_idx >= end_idx:
            continue
            
        start_idx = max(0, start_idx)
        end_idx = min(t_len, end_idx)
        
        req_energy = row['kWhDelivered']
        duration_slots = end_idx - start_idx
        
        # 초기화: 균등 분배 (Flat start)
        # 시간당 평균 파워 = 필요한 에너지 / 총 주차 시간
        avg_power = min(MAX_CHARGING_RATE, req_energy / (duration_slots * (TIME_INTERVAL/60.0)))
        profile = np.full(duration_slots, avg_power)
        
        ev_profiles.append({
            'start': start_idx, 'end': end_idx,
            'profile': profile, 'energy': req_energy
        })
        total_load_arr[start_idx:end_idx] += profile

    # Iterative Water-filling (최적화)
    iterations = 15
    for it in range(iterations):
        for ev in ev_profiles:
            s, e = ev['start'], ev['end']
            old_profile = ev['profile']
            
            # 나를 뺀 배경 부하 계산
            total_load_arr[s:e] -= old_profile
            background_load = total_load_arr[s:e]
            
            target_energy = ev['energy']
            
            # 이진 탐색으로 최적 높이 찾기
            low, high = np.min(background_load), np.max(background_load) + MAX_CHARGING_RATE
            best_profile = old_profile
            
            for _ in range(10):
                mid = (low + high) / 2
                proposed_power = np.clip(mid - background_load, 0, MAX_CHARGING_RATE)
                proposed_energy = np.sum(proposed_power) * (TIME_INTERVAL/60.0)
                
                if proposed_energy < target_energy:
                    low = mid
                else:
                    high = mid
                    best_profile = proposed_power
            
            ev['profile'] = best_profile
            total_load_arr[s:e] += best_profile
            
    return pd.Series(total_load_arr, index=time_index)

# ---------------------------------------------------------
# 5. EDF (Earliest Deadline First)
# ---------------------------------------------------------
def calculate_edf_load(ev_data, time_index, grid_capacity):
    print(f"EDF 계산 중... (Grid Cap: {grid_capacity:.1f} kW)")
    
    t_len = len(time_index)
    total_load_arr = np.zeros(t_len)
    dt_hours = TIME_INTERVAL / 60.0
    
    # 상태 객체 초기화
    ev_states = []
    for _, row in ev_data.iterrows():
        ev_states.append({
            'arrival_idx': time_index.searchsorted(row['connectionTime']),
            'departure_idx': time_index.searchsorted(row['disconnectTime']),
            'remaining_energy': row['kWhDelivered'],
            'max_rate': MAX_CHARGING_RATE
        })
        
    # Time-stepping Simulation
    for t in range(t_len):
        # 1. Active EVs 찾기 (주차 중이고 에너지 필요한 차)
        active_evs = []
        for i, ev in enumerate(ev_states):
            if ev['arrival_idx'] <= t < ev['departure_idx']: 
                if ev['remaining_energy'] > 0.001: 
                    active_evs.append(i)
        
        if not active_evs:
            continue
            
        # 2. 정렬: 마감 시간(Departure)이 빠른 순서
        active_evs.sort(key=lambda i: ev_states[i]['departure_idx'])
        
        # 3. 전력 할당
        current_grid_load = 0
        
        for ev_idx in active_evs:
            if current_grid_load >= grid_capacity:
                break # 용량 초과
            
            available = grid_capacity - current_grid_load
            
            # 이번 step에 필요한 파워 (완충하려면)
            # 남은 에너지를 한 타임스텝(dt) 안에 다 넣으려면? -> energy / dt
            # 하지만 max_rate를 넘을 순 없음
            power_needed = ev_states[ev_idx]['remaining_energy'] / dt_hours
            
            allocated_power = min(ev_states[ev_idx]['max_rate'], available, power_needed)
            
            # 상태 업데이트
            total_load_arr[t] += allocated_power
            current_grid_load += allocated_power
            ev_states[ev_idx]['remaining_energy'] -= (allocated_power * dt_hours)
            
            if ev_states[ev_idx]['remaining_energy'] < 0:
                ev_states[ev_idx]['remaining_energy'] = 0

    return pd.Series(total_load_arr, index=time_index)

# ---------------------------------------------------------
# 6. 실행 및 시각화
# ---------------------------------------------------------
if __name__ == "__main__":
    ev_df = get_ev_data(CSV_FILENAME)
    
    if not ev_df.empty:
        # 시간 축 생성 (데이터 전체 기간 포함)
        # 데이터가 듬성듬성할 수 있으므로 시작/끝 날짜를 명시적으로 지정
        start_time = pd.to_datetime(TARGET_START_DATE)
        end_time = pd.to_datetime(TARGET_END_DATE) + pd.Timedelta(days=1)
        
        time_index = pd.date_range(start=start_time, end=end_time, freq=f'{TIME_INTERVAL}min')
        
        # 1. ASAP 계산 (Baseline)
        load_asap = calculate_uncoordinated_load(ev_df, time_index)
        asap_peak = load_asap.max()
        
        # 2. 전력망 용량 제한 설정 (ASAP 피크의 60%)
        # 피크가 0이면(데이터 없음) 10kW 기본값
        GRID_CAPACITY = (asap_peak * 0.6) if asap_peak > 0 else 10.0
        
        # 3. EDF 계산
        load_edf = calculate_edf_load(ev_df, time_index, GRID_CAPACITY)
        
        # 4. Offline Optimal 계산
        load_opt = calculate_offline_optimal_load(ev_df, time_index)
        
        # 결과 출력
        print(f"\n[Result] Grid Capacity Limit: {GRID_CAPACITY:.2f} kW")
        print(f"[Result] ASAP Peak: {asap_peak:.2f} kW")
        print(f"[Result] EDF Peak:  {load_edf.max():.2f} kW")
        print(f"[Result] Optimal Peak: {load_opt.max():.2f} kW")

        # 5. 그래프 그리기
        # 전체 기간을 그리면 너무 빽빽하므로, 가장 피크가 높았던 '일주일' 또는 '하루'를 확대해서 보여주는 것이 좋음
        # 여기서는 전체 기간을 그립니다.
        
        plt.figure(figsize=(14, 6))
        
        # ASAP (Uncoordinated)
        plt.fill_between(load_asap.index, load_asap.values, color='gray', alpha=0.2, label='Uncoordinated (ASAP)')
        
        # Grid Capacity
        plt.axhline(y=GRID_CAPACITY, color='red', linestyle='--', linewidth=1.5, label='Grid Capacity Limit')
        
        # EDF
        plt.plot(load_edf.index, load_edf.values, color='blue', linewidth=1.5, label='EDF Scheduling')
        
        # Offline Optimal
        plt.plot(load_opt.index, load_opt.values, color='green', linestyle=':', linewidth=1.5, label='Offline Optimal')
        
        # 꾸미기
        plt.title(f'Load Profile Comparison ({TARGET_START_DATE} ~ {TARGET_END_DATE})', fontsize=14)
        plt.xlabel('Date & Time', fontsize=12)
        plt.ylabel('Power Demand (kW)', fontsize=12)
        plt.grid(True, linestyle='--', alpha=0.6)
        plt.legend(fontsize=11, loc='upper right', frameon=True)
        
        # X축 포맷
        plt.gca().xaxis.set_major_formatter(mdates.DateFormatter('%m-%d'))
        plt.gcf().autofmt_xdate()
        
        plt.tight_layout()
        plt.savefig(OUTPUT_FIG, dpi=300) # 사용자가 설정한 경로
        print(f"그래프가 '{OUTPUT_FIG}'로 저장되었습니다.")
        plt.show()
    else:
        print("데이터프레임이 비어있습니다.")