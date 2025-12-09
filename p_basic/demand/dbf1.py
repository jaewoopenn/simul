import matplotlib.pyplot as plt
import numpy as np
import random

def calculate_dbf(t, tasks):
    """
    Demand Bound Function: DBF(t)
    Implicit Deadline Model 가정 (Period = Deadline)
    """
    demand = 0
    for d, c in tasks:
        # t 시간까지 해당 태스크가 몇 번 실행되어야 하는지(floor) * 실행 시간
        if d > 0:
            demand += (t // d) * c
    return demand

def generate_random_tasks(num_tasks=3):
    """
    랜덤한 태스크 (Deadline, Execution Time) 생성
    """
    tasks = []
    print(f"--- 생성된 랜덤 태스크 ({num_tasks}개) ---")
    for i in range(num_tasks):
        # Deadline은 5 ~ 20 사이 랜덤
        d = random.randint(5, 20)
        # Execution Time은 1 ~ D의 절반 사이 (과부하 방지 위해 적당히 설정)
        c = random.randint(1, max(1, d // 2))
        
        tasks.append((d, c))
        print(f"Task {i+1}: Deadline(Period) = {d}, Execution Time = {c}")
    print("---------------------------------------")
    return tasks

def main():
    # 1. 입력 대신 랜덤 하드코딩 데이터 생성
    # 실행할 때마다 다른 값으로 3개의 태스크가 생성됩니다.
    tasks = generate_random_tasks(3)

    # 2. 그래프 범위 설정 (최대 시간 50으로 고정하여 보기 편하게 함)
    max_time = 50
    
    # 시간 축 생성 (0, 1, 2, ... 50) - 정수 단위로 계산
    time_steps = np.arange(0, max_time + 1, 1)
    
    # 각 시간별 DBF 값 계산
    dbf_values = [calculate_dbf(t, tasks) for t in time_steps]

    # 3. 그래프 그리기
    plt.figure(figsize=(10, 6))

    # DBF는 이산적인 이벤트 발생 시점에 값이 점프하므로 step 그래프가 적절함
    # where='post': 시간 t가 되는 순간 수요가 발생함을 의미
    plt.step(time_steps, dbf_values, where='post', label='DBF(t)', color='blue', linewidth=2)
    
    # Supply Function (y=t) - 스케줄링 가능 여부 기준선
    plt.plot([0, max_time], [0, max_time], label='Supply (y=t)', color='red', linestyle='--')

    # 그래프 꾸미기
    plt.title("Demand Bound Function (Random Generated Tasks)")
    plt.xlabel("Time (t)")
    plt.ylabel("Cumulative Execution Time Demand")
    plt.grid(True, which='both', linestyle='--', linewidth=0.5)
    plt.legend()
    
    # x축, y축 눈금 설정 (정수 단위로 보기 편하게)
    plt.xticks(np.arange(0, max_time + 1, 5))
    
    # 시각적 확인을 위해 오버로드 구간(DBF > t)이 있으면 빨간색으로 칠하기
    plt.fill_between(time_steps, dbf_values, time_steps, 
                     where=(np.array(dbf_values) > time_steps), 
                     interpolate=True, color='red', alpha=0.3, label='Overload')

    plt.show()

if __name__ == "__main__":
    main()