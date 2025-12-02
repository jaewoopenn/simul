import numpy as np
import matplotlib.pyplot as plt
from scipy.stats import norm

# 1. 설정: 시간 축 (오전 6시 ~ 오후 8시)
x = np.linspace(6, 20, 500)  # 6:00 to 20:00

# 2. 공급(Supply): 고정된 전력망 용량 (Grid Capacity)
capacity_limit = 75
y_capacity = np.full_like(x, capacity_limit)

# 3. 수요(Demand): 비제어 충전 수요 (Uncoordinated Charging)
# 오전 9시(9.0)를 중심으로 하는 정규분포(Gaussian)를 사용하여 피크를 모델링
# loc=9.0 (오전 9시 피크), scale=1.5 (분산 정도)
y_demand_norm = norm.pdf(x, loc=9.0, scale=1.5)
# 높이를 조정하여 Capacity를 초과하도록 스케일링 (최대값 약 100)
y_demand = y_demand_norm / y_demand_norm.max() * 105 

# 4. 기본 부하(Base Load): 건물 기본 전력 사용량 (선택 사항)
# 완만한 곡선으로 바닥에 깔아줌
y_base = 20 + 5 * np.sin((x - 6) / 14 * np.pi) 

# --- 그래프 그리기 ---
plt.figure(figsize=(10, 6))

# A. 수요 곡선 (Demand Curve)
plt.plot(x, y_demand + y_base, color='#D32F2F', linewidth=2.5, label='Total Power Demand (Uncoordinated)')
# (참고) 순수 충전 부하만 보여줄지, 건물 부하를 합칠지는 선택 가능. 여기선 합쳐서 표현.

# B. 공급 한계선 (Capacity Line)
plt.plot(x, y_capacity, color='#1976D2', linestyle='--', linewidth=2.5, label='Grid Capacity Limit (Inelastic)')

# C. 영역 채우기 (Highlight Overload)
# 수요가 공급을 초과하는 위험 구간 (Overload Risk)
y_total = y_demand + y_base
plt.fill_between(x, y_capacity, y_total, 
                 where=(y_total > y_capacity), 
                 interpolate=True, color='#FFCDD2', alpha=0.5, hatch='//', label='Overload Risk (Demand > Supply)')

# D. 스타일링 및 주석 (Annotation)
plt.title('Conceptual Diagram: Demand-Supply Mismatch in EV Charging', fontsize=14, pad=20, fontweight='bold')
plt.xlabel('Time of Day', fontsize=12)
plt.ylabel('Power Demand (kW)', fontsize=12)

# X축 틱 설정 (시간 표시)
plt.xticks(np.arange(6, 21, 2), [f'{h:02d}:00' for h in np.arange(6, 21, 2)])
# Y축 틱 제거 (개념도이므로 구체적 수치는 숨김)
plt.yticks([]) 

# 화살표 주석 추가
peak_x = 9.0
peak_y = y_total.max()
plt.annotate('Peak Demand\n(Morning Arrival)', xy=(peak_x, peak_y), xytext=(peak_x + 2, peak_y),
             arrowprops=dict(facecolor='black', shrink=0.05), fontsize=10)

plt.text(16, capacity_limit + 2, 'Grid Capacity Constraint', color='#1976D2', fontweight='bold', ha='center')

# 범례 및 그리드
plt.legend(loc='upper right', frameon=True)
plt.grid(True, linestyle=':', alpha=0.6)
plt.ylim(0, 140)

# 저장 및 출력
plt.tight_layout()
plt.savefig('fig1_conceptual_diagram.png', dpi=300)
plt.show()