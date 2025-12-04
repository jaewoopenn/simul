import os
from graphviz import Digraph

# 아래 경로는 위에서 'which dot'으로 확인한 경로의 폴더명으로 바꿔주세요.
# 예: /opt/homebrew/bin/dot 이 나왔다면 -> /opt/homebrew/bin
os.environ["PATH"] += os.pathsep + '/opt/homebrew/bin'

def create_sgo_architecture():
    # Create Digraph
    dot = Digraph('SGO_Architecture', comment='Sustainable Grid Orchestration System Architecture')
    dot.attr(rankdir='LR', size='12,8', dpi='300', compound='true', fontname='Helvetica')
    
    # Global Node Styles
    dot.attr('node', shape='box', style='filled, rounded', fontname='Helvetica', fontsize='12')
    dot.attr('edge', fontname='Helvetica', fontsize='10')

    # 1. External Environment Cluster (Input)
    with dot.subgraph(name='cluster_0') as c:
        c.attr(label='External Environment (Dynamic Constraints)', style='dashed', color='gray40', fontcolor='gray40')
        c.node('Grid', 'Grid Operator / EMS\n(Renewable Supply & TOU Price)', fillcolor='#E3F2FD', color='#1565C0')
        c.node('User', 'EV Users\n(Arrival, SoC, Deadline)', fillcolor='#FFF3E0', color='#EF6C00')
        
    # 2. SGO Controller Cluster (The Core Framework)
    with dot.subgraph(name='cluster_1') as c:
        c.attr(label='Sustainable Grid Orchestration (SGO) Controller', style='solid', color='#2E7D32', penwidth='2', fontcolor='#2E7D32', fontsize='14')
        c.attr(bgcolor='#F1F8E9')
        
        # State Estimation
        c.node('State', 'State Estimation\n(Monitor Current Loads)', shape='ellipse', fillcolor='white', color='#558B2F')

        # Layer 1: Admission Control
        c.node('Gatekeeper', '<<Layer 1>>\nSustainability Gatekeeper\n(Capacity-Aware Admission Control)', 
               shape='component', fillcolor='#C8E6C9', color='#2E7D32', penwidth='2')
        
        # Layer 2: Scheduler
        c.node('Scheduler', '<<Layer 2>>\nResource-Efficient Scheduler\n(Least Laxity First Policy)', 
               shape='component', fillcolor='#DCEDC8', color='#558B2F')

        # Logic Flow inside Controller
        c.edge('State', 'Gatekeeper', style='dotted')

    # 3. Physical Infrastructure Cluster
    with dot.subgraph(name='cluster_2') as c:
        c.attr(label='Physical Infrastructure', style='dashed', color='gray40', fontcolor='gray40')
        c.node('EVSE', 'Smart EVSEs\n(Power Delivery)', fillcolor='#EEEEEE', color='black')
        c.node('Battery', 'EV Batteries\n(State of Charge)', shape='cylinder', fillcolor='#E0E0E0')

    # --- Edges (Data & Control Flow) ---

    # Input to Controller
    dot.edge('Grid', 'Gatekeeper', label=' Dynamic Capacity Limit P_limit(t)\n(Based on TOU/Renewables)', color='#1565C0', penwidth='1.5')
    dot.edge('User', 'Gatekeeper', label=' Charging Request\n(Energy Demand)', color='#EF6C00', penwidth='1.5')

    # Inside Controller Decisions
    dot.edge('Gatekeeper', 'Scheduler', label=' Admitted Set\n(Feasible Requests)', color='#2E7D32', penwidth='2')
    dot.edge('Gatekeeper', 'User', label=' Rejection Signal\n(Infeasible)', color='#D32F2F', style='dotted', constraint='false')

    # Output to Physical
    dot.edge('Scheduler', 'EVSE', label=' Optimal Charging Profile\n(Setpoint)', color='#2E7D32', penwidth='1.5')
    
    # Physical Interaction
    dot.edge('EVSE', 'Battery', label=' Energy Transfer', color='black')
    
    # Feedback Loop
    dot.edge('Battery', 'State', label=' Real-time SOC\nFeedback', style='dashed', color='gray50')
    dot.edge('EVSE', 'State', style='dashed', color='gray50')

    # Render
    output_path = '/users/jaewoo/data/acn/SGO_System_Architecture'
    dot.render(output_path, format='png', cleanup=False)
    print(f"Diagram generated at: {output_path}.png")

if __name__ == "__main__":
    create_sgo_architecture()