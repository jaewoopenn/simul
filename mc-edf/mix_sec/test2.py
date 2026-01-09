'''
Created on 2026. 1. 8.

@author: jaewoo
'''

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import glob
import os
import re

PATH='/users/jaewoo/data/mix_sec/data'

def check_schedulability(df_task_set, is_baseline=False):
    """
    한 태스크 셋에 대해 스케줄링 가능 여부를 판단합니다.
    is_baseline=True 이면, C_D 대신 C_L을 사용하여 기존 EDF-VD를 시뮬레이션합니다.
    """
    # 1. 데이터 전처리
    # Baseline인 경우, 논리적으로 Detection이 LO 실행 종료 시점에 일어남 -> C_D = C_L
    if is_baseline:
        u_d_eff = df_task_set['u_L'].values # Effective u_D
    else:
        u_d_eff = df_task_set['u_D'].values # Actual u_D
        
    u_l = df_task_set['u_L'].values
    u_h = df_task_set['u_H'].values
    
    # Criticality 마스크 (boolean array)
    is_hi = (df_task_set['criticality'] == 'HI').values
    is_lo = ~is_hi

    # --- Pre-check (Sanity Check) ---
    # HI 모드일 때 HI 태스크들의 순수 이용률 합이 1을 넘으면 절대 불가
    if np.sum(u_h[is_hi]) > 1.0:
        return False
    
    # LO 모드 이용률 합 (LO 태스크)
    sum_u_L_lo_tasks = np.sum(u_l[is_lo])

    # HI 태스크 데이터 추출 (벡터 연산 최적화용)
    hi_u_d = u_d_eff[is_hi]
    hi_u_l = u_l[is_hi]
    hi_u_h = u_h[is_hi]
    
    # LO 모드 전체 합이 1 이하면 일반 EDF로도 가능 (VD 불필요)
    # 하지만 EDF-VD 로직을 타야 하므로 x 탐색 진행
    
    # --- x 탐색 (Search x in (0, 1)) ---
    # x: 가상 데드라인 비율 (Virtual Deadline Scaling Factor)
    # x가 작을수록 HI 태스크를 미리 당겨서 실행 (LO 모드 부담 증가)
    # x가 클수록 HI 태스크를 늦게 실행 (HI 모드 부담 증가)
    
    step = 0.005 # 속도를 위해 0.005 단위 탐색 (정밀도 조절 가능)
    search_range = np.arange(step, 1.0, step)
    
    for x in search_range:
        # [Condition 1: LO Mode]
        # 식: Sum_{HI} max(u_D/x, (u_L - u_D)/(1-x)) + Sum_{LO} u_L <= 1
        
        term1 = hi_u_d / x
        term2 = (hi_u_l - hi_u_d) / (1 - x)
        
        # HI 태스크들의 기여도 (Element-wise max)
        hi_load_lo_mode = np.sum(np.maximum(term1, term2))
        
        if (hi_load_lo_mode + sum_u_L_lo_tasks) > 1.0:
            continue # LO 모드 위반, x 증가 필요 (x가 커지면 term1 감소)

        # [Condition 2: HI Mode]
        # 식: Sum_{HI} max( (u_H - u_D)/(1-x), u_H ) <= 1
        # Baseline의 경우 u_D 자리에 u_L이 들어갔으므로 (u_H - u_L)/(1-x)가 됨 (carry-over work density)
        
        term3 = (hi_u_h - hi_u_d) / (1 - x)
        term4 = hi_u_h
        
        hi_load_hi_mode = np.sum(np.maximum(term3, term4))
        
        if hi_load_hi_mode > 1.0:
            continue # HI 모드 위반
            
        # 두 조건을 모두 만족하는 x가 하나라도 있으면 Schedulable
        return True

    return False

def run_experiment():
    # 파일 목록 가져오기 (task_data 폴더)
    file_pattern = os.path.join(PATH, "task_sets_u_*.csv")
    files = sorted(glob.glob(file_pattern))
    
    if not files:
        print("Error: 'task_data' 폴더에 CSV 파일이 없습니다. 먼저 데이터 생성 코드를 실행하세요.")
        return

    results = []

    print(f"{'Utilization':<12} | {'Proposed (%)':<12} | {'Baseline (%)':<12}")
    print("-" * 45)

    for file_path in files:
        # 파일명에서 Utilization 추출 (예: task_sets_u_0.60.csv -> 0.60)
        match = re.search(r"u_(\d+\.\d+)", file_path)
        if match:
            u_val = float(match.group(1))
        else:
            continue

        # 데이터 로드
        df = pd.read_csv(file_path)
        
        # 이용률 계산 미리 해두기 (성능 최적화)
        df['u_D'] = df['C_D'] / df['T']
        df['u_L'] = df['C_L'] / df['T']
        df['u_H'] = df['C_H'] / df['T']
        
        # 태스크 셋 별로 그룹화
        grouped = df.groupby('set_id')
        
        total_sets = 0
        proposed_success = 0
        baseline_success = 0
        
        for set_id, group in grouped:
            total_sets += 1
            
            # Proposed 알고리즘 (Early Detection)
            if check_schedulability(group, is_baseline=False):
                proposed_success += 1
            
            # Baseline 알고리즘 (Detection at the end of LO exec)
            if check_schedulability(group, is_baseline=True):
                baseline_success += 1
        
        # 비율 계산
        prop_ratio = proposed_success / total_sets
        base_ratio = baseline_success / total_sets
        
        results.append({
            'Utilization': u_val,
            'Proposed': prop_ratio,
            'Baseline': base_ratio
        })
        
        print(f"{u_val:<12.2f} | {prop_ratio*100:<11.1f} | {base_ratio*100:<11.1f}")

    return pd.DataFrame(results)

def plot_results(df_res):
    plt.figure(figsize=(10, 6))
    
    plt.plot(df_res['Utilization'], df_res['Proposed'], 'o-', label='Proposed (Early Detection)', linewidth=2)
    plt.plot(df_res['Utilization'], df_res['Baseline'], 's--', label='Baseline (Standard EDF-VD)', linewidth=2, color='red')
    
    plt.title('Schedulability Comparison: Proposed vs Baseline', fontsize=15)
    plt.xlabel('Normalized Utilization Bound', fontsize=12)
    plt.ylabel('Acceptance Ratio', fontsize=12)
    plt.ylim(-0.05, 1.05)
    plt.grid(True, linestyle=':', alpha=0.6)
    plt.legend(fontsize=12)
    
    # 결과 저장 혹은 보여주기
    plt.savefig('comparison_result.png')
    print("\nResult graph saved as 'comparison_result.png'")
    plt.show()

# --- 실행 ---
if __name__ == "__main__":
    df_results = run_experiment()
    if df_results is not None:
        plot_results(df_results)