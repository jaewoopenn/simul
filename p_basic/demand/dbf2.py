import matplotlib.pyplot as plt
import numpy as np
import random

class Job:
    def __init__(self, id, arrival, cost, deadline):
        self.id = id
        self.arrival = arrival       # a_i: 도착 시간
        self.cost = cost             # c_i: 실행 시간 (Execution Time)
        self.deadline = deadline     # d_i: 절대 데드라인 (Absolute Deadline)

    def __repr__(self):
        return f"J{self.id}(A={self.arrival}, C={self.cost}, D={self.deadline})"

def generate_random_jobs(num_jobs=10):
    """
    랜덤한 10개의 Job 생성
    Task가 아니므로 주기(Period) 개념 없이 개별 인스턴스만 생성합니다.
    """
    jobs = []
    print("--- Generated Jobs (Arrival, Cost, Absolute Deadline) ---")
    for i in range(num_jobs):
        # Arrival: 0 ~ 40 사이 랜덤
        a = random.randint(0, 40)
        # Cost: 2 ~ 8 사이 랜덤
        c = random.randint(2, 8)
        # Deadline: 도착 + 실행시간 + 여유시간(Slack)
        # (최소한 실행시간만큼은 확보되어야 하므로 a + c 이상이어야 함)
        d = a + c + random.randint(0, 20)
        
        jobs.append(Job(i+1, a, c, d))
        print(f"Job {i+1}: Arrival={a}, Cost={c}, Abs_Deadline={d}")
    print("-------------------------------------------------------")
    return jobs

def calculate_dbf(t, jobs):
    """
    시간 t까지의 절대적인 Demand Bound를 계산
    조건: 절대 데드라인(d_i)이 t보다 작거나 같은 Job들의 Cost 합
    """
    demand = 0
    for job in jobs:
        # DBF 정의: 구간 [0, t] 내에 데드라인이 있는 작업의 총량
        if job.deadline <= t:
            demand += job.cost
    return demand

def main():
    # 1. 랜덤 Job 10개 생성
    jobs = generate_random_jobs(10)

    # 2. 그래프 시간 축 설정
    # 가장 늦은 데드라인을 찾아서 그보다 조금 더 뒤까지 그림
    max_deadline = max(job.deadline for job in jobs)
    time_limit = max_deadline + 5
    
    time_steps = np.arange(0, time_limit + 1, 1)
    dbf_values = [calculate_dbf(t, jobs) for t in time_steps]

    # 3. 그래프 그리기 (2개의 서브플롯)
    fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(10, 10), sharex=True)

    # [상단 그래프] Job 시각화 (Gantt Chart 스타일)
    # 각 Job의 (도착 ~ 데드라인) 구간을 보여줌
    ax1.set_title("Job Visualization (Arrival to Deadline window)")
    ax1.set_ylabel("Job ID")
    ax1.grid(True, linestyle='--', alpha=0.5)
    
    # Y축 범위를 Job 개수에 맞춤
    ax1.set_ylim(0, len(jobs) + 1)
    
    for job in jobs:
        # 도착점(동그라미)
        ax1.plot(job.arrival, job.id, 'go', markersize=5) 
        # 데드라인(X표시)
        ax1.plot(job.deadline, job.id, 'rx', markersize=8, markeredgewidth=2)
        # 도착~데드라인 선 연결
        ax1.hlines(job.id, job.arrival, job.deadline, colors='gray', linestyles='dotted')
        # 실행 시간(Cost)만큼의 막대 (도착 직후 실행된다고 가정하고 길이만 표시)
        ax1.barh(job.id, job.cost, left=job.arrival, height=0.4, color='skyblue', alpha=0.8, align='center')

    # 범례 생성용 가짜 플롯
    from matplotlib.lines import Line2D
    custom_lines = [Line2D([0], [0], color='green', marker='o', lw=0),
                    Line2D([0], [0], color='red', marker='x', lw=0),
                    Line2D([0], [0], color='skyblue', lw=4)]
    ax1.legend(custom_lines, ['Arrival', 'Deadline', 'Execution Cost'])


    # [하단 그래프] DBF(t)
    ax2.set_title("Demand Bound Function (DBF)")
    ax2.set_xlabel("Time (t)")
    ax2.set_ylabel("Demand")
    ax2.grid(True, which='both', linestyle='--')
    
    # DBF 그리기 (step function)
    ax2.step(time_steps, dbf_values, where='post', color='blue', linewidth=2, label='DBF(t)')
    
    # Supply Bound (y=t) - 비교용
    ax2.plot([0, time_limit], [0, time_limit], color='red', linestyle='--', label='Supply (y=t)')

    # 오버로드 구간 표시
    dbf_arr = np.array(dbf_values)
    ax2.fill_between(time_steps, dbf_arr, time_steps, 
                     where=(dbf_arr > time_steps), 
                     color='red', alpha=0.3, interpolate=True, label='Overload')

    ax2.legend()
    
    plt.tight_layout()
    plt.show()

if __name__ == "__main__":
    main()