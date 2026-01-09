'''
Created on 2026. 1. 8.

@author: jaewoo
'''
import numpy as np
import pandas as pd
import os

PATH='/users/jaewoo/data/mix_sec/data'


def uunifast(n, u_total):
    """
    UUniFast 알고리즘:
    n개의 태스크에 대해 총 이용률 u_total을 랜덤하게 분배합니다.
    (O(n) 복잡도로 균일한 분포를 보장하는 표준 알고리즘)
    """
    vect = []
    sum_u = u_total
    for i in range(1, n):
        # 기하학적 분포를 이용한 다음 이용률 계산
        next_sum_u = sum_u * (np.random.random() ** (1.0 / (n - i)))
        vect.append(sum_u - next_sum_u)
        sum_u = next_sum_u
    vect.append(sum_u)
    return np.array(vect)

def generate_task_sets_to_csv():
    # --- 설정 파라미터 ---
    n_tasks = 10                  # 세트 당 태스크 수
    num_sets = 500                # 이용률 별 생성할 세트 수
    prob_hi = 0.5                 # HI Criticality 태스크 비율 (50%)
    
    # 이용률 범위 (0.6 ~ 1.0, 0.05 단위)
    utilization_bounds = np.arange(0.6, 1.01, 0.05)
    
    # 파라미터 계수 범위
    period_range = (100, 1000)    # 주기 T 범위 (Log-uniform 분포 사용 예정)
    hi_factor_range = (1.5, 2.5)  # C_H는 C_L의 1.5~2.5배
    detect_factor_range = (0.1, 0.3) # C_D는 C_L의 0.1~0.3배 (10%~30%)
    
    # 저장 디렉토리 생성
    output_dir =PATH
    os.makedirs(output_dir, exist_ok=True)
    
    print(f"Generating task sets in '{output_dir}'...")

    # --- 생성 루프 ---
    for u_target in utilization_bounds:
        u_target = round(u_target, 2) # 부동소수점 오차 방지
        all_tasks_data = []
        
        for set_idx in range(num_sets):
            # 1. UUniFast로 이용률 분배
            u_vec = uunifast(n_tasks, u_target)
            
            # 2. 주기(Period) 생성 (Log-uniform 분포: 100~1000 사이에서 골고루 퍼지게)
            periods = np.exp(np.random.uniform(np.log(period_range[0]), np.log(period_range[1]), n_tasks))
            periods = np.round(periods).astype(int)
            
            # 3. Criticality 할당 (LO or HI)
            criticalities = np.random.choice(['LO', 'HI'], size=n_tasks, p=[1-prob_hi, prob_hi])
            
            for i in range(n_tasks):
                # 기본 변수 할당
                u_l = u_vec[i]
                T = periods[i]
                crit = criticalities[i]
                
                # C_L (LO 모드 실행 시간) 계산
                C_L = u_l * T
                
                # C_D (Detection 시간) 계산: C_L의 10~30%
                detect_ratio = np.random.uniform(detect_factor_range[0], detect_factor_range[1])
                C_D = C_L * detect_ratio
                
                # C_H (HI 모드 실행 시간) 계산
                if crit == 'HI':
                    # HI 태스크는 C_L보다 더 오래 걸림
                    hi_factor = np.random.uniform(hi_factor_range[0], hi_factor_range[1])
                    C_H = C_L * hi_factor
                else:
                    # LO 태스크는 HI 모드에서 추가 실행 시간이 없거나 동일함 (여기선 동일하게 표기)
                    C_H = C_L 

                # 리스트에 추가
                all_tasks_data.append({
                    'set_id': set_idx,
                    'task_id': i,
                    'T': T,
                    'C_D': C_D,
                    'C_L': C_L,
                    'C_H': C_H,
                    'criticality': crit,
                    'u_generated': u_l # 검증용 원본 이용률
                })
        
        # DataFrame 변환 및 CSV 저장
        df = pd.DataFrame(all_tasks_data)
        file_path = os.path.join(output_dir, f"task_sets_u_{u_target:.2f}.csv")
        df.to_csv(file_path, index=False)
        print(f" -> Saved: {file_path} (Total {num_sets} sets)")

if __name__ == "__main__":
    generate_task_sets_to_csv()