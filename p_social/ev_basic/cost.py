'''
Created on 2025. 11. 28.

@author: jaewoo
'''
import pandas as pd
import matplotlib.pyplot as plt

SAVE_DIR = '/users/jaewoo/data/acn'
OUTPUT_FIG=SAVE_DIR+'/cost.png'

# ---------------------------------------------------------
# 1. 시뮬레이션 결과 입력 (앞선 코드에서 나온 결과값 대입)
# ---------------------------------------------------------
# 예시 값입니다. 실제 baseline_load_profile.py 실행 결과를 여기에 넣으세요.
peak_uncoordinated = 45.5  # kW (기존 방식 피크)
peak_optimized = 28.2      # kW (제안 방식 피크: Capacity Limit)

total_energy_kwh = 1500    # kWh (일주일간 총 충전량, 방식 상관없이 동일하다고 가정)

# ---------------------------------------------------------
# 2. 요금제 설정 (SCE 상업용 요금제 참조)
# ---------------------------------------------------------
# Demand Charge: kW당 부과되는 요금 (피크가 높으면 비싸짐)
DEMAND_CHARGE_RATE = 18.60  # $ per kW (월간)

# Energy Charge: kWh당 부과되는 요금 (평균 단가 가정)
ENERGY_CHARGE_RATE = 0.15   # $ per kWh

# ---------------------------------------------------------
# 3. 비용 계산 로직
# ---------------------------------------------------------
def calculate_monthly_bill(peak_kw, total_kwh):
    # 1. 피크 요금 (Demand Cost)
    demand_cost = peak_kw * DEMAND_CHARGE_RATE
    
    # 2. 사용량 요금 (Energy Cost)
    energy_cost = total_kwh * ENERGY_CHARGE_RATE
    
    total_cost = demand_cost + energy_cost
    return demand_cost, energy_cost, total_cost

# 계산 수행
dem_base, en_base, tot_base = calculate_monthly_bill(peak_uncoordinated, total_energy_kwh)
dem_opt, en_opt, tot_opt = calculate_monthly_bill(peak_optimized, total_energy_kwh)

# 절감액 계산
savings_monthly = tot_base - tot_opt
savings_annual = savings_monthly * 12

# 결과 출력
print(f"--- Economic Analysis Results ---")
print(f"[Baseline] Monthly Bill: ${tot_base:.2f} (Demand: ${dem_base:.2f})")
print(f"[Proposed] Monthly Bill: ${tot_opt:.2f} (Demand: ${dem_opt:.2f})")
print(f"---------------------------------")
print(f"Monthly Savings: ${savings_monthly:.2f}")
print(f"Annual Savings : ${savings_annual:.2f}")
print(f"Cost Reduction : {((tot_base - tot_opt) / tot_base) * 100:.1f}%")

# ---------------------------------------------------------
# 4. 그래프 그리기 (Bar Chart)
# ---------------------------------------------------------
labels = ['Uncoordinated', 'Proposed (EDF)']
demand_costs = [dem_base, dem_opt]
energy_costs = [en_base, en_opt]

width = 0.5
fig, ax = plt.subplots(figsize=(8, 6))

# 누적 막대 그래프 (Energy Cost 위에 Demand Cost 쌓기)
p1 = ax.bar(labels, energy_costs, width, label='Energy Cost ($)', color='lightgray')
p2 = ax.bar(labels, demand_costs, width, bottom=energy_costs, label='Demand Cost ($)', color='royalblue')

ax.set_ylabel('Monthly Electricity Bill ($)', fontsize=12)
ax.set_title('Economic Comparison: Monthly Electricity Cost', fontsize=14)
ax.legend()

# 값 표시
for i, (d_cost, e_cost) in enumerate(zip(demand_costs, energy_costs)):
    # Total 값
    ax.text(i, d_cost + e_cost + 5, f"${d_cost + e_cost:.0f}", ha='center', fontweight='bold')
    # Demand 절감액 표시
    if i == 1:
        savings = demand_costs[0] - demand_costs[1]
        ax.annotate(f"Save ${savings:.0f}/mo\n(Demand Charge)", 
                    xy=(1, d_cost + e_cost/2), 
                    xytext=(1.6, d_cost + e_cost/2),
                    arrowprops=dict(arrowstyle='->', color='red'),
                    color='red', fontweight='bold', ha='center')

plt.tight_layout()
plt.savefig(OUTPUT_FIG, dpi=300)
plt.show()