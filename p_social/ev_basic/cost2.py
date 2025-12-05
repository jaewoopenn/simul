'''
Created on 2025. 11. 29.

@author: jaewoo
'''
import matplotlib.pyplot as plt
import numpy as np
import os

SAVE_DIR = '/users/jaewoo/data/acn'
OUTPUT_FIG = os.path.join(SAVE_DIR, 'capex_savings_analysis.png')

# ---------------------------------------------------------
# 1. 가정 (Assumptions for Business Case)
# ---------------------------------------------------------
# FCFS 피크: 제한이 없을 때 실제 발생했던 최대 피크 (데이터 분석 결과)
PEAK_REQUIRED_FCFS = 48.5  # kW 
# Fair-EDF 운영 제한: 우리가 목표로 하는 빡빡한 용량 제한
PEAK_REQUIRED_EDF = 20.0   # kW

# 변압기/인프라 증설 비용 가정 (kW당 설치비)
# 상업용 건물 기준, kW당 약 $200 ~ $500 (공사비 포함) 가정
INFRA_COST_PER_KW = 400  # $

# ---------------------------------------------------------
# 2. 계산
# ---------------------------------------------------------
capex_fcfs = PEAK_REQUIRED_FCFS * INFRA_COST_PER_KW
capex_edf = PEAK_REQUIRED_EDF * INFRA_COST_PER_KW
savings = capex_fcfs - capex_edf

# ---------------------------------------------------------
# 3. 그래프 그리기
# ---------------------------------------------------------
def plot_capex():
    # [수정] 라벨을 'Online EDF' -> 'Fair-EDF'로 변경하여 논문 내용과 일치시킴
    labels = ['Unmanaged (FCFS)\nRequires Upgrade', 'Managed (Fair-EDF)\nExisting Infra']
    values = [capex_fcfs, capex_edf]
    
    fig, ax = plt.subplots(figsize=(8, 6))
    
    # 막대 그래프
    bars = ax.bar(labels, values, color=['lightgray', 'forestgreen'], width=0.5)
    
    # 꾸미기
    ax.set_ylabel('Required Infrastructure Investment ($)', fontsize=12)
    ax.set_title('CAPEX Savings via Virtual Capacity Management', fontsize=14, fontweight='bold')
    ax.set_ylim(0, max(values) * 1.2)
    ax.grid(True, axis='y', linestyle='--', alpha=0.3)
    
    # 값 표시
    for bar in bars:
        height = bar.get_height()
        ax.text(bar.get_x() + bar.get_width()/2., height,
                f'${height:,.0f}', ha='center', va='bottom', fontsize=12, fontweight='bold')
        
    # 절감액 화살표 및 텍스트
    ax.annotate(f"AVOIDED COST\n${savings:,.0f}", 
                xy=(1, capex_edf), 
                xytext=(0.5, (capex_fcfs + capex_edf)/2),
                arrowprops=dict(arrowstyle='<->', color='red', lw=1.5),
                color='red', fontsize=12, fontweight='bold', ha='center',
                bbox=dict(boxstyle="round,pad=0.3", fc="white", ec="red", alpha=0.8))

    plt.tight_layout()
    
    if not os.path.exists(SAVE_DIR):
        try: os.makedirs(SAVE_DIR)
        except: pass
        
    plt.savefig(OUTPUT_FIG, dpi=300)
    print(f"[SUCCESS] 그래프 저장 완료: {OUTPUT_FIG}")
    # plt.show()

if __name__ == "__main__":
    plot_capex()