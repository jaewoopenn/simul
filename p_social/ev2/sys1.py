import matplotlib.pyplot as plt
import matplotlib.patches as patches

# ---------------------------------------------------------
# 설정
# ---------------------------------------------------------
fig, ax = plt.subplots(figsize=(12, 8))
ax.set_xlim(0, 100)
ax.set_ylim(0, 100)
ax.axis('off') # 축 숨기기

# ---------------------------------------------------------
# 도형 그리기 함수 (Helper)
# ---------------------------------------------------------
def draw_box(x, y, w, h, text, color='#E3F2FD', edge='#1565C0', fontsize=12, fontweight='bold', subtext=None):
    # 박스 그림
    box = patches.FancyBboxPatch((x, y), w, h, boxstyle="round,pad=0.2", 
                                 linewidth=2, edgecolor=edge, facecolor=color)
    ax.add_patch(box)
    
    # 텍스트 중앙 정렬
    cx = x + w/2
    cy = y + h/2
    ax.text(cx, cy, text, ha='center', va='center', fontsize=fontsize, fontweight=fontweight, color='#0D47A1')
    
    if subtext:
        ax.text(cx, cy - h*0.3, subtext, ha='center', va='center', fontsize=9, color='#333333')

def draw_arrow(x1, y1, x2, y2, label=None, color='black'):
    ax.annotate('', xy=(x2, y2), xytext=(x1, y1),
                arrowprops=dict(arrowstyle='->', lw=2, color=color))
    if label:
        mx, my = (x1+x2)/2, (y1+y2)/2
        ax.text(mx + 1, my, label, fontsize=10, color=color, va='center',  bbox=dict(facecolor='white', edgecolor='none', alpha=0.8))

# ---------------------------------------------------------
# 1. Physical Layer (Bottom)
# ---------------------------------------------------------
# 배경 박스
ax.add_patch(patches.Rectangle((5, 5), 90, 20, fill=None, linestyle='--', edgecolor='gray'))
ax.text(10, 26, 'Physical Layer (Infrastructure)', fontsize=12, fontweight='bold', color='gray')

# EVSE Cluster
draw_box(10, 8, 25, 12, 'EV Charger\n(EVSE)', color='#E8F5E9', edge='#2E7D32')
draw_box(12, 10, 21, 8, '', color='white', edge='#2E7D32') # 내부 공백
ax.text(22.5, 14, 'EV #1 ... EV #N', ha='center', fontsize=10)

# Smart Meter
draw_box(65, 8, 20, 12, 'Smart Meter\n(BEMS)', color='#FFF3E0', edge='#EF6C00')

# ---------------------------------------------------------
# 2. Data/Communication Layer (Middle)
# ---------------------------------------------------------
# Gateway
draw_box(35, 35, 30, 10, 'IoT Gateway / Aggregator', color='#F3E5F5', edge='#7B1FA2', 
         subtext='Protocol Conversion (OCPP, Modbus)')

# ---------------------------------------------------------
# 3. Application Layer (SGO Cloud Engine) - Top
# ---------------------------------------------------------
# 메인 컨테이너
ax.add_patch(patches.FancyBboxPatch((15, 60), 70, 35, boxstyle="round,pad=0.4", 
                                    linewidth=3, edgecolor='#0D47A1', facecolor='#F5F5F5'))
ax.text(50, 97, 'Sustainable Grid Orchestration (SGO) Engine', ha='center', fontsize=14, fontweight='bold')

# 내부 모듈 1: State Estimation
draw_box(20, 75, 20, 12, 'State Estimation\nModule', color='white', edge='#1976D2')
ax.text(30, 72, '- SoH, SoC Check\n- Laxity Calculation', ha='center', fontsize=9)

# 내부 모듈 2: Grid Monitor
draw_box(60, 75, 20, 12, 'Grid Monitoring\nModule', color='white', edge='#1976D2')
ax.text(70, 72, '- Total Load Check\n- Congestion Index', ha='center', fontsize=9)

# 내부 모듈 3: Adaptive Scheduler (Core)
draw_box(35, 62, 30, 10, 'Adaptive Hybrid Scheduler', color='#E3F2FD', edge='#0D47A1')
ax.text(50, 60, '(Min Guarantee + Urgency Allocation)', ha='center', fontsize=8)

# ---------------------------------------------------------
# 4. 데이터 흐름 (Arrows)
# ---------------------------------------------------------

# Physical -> Gateway (Up)
draw_arrow(22.5, 20, 40, 35, '', color='#2E7D32') # EV -> GW
draw_arrow(75, 20, 60, 35, '', color='#EF6C00')   # Meter -> GW

# Gateway -> Cloud (Up: Data)
draw_arrow(45, 45, 30, 75, 'Real-time Data\n(Arrival, Power, Limit)', color='black') # To State
draw_arrow(55, 45, 70, 75, '', color='black') # To Grid

# Cloud Internal Flows
draw_arrow(40, 78, 50, 72, '', color='#1976D2') # State -> Scheduler
draw_arrow(60, 78, 50, 72, '', color='#1976D2') # Monitor -> Scheduler

# Cloud -> Gateway (Down: Control)
# Scheduler -> GW (직접 연결 대신 곡선으로 표현하기 어려우므로 직선 후 주석)
ax.annotate('', xy=(50, 45), xytext=(50, 62),
            arrowprops=dict(arrowstyle='->', lw=2, color='#D32F2F', linestyle='--'))
ax.text(51, 53, 'Control Command\n(Charging Setpoints)', fontsize=10, color='#D32F2F', ha='left')

# Gateway -> EVSE (Down)
ax.annotate('', xy=(22.5, 22), xytext=(45, 35),
            arrowprops=dict(arrowstyle='->', lw=2, color='#D32F2F', linestyle='--'))

# ---------------------------------------------------------
# 5. 저장 및 출력
# ---------------------------------------------------------
plt.tight_layout()
plt.savefig('fig3_sgo_architecture.png', dpi=300, bbox_inches='tight')
plt.show()