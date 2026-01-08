'''
Created on 2026. 1. 8.

@author: jaewoo
'''
import pandas as pd
import numpy as np

def solve_edf_vd_extension(csv_path):
    """
    Detection 기반 EDF-VD 확장 모델의 스케줄링 가능성 판별
    
    CSV 컬럼 요구사항:
    - task_id: 태스크 ID
    - T: 주기 (Period)
    - C_D: Detection 실행 시간
    - C_L: LO 모드 WCET (C_D 포함)
    - C_H: HI 모드 WCET (C_D 포함)
    - criticality: 'HI' 또는 'LO'
    """
    
    # 1. 데이터 로드
    try:
        df = pd.read_csv(csv_path)
    except FileNotFoundError:
        return "Error: 파일을 찾을 수 없습니다."

    # 2. Utilization 계산 (u = C/T)
    df['u_D'] = df['C_D'] / df['T']
    df['u_L'] = df['C_L'] / df['T']
    df['u_H'] = df['C_H'] / df['T']

    # 태스크 분리
    hi_tasks = df[df['criticality'] == 'HI'].copy()
    lo_tasks = df[df['criticality'] == 'LO'].copy()

    # 3. 기본 이용률 합 계산 (LO 태스크들의 합)
    # LO 모드 식에서 LO 태스크는 가상 데드라인 없이 원래 u_L 사용
    sum_u_L_lo_tasks = lo_tasks['u_L'].sum()

    # 빠른 실패 조건 (Sanity Check)
    # HI 모드에서 HI 태스크들의 기본 이용률 합이 1을 넘으면 절대 불가능
    if hi_tasks['u_H'].sum() > 1.0:
        return "Unschedulable (Sum of HI utilization > 1)"
    
    # LO 모드에서 모든 태스크의 LO 이용률 합이 1을 넘으면 (일반 EDF) 불가능 가능성 높음
    if (hi_tasks['u_L'].sum() + sum_u_L_lo_tasks) > 1.0:
        # EDF-VD에서는 x 조정을 통해 가능할 수도 있으니 경고만 하고 진행하거나,
        # 일반적인 경우에는 여기서도 fail할 수 있으나, 알고리즘상 x 탐색으로 넘깁니다.
        pass

    # 4. x 탐색 (Search x in (0, 1))
    # 정밀도는 step으로 조절 (예: 0.001)
    # x가 너무 0에 가깝거나 1에 가까우면 분모가 0이 되어 계산 불가하므로 범위 제한
    step = 0.001
    search_range = np.arange(step, 1.0, step)
    
    valid_x_values = []

    for x in search_range:
        # --- 조건 1: LO Mode Check ---
        # HI 태스크들의 기여도 계산
        # term1 = u_D / x
        # term2 = (u_L - u_D) / (1 - x)
        
        # 벡터 연산으로 모든 HI 태스크에 대해 계산
        term1 = hi_tasks['u_D'] / x
        term2 = (hi_tasks['u_L'] - hi_tasks['u_D']) / (1 - x)
        
        # 각 태스크별 max 값
        hi_contribution_lo_mode = np.maximum(term1, term2).sum()
        
        lhs_lo = hi_contribution_lo_mode + sum_u_L_lo_tasks
        
        if lhs_lo > 1.0:
            continue # LO 모드 조건 불만족, 다음 x로

        # --- 조건 2: HI Mode Check ---
        # term3 = (u_H - u_D) / (1 - x)
        # term4 = u_H
        
        term3 = (hi_tasks['u_H'] - hi_tasks['u_D']) / (1 - x)
        term4 = hi_tasks['u_H']
        
        hi_contribution_hi_mode = np.maximum(term3, term4).sum()
        
        lhs_hi = hi_contribution_hi_mode
        
        if lhs_hi > 1.0:
            continue # HI 모드 조건 불만족, 다음 x로

        # 두 조건을 모두 만족하면 유효한 x임
        valid_x_values.append(x)

    # 5. 결과 반환
    if valid_x_values:
        # 가장 넓은 범위를 확보하거나 중간값을 선택하는 정책을 쓸 수 있음.
        # 여기서는 첫 번째 발견된 값과 범위를 출력
        best_x = valid_x_values[len(valid_x_values)//2] # 중간값 선택
        min_x = min(valid_x_values)
        max_x = max(valid_x_values)
        return f"Schedulable! (Valid x range: {min_x:.3f} ~ {max_x:.3f}, Selected x: {best_x:.3f})"
    else:
        return "Unschedulable (No valid x found)"

# --- 사용 예시 ---

# 실제 사용 시에는 'tasks.csv' 같은 파일 경로를 넣으세요.
print("=== Task Set Analysis ===")
result = solve_edf_vd_extension("test.csv")
print(result)