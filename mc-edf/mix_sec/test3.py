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

PATH='/users/jaewoo/data/mix_sec'

# ==========================================
# 1. Core Logic (Common Math)
# ==========================================

def _core_edf_vd_search(df, u_d_col_name):
    """
    EDF-VD 스타일의 x 탐색 로직을 수행하는 내부 함수.
    u_d_col_name에 따라 'Proposed'와 'Standard'가 갈립니다.
    """
    # 데이터 추출
    u_l = df['u_L'].values
    u_h = df['u_H'].values
    u_d_eff = df[u_d_col_name].values # Effective u_D (Detection or LO execution)

    is_hi = (df['criticality'] == 'HI').values
    is_lo = ~is_hi

    # Basic Sanity Check
    if np.sum(u_h[is_hi]) > 1.0: return False

    # LO 태스크들의 이용률 합
    sum_u_L_lo_tasks = np.sum(u_l[is_lo])

    # HI 태스크 데이터 (벡터화)
    hi_u_d = u_d_eff[is_hi]
    hi_u_l = u_l[is_hi]
    hi_u_h = u_h[is_hi]

    # x 탐색 (Search range: 0 < x < 1)
    # 정밀도 조절: step이 작을수록 정확하지만 느림
    step = 0.005 
    search_range = np.arange(step, 1.0, step)

    for x in search_range:
        # [Condition 1: LO Mode]
        # HI Task: max(u_D/x, (u_L - u_D)/(1-x))
        term1 = hi_u_d / x
        term2 = (hi_u_l - hi_u_d) / (1 - x)
        
        hi_contribution_lo = np.sum(np.maximum(term1, term2))
        
        if (hi_contribution_lo + sum_u_L_lo_tasks) > 1.0:
            continue 

        # [Condition 2: HI Mode]
        # HI Task: max( (u_H - u_D)/(1-x), u_H )
        term3 = (hi_u_h - hi_u_d) / (1 - x)
        term4 = hi_u_h
        
        hi_contribution_hi = np.sum(np.maximum(term3, term4))
        
        if hi_contribution_hi > 1.0:
            continue
        
        # Found valid x
        return True

    return False

# ==========================================
# 2. Scheduling Algorithms (Implement Here)
# ==========================================

def sched_proposed_detection(df):
    """
    [Proposed] Detection 실행 후 모드 스위치 결정.
    Detection Overhead(C_D)만 고려함.
    """
    return _core_edf_vd_search(df, u_d_col_name='u_D')

def sched_baseline_standard_edf_vd(df):
    """
    [Baseline 1] Standard EDF-VD.
    LO 실행이 끝난 후(C_L) 모드 스위치 결정.
    논리적으로 u_D 자리에 u_L을 대입한 것과 같음.
    """
    return _core_edf_vd_search(df, u_d_col_name='u_L')

def sched_baseline_naive_edf(df):
    """
    [Baseline 2] Naive EDF (No Mode Switch).
    최악의 경우(HI Mode)를 기준으로 모든 태스크를 스케줄링.
    Sum(u_H for all tasks) <= 1 이어야 함.
    """
    # LO 태스크는 HI 모드에서도 C_L 만큼 돈다고 가정 (혹은 그대로)
    total_util = df['u_H'].sum() # u_H 컬럼에 LO태스크는 u_L값이 들어가 있음
    return total_util <= 1.0

# ---------------------------------------------------------
# ★ 여기에 새로운 알고리즘을 추가하세요
# def sched_new_algorithm(df):
#     ... logic ...
#     return True/False
# ---------------------------------------------------------

# 알고리즘 등록 (이름: 함수)
# 이 딕셔너리에 추가하면 자동으로 실험에 포함됩니다.
ALGORITHM_REGISTRY = {
    "Proposed (Early Detect)": sched_proposed_detection,
    "Standard EDF-VD": sched_baseline_standard_edf_vd,
    "Naive EDF": sched_baseline_naive_edf,
    # "New Algo": sched_new_algorithm
}

# ==========================================
# 3. Experiment Runner
# ==========================================

def run_comparison_experiment(data_dir="task_data"):
    file_pattern = os.path.join(data_dir, "task_sets_u_*.csv")
    files = sorted(glob.glob(file_pattern))
    
    if not files:
        print("Error: 데이터 파일이 없습니다.")
        return None

    # 결과 저장을 위한 구조
    # results = { '0.60': {'Algo1': 0.9, 'Algo2': 0.8}, ... }
    final_stats = []

    print(f"Analyzing {len(files)} utilization points with {len(ALGORITHM_REGISTRY)} algorithms...")

    for file_path in files:
        # Utilization 파싱
        match = re.search(r"u_(\d+\.\d+)", file_path)
        if not match: continue
        u_val = float(match.group(1))
        
        # 데이터 로드 및 전처리
        df = pd.read_csv(file_path)
        df['u_D'] = df['C_D'] / df['T']
        df['u_L'] = df['C_L'] / df['T']
        df['u_H'] = df['C_H'] / df['T']
        
        grouped = df.groupby('set_id')
        total_sets = len(grouped)
        
        # 각 알고리즘 별 성공 횟수 카운터 초기화
        success_counts = {name: 0 for name in ALGORITHM_REGISTRY.keys()}
        
        # 태스크 셋 별 시뮬레이션
        for _, group in grouped:
            for algo_name, algo_func in ALGORITHM_REGISTRY.items():
                if algo_func(group):
                    success_counts[algo_name] += 1
        
        # 결과 기록
        row = {'Utilization': u_val}
        for name, count in success_counts.items():
            row[name] = count / total_sets
            
        final_stats.append(row)
        
        # 진행 상황 출력
        print(f" > U={u_val:.2f} Done. Proposed: {row['Proposed (Early Detect)']*100:.1f}%")

    return pd.DataFrame(final_stats)

def plot_benchmark_results(df_res):
    plt.figure(figsize=(10, 7))
    
    # 마커와 스타일 정의
    markers = ['o', 's', '^', 'D', 'x', '*']
    linestyles = ['-', '--', '-.', ':', '-', '--']
    
    for i, algo_name in enumerate(ALGORITHM_REGISTRY.keys()):
        style_idx = i % len(markers)
        plt.plot(df_res['Utilization'], df_res[algo_name], 
                 marker=markers[style_idx], 
                 linestyle=linestyles[style_idx], 
                 linewidth=2, 
                 label=algo_name)
    
    plt.title('Schedulability Analysis Comparison', fontsize=16)
    plt.xlabel('Normalized Utilization Bound', fontsize=14)
    plt.ylabel('Acceptance Ratio', fontsize=14)
    plt.ylim(-0.05, 1.05)
    plt.grid(True, linestyle=':', alpha=0.6)
    plt.legend(fontsize=12)
    
    save_path = PATH+'/benchmark_comparison.png'
    plt.savefig(save_path)
    print(f"\nGraph saved to '{save_path}'")
    plt.show()

# ==========================================
# 4. Main Execution
# ==========================================
if __name__ == "__main__":
    results_df = run_comparison_experiment(PATH+"/data")
    if results_df is not None:
        print("\n=== Experiment Results ===")
        print(results_df)
        plot_benchmark_results(results_df)