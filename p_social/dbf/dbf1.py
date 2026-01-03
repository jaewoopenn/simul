def task_set_dbf(t, tasks):
    """
    t: 시간 구간 (Time Interval)
    tasks: 태스크 리스트 [{'e': 실행시간, 'd': 데드라인}, ...]
    """
    total_demand = 0
    
    for task in tasks:
        e = task['e']
        d = task['d']
        
        # 앞서 정의한 로직을 min/max로 간결하게 표현
        # 1. 0과 (t - (d-e)) 중 큰 값을 취함 (음수 제거)
        ramp = max(0, t - (d - e))
        # 2. 위 값과 e 중 작은 값을 취함 (e를 넘지 않도록)
        demand = min(e, ramp)
        
        total_demand += demand
        
    return total_demand

# 사용 예시
# 태스크 1: 실행 2, 데드라인 5 (여유 3)
# 태스크 2: 실행 3, 데드라인 10 (여유 7)
my_tasks = [
    {'e': 2, 'd': 5}, 
    {'e': 3, 'd': 10}
]

# t=4일 때: 태스크 1은 여유(3)을 지났으므로 4-3=1만큼 수요 발생. 태스크 2는 아직 0. 총합 1.
print(f"t=4일 때 DBF: {task_set_dbf(4, my_tasks)}") 

# t=6일 때: 태스크 1은 데드라인(5) 지남 -> 2(max). 태스크 2는 아직 0. 총합 2.
print(f"t=6일 때 DBF: {task_set_dbf(6, my_tasks)}")