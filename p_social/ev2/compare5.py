'''
Created on 2025. 11. 29.
Modified for Admission Control (SGO) Comparison
'''
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.dates as mdates
import os
import copy

# ---------------------------------------------------------
# 1. 설정 (Configuration)
# ---------------------------------------------------------
SAVE_DIR = '/users/jaewoo/data/acn'
OUTPUT_FIG = os.path.join(SAVE_DIR, 'sgo_vs_fcfs_vs_edf_simulation.png')
CSV_FILENAME = os.path.join(SAVE_DIR, 'acn_data_1week.csv')

MAX_CHARGING_RATE = 6.6
TIME_INTERVAL = 5  # minutes
DT_HOURS = TIME_INTERVAL / 60.0
TARGET_START_DATE = '2019-10-01'
TARGET_END_DATE = '2019-10-08'

# TOU 기반 동적 용량 제한 (kW)
ON_PEAK_LIMIT = 20.0
OFF_PEAK_LIMIT = 40.0
ON_PEAK_START_HOUR = 12
ON_PEAK_END_HOUR = 18

# ---------------------------------------------------------
# 2. 데이터 로드 및 유틸리티
# ---------------------------------------------------------
def get_ev_data(filename):
    try:
        if not os.path.exists(filename): 
            print(f"[WARN] 파일이 없습니다: {filename}")
            return pd.DataFrame()
        df = pd.read_csv(filename)
        for col in ['connectionTime', 'disconnectTime']:
            df[col] = pd.to_datetime(df[col], utc=True)
        
        mask = (df['connectionTime'] >= TARGET_START_DATE) & (df['connectionTime'] <= f"{TARGET_END_DATE} 23:59:59")
        df = df.loc[mask].copy()
        df = df[df['kWhDelivered'] > 0]
        return df.sort_values(by='connectionTime')
    except Exception as e:
        print(f"[ERROR] 데이터 로드 중 오류: {e}")
        return pd.DataFrame()

def get_capacity_limit_profile(time_index):
    limits = []
    for t in time_index:
        hour = t.hour
        if ON_PEAK_START_HOUR <= hour < ON_PEAK_END_HOUR:
            limits.append(ON_PEAK_LIMIT)
        else:
            limits.append(OFF_PEAK_LIMIT)
    return np.array(limits)

# ---------------------------------------------------------
# 3. Admission Control을 위한 타당성 검사 (Feasibility Check)
# ---------------------------------------------------------
def check_feasibility(existing_evs, new_ev, current_time_idx, limit_profile, algorithm):
    """
    현재 충전 중인 EV들과 새로운 EV를 모두 포함했을 때, 
    미래의 스케줄링이 가능한지 가상으로 시뮬레이션합니다.
    
    Returns:
        True (수락 가능), False (수락 불가 - 데드라인 위반 발생)
    """
    # 1. 가상 환경 구성을 위해 상태 복제 (Deep Copy)
    #    새로운 EV를 리스트에 추가합니다.
    virtual_evs = [copy.deepcopy(ev) for ev in existing_evs]
    virtual_evs.append(copy.deepcopy(new_ev))
    
    # 2. 시뮬레이션 범위 설정 (현재 시점 ~ 가장 늦은 출차 시점)
    max_departure = max(ev['departure_idx'] for ev in virtual_evs)
    
    # 3. 가상 스케줄링 루프
    for t in range(current_time_idx, max_departure + 1):
        # 현재 시간에 남아있는(충전 필요한) EV 필터링
        active_virtual = [ev for ev in virtual_evs 
                          if ev['arrival_idx'] <= t < ev['departure_idx'] 
                          and ev['energy_remaining'] > 0.001]
        
        if not active_virtual:
            # 남은 차가 없는데 아직 완충 안 된 차가 있는지 확인 (이미 데드라인 지난 경우)
            unfinished = [ev for ev in virtual_evs if ev['energy_remaining'] > 0.001 and ev['departure_idx'] <= t]
            if unfinished: return False 
            if t > max_departure: break
            continue

        # 정렬 (알고리즘별 우선순위 적용)
        # if algorithm == 'FCFS':
        #     active_virtual.sort(key=lambda x: x['arrival_idx'])
        # else: # EDF & SGO
        #     active_virtual.sort(key=lambda x: x['departure_idx'])
        
        active_virtual.sort(key=lambda x: x['departure_idx'])
        
        # 용량 배분
        current_limit = limit_profile[t] if t < len(limit_profile) else OFF_PEAK_LIMIT
        current_load = 0.0

        for ev in active_virtual:
            if current_load >= current_limit: break
            
            available = current_limit - current_load
            req_power = ev['energy_remaining'] / DT_HOURS
            power = min(ev['max_rate'], available, req_power)
            
            current_load += power
            ev['energy_remaining'] -= (power * DT_HOURS)
    
    # 4. 검증: 모든 차량이 완충되었는가?
    # 약간의 부동소수점 오차 허용 (0.01 kWh)
    failures = [ev for ev in virtual_evs if ev['energy_remaining'] > 0.01]
    
    if len(failures) > 0:
        return False # 불가능 (Reject)
    else:
        return True # 가능 (Admit)

# ---------------------------------------------------------
# 4. 메인 시뮬레이션 엔진
# ---------------------------------------------------------
def run_simulation(ev_data, time_index, limit_profile, algorithm, use_admission_control):
    """
    algorithm: 'FCFS' or 'EDF'
    use_admission_control: True or False
    """
    t_len = len(time_index)
    total_load = np.zeros(t_len)
    
    # 전체 EV 목록 생성 (아직 Active 아님)
    # 시뮬레이션 효율을 위해 리스트 형태의 딕셔너리로 변환
    all_evs = []
    for i, row in ev_data.iterrows():
        all_evs.append({
            'id': i,
            'arrival_idx': time_index.searchsorted(row['connectionTime']),
            'departure_idx': time_index.searchsorted(row['disconnectTime']),
            'energy_requested': row['kWhDelivered'],
            'energy_remaining': row['kWhDelivered'], # 초기값
            'energy_delivered': 0.0,
            'max_rate': MAX_CHARGING_RATE,
            'status': 'waiting' # waiting, active, finished, rejected
        })
    
    active_evs = [] # 현재 충전소에 있는(수락된) EV들
    
    admitted_count = 0
    rejected_count = 0
    
    # 시간 단계별 시뮬레이션
    for t in range(t_len):
        # 1. 새로운 도착 차량 처리 (Arrival Event)
        new_arrivals = [ev for ev in all_evs if ev['arrival_idx'] == t]
        
        for new_ev in new_arrivals:
            if use_admission_control:
                # [Admission Control]
                # 기존 active_evs와 new_ev를 합쳤을 때 모두 완충 가능한지 확인
                is_feasible = check_feasibility(active_evs, new_ev, t, limit_profile, algorithm)
                
                if is_feasible:
                    new_ev['status'] = 'active'
                    active_evs.append(new_ev)
                    admitted_count += 1
                else:
                    new_ev['status'] = 'rejected'
                    rejected_count += 1
            else:
                # [No Admission Control] 무조건 수락
                new_ev['status'] = 'active'
                active_evs.append(new_ev)
                admitted_count += 1

        # 2. 떠나는 차량 처리 (Departure Event)
        # departure_idx가 t인 차량 제거
        # (주의: 리스트 순회하며 삭제시 역순 혹은 새 리스트 생성 권장)
        active_evs = [ev for ev in active_evs if ev['departure_idx'] > t]

        # 3. 충전 스케줄링 (Charging)
        # 현재 충전 필요한 차량 필터링
        charging_candidates = [ev for ev in active_evs if ev['energy_remaining'] > 0.001]
        
        if not charging_candidates:
            continue
            
        # 정렬 (스케줄링 정책)
        if algorithm == 'FCFS':
            charging_candidates.sort(key=lambda x: x['arrival_idx'])
        elif algorithm == 'EDF':
            charging_candidates.sort(key=lambda x: x['departure_idx'])
            
        current_limit = limit_profile[t]
        current_load_t = 0.0
        
        for ev in charging_candidates:
            if current_load_t >= current_limit: 
                break # 용량 초과 시 중단
            
            available = current_limit - current_load_t
            req_power = ev['energy_remaining'] / DT_HOURS
            power = min(ev['max_rate'], available, req_power)
            
            # 상태 업데이트
            current_load_t += power
            total_load[t] += power
            ev['energy_remaining'] -= (power * DT_HOURS)
            ev['energy_delivered'] += (power * DT_HOURS)
            
            if ev['energy_remaining'] < 0: 
                ev['energy_remaining'] = 0

    # 결과 분석
    # 완충 기준: 요청량의 99% 이상 충전
    fully_charged_count = 0
    total_energy_delivered = 0.0
    
    for ev in all_evs:
        if ev['status'] == 'rejected':
            continue # 거절된 차는 완충 실패로 간주 (카운트 안함)
        
        if (ev['energy_delivered'] / ev['energy_requested']) >= 0.99:
            fully_charged_count += 1
        
        total_energy_delivered += ev['energy_delivered']

    return {
        'load_profile': total_load,
        'fully_charged_count': fully_charged_count,
        'admitted_count': admitted_count,
        'rejected_count': rejected_count,
        'total_energy': total_energy_delivered
    }

# ---------------------------------------------------------
# 5. 메인 실행 및 시각화
# ---------------------------------------------------------
if __name__ == "__main__":
    ev_df = get_ev_data(CSV_FILENAME)
    
    if not ev_df.empty:
        start = pd.to_datetime(TARGET_START_DATE, utc=True)
        end = pd.to_datetime(TARGET_END_DATE, utc=True) + pd.Timedelta(days=1)
        time_index = pd.date_range(start, end, freq=f'{TIME_INTERVAL}min')
        limit_profile = get_capacity_limit_profile(time_index)
        
        total_evs = len(ev_df)
        print(f"Total EVs: {total_evs}")
        print(f"Simulation Limits: Off-peak {OFF_PEAK_LIMIT}kW, On-peak {ON_PEAK_LIMIT}kW")
        print("-" * 60)

        # --- 1. SGO (EDF + Admission Control) ---
        print("Running SGO (EDF + Admission Control)...")
        res_sgo = run_simulation(ev_df, time_index, limit_profile, 'EDF', use_admission_control=True)
        
        # --- 2. FCFS + Admission Control ---
        print("Running FCFS + Admission Control...")
        res_fcfs_ac = run_simulation(ev_df, time_index, limit_profile, 'FCFS', use_admission_control=True)
        
        # --- 3. EDF (No Admission Control) ---
        print("Running EDF (No Admission Control)...")
        res_edf_no_ac = run_simulation(ev_df, time_index, limit_profile, 'EDF', use_admission_control=False)

        # --- 결과 출력 ---
        results = [
            ('SGO (EDF+AC)', res_sgo),
            ('FCFS + AC', res_fcfs_ac),
            ('EDF (No AC)', res_edf_no_ac)
        ]
        
        print("-" * 60)
        print(f"{'Algorithm':<20} | {'Fully Charged':<15} | {'Admitted':<10} | {'Rejected':<10}")
        print("-" * 60)
        for name, res in results:
            full_rate = (res['fully_charged_count'] / total_evs) * 100
            print(f"{name:<20} | {res['fully_charged_count']}/{total_evs} ({full_rate:.1f}%) | "
                  f"{res['admitted_count']:<10} | {res['rejected_count']:<10}")
        print("-" * 60)

        # --- 그래프 그리기 ---
        fig, axes = plt.subplots(3, 1, figsize=(14, 12), sharex=False)
        
        # 1. Load Profile 비교
        ax1 = axes[0]
        ax1.step(time_index, limit_profile, color='red', linestyle='--', label='Limit', where='post', alpha=0.5)
        ax1.plot(time_index, res_sgo['load_profile'], color='green', alpha=0.7, label='SGO (EDF+AC)')
        ax1.plot(time_index, res_edf_no_ac['load_profile'], color='blue', alpha=0.4, label='EDF (No AC)')
        ax1.set_title('Load Profile Comparison: SGO vs EDF(No AC)')
        ax1.set_ylabel('Power (kW)')
        ax1.legend(loc='upper right')
        ax1.grid(True, alpha=0.3)
        ax1.xaxis.set_major_formatter(mdates.DateFormatter('%m-%d %H'))

        # 2. Fully Charged Rate (Bar Chart)
        ax2 = axes[1]
        labels = [r[0] for r in results]
        # values = [(r[1]['fully_charged_count'] / total_evs) * 100 for r in results]
        values = [(r[1]['fully_charged_count'] / r[1]['admitted_count'] ) * 100 for r in results]
        colors = ['forestgreen', 'orange', 'royalblue']
        
        bars = ax2.bar(labels, values, color=colors, width=0.5)
        ax2.set_ylabel('Fully Charged Rate (%)')
        ax2.set_title('Service Satisfaction (Fully Charged / Total Arrivals)')
        ax2.set_ylim(0, 110)
        
        for bar in bars:
            height = bar.get_height()
            ax2.text(bar.get_x() + bar.get_width()/2., height + 1, f'{height:.1f}%', 
                     ha='center', va='bottom', fontsize=12, fontweight='bold')

        # 3. Admitted vs Rejected (Stacked Bar)
        ax3 = axes[2]
        admitted = [r[1]['admitted_count'] for r in results]
        rejected = [r[1]['rejected_count'] for r in results]
        
        # EDF(No AC)는 Rejected가 0이므로 시각적으로 비교가 됨
        ax3.bar(labels, admitted, label='Admitted', color='cornflowerblue', width=0.5)
        ax3.bar(labels, rejected, bottom=admitted, label='Rejected (Admission Control)', color='salmon', width=0.5)
        
        ax3.set_ylabel('Number of EVs')
        ax3.set_title('Admission Control Impact: Admitted vs Rejected')
        ax3.legend()
        
        # 수치 텍스트 추가
        for i, (adm, rej) in enumerate(zip(admitted, rejected)):
            ax3.text(i, adm/2, str(adm), ha='center', va='center', color='white', fontweight='bold')
            if rej > 0:
                ax3.text(i, adm + rej/2, str(rej), ha='center', va='center', color='white', fontweight='bold')

        plt.tight_layout()
        
        if not os.path.exists(SAVE_DIR):
            os.makedirs(SAVE_DIR, exist_ok=True)
            
        plt.savefig(OUTPUT_FIG, dpi=300)
        print(f"[SUCCESS] 결과 그래프 저장: {OUTPUT_FIG}")
        # plt.show() # 필요시 주석 해제

    else:
        print("[ERROR] 데이터가 없습니다.")