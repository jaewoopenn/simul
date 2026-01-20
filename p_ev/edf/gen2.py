import os
import random
import math
import pickle
import argparse
from dataclasses import dataclass, field
from typing import List, Dict

# ---------------------------------------------------------
# 1. 설정 및 Config 관리 (매직 넘버 제거)
# ---------------------------------------------------------
@dataclass
class SimulationConfig:
    # 경로 설정
    base_path: str = "/Users/jaewoo/data/ev/cab/data"
    
    # 실험 규모
    trial_num: int = 200
    stress_start: int = 0
    stress_num: int = 10
    
    # 시나리오 설정
    max_time: int = 1440        # 24시간 (분 단위) 권장
    random_seed: int = 42       # 재현성을 위한 시드
    
    # EV 생성 파라미터 (계수)
    base_arrival_rate: float = 0.05   # 60분 기준보다 작게 조정 (1440분 기준)
    arrival_inc_factor: float = 0.01  # 혼잡도별 증가량
    
    base_energy: float = 20.0   # kWh 단위 가정 (더 현실적인 수치로 변경 가능)
    energy_inc_factor: float = 1.5
    
    # 충전 속도 다양성 (kW) - 차량별로 다를 수 있음
    power_options: List[float] = field(default_factory=lambda: [3.0, 7.0, 11.0, 50.0])

@dataclass
class EVRequest:
    ev_id: int
    arrival: int
    required_energy: float
    deadline: int
    remaining: float
    max_power: float  # [추가] 차량별 최대 충전 가능 속도

    def __repr__(self):
        return (f"EV{self.ev_id:03d} [Arr:{self.arrival:>4}, "
                f"Req:{self.remaining:>5.2f}, Dead:{self.deadline:>4}, "
                f"Pwr:{self.max_power:>4.1f}]")

# ---------------------------------------------------------
# 2. EV 데이터 생성기
# ---------------------------------------------------------
def generate_ev_set(config: SimulationConfig, congestion_level: int, trial_seed: int) -> List[EVRequest]:
    # Trial 별로 고유한 Seed 설정 (재현성 확보)
    # random.seed(trial_seed)
    
    # 혼잡도에 따른 파라미터 동적 계산
    arrival_rate = config.base_arrival_rate + (congestion_level * config.arrival_inc_factor)
    avg_energy = config.base_energy + (congestion_level * config.energy_inc_factor)
    std_energy = avg_energy * 0.3  # 평균의 30%를 표준편차로 설정
    
    # 혼잡할수록 여유시간(Slack)이 줄어드는 로직 유지
    slack_mean_factor = max(0.4, 1.9 - (congestion_level * 0.1))

    ev_requests = []
    current_time = 0.0
    ev_id = 0
    
    while True:
        # 도착 시간 (Exponential Inter-arrival)
        inter_arrival = random.expovariate(arrival_rate)
        current_time += inter_arrival
        
        if current_time > config.max_time:
            break
            
        arrival_int = int(current_time)
        
        # 요구 에너지 (Gaussian, 음수 방지)
        req_energy = random.gauss(avg_energy, std_energy)
        req_energy = max(1.0, req_energy) # 최소 1kWh
        req_energy = round(req_energy, 2)
        
        # [시나리오 확장] 차량별 충전 속도 랜덤 배정
        vehicle_max_power = random.choice(config.power_options)
        
        # 최소 필요 시간 계산
        min_required_time = math.ceil(req_energy / vehicle_max_power)
        
        # Slack 및 Deadline 계산
        raw_slack = random.expovariate(1.0) * (min_required_time * slack_mean_factor)
        slack = int(raw_slack)
        deadline = arrival_int + min_required_time + slack
        
        # Deadline이 전체 시뮬레이션 시간을 너무 초과하지 않도록 조정 (옵션)
        # deadline = min(deadline, config.max_time + 120) 
        
        ev_requests.append(EVRequest(
            ev_id=ev_id, 
            arrival=arrival_int, 
            required_energy=req_energy, 
            deadline=deadline, 
            remaining=req_energy,
            max_power=vehicle_max_power
        ))
        ev_id += 1
        
    return ev_requests

# ---------------------------------------------------------
# 3. 메인 실행
# ---------------------------------------------------------
if __name__ == '__main__':
    # 설정 로드
    conf = SimulationConfig()
    
    # 저장 경로 생성 (상대 경로 권장)
    save_dir = os.path.abspath(conf.base_path)
    if not os.path.exists(save_dir):
        os.makedirs(save_dir, exist_ok=True)
        print(f"[Info] Directory created: {save_dir}")
    
    print(f"Start generating datasets...")
    print(f"Config: Level {conf.stress_start}~{conf.stress_start + conf.stress_num - 1}, "
          f"{conf.trial_num} Trials/Level, MaxTime={conf.max_time}")

    for level in range(conf.stress_start, conf.stress_start + conf.stress_num):
        level_trials_data = []
        
        for trial in range(conf.trial_num):
            # Seed 설계: (Level * 10000) + Trial -> 겹칠 확률 0, 완벽한 재현 가능
            current_seed = conf.random_seed + (level * 10000) + trial
            ev_set = generate_ev_set(conf, level, current_seed)
            level_trials_data.append(ev_set)
            
        filename = f"ev_level_{level}.pkl"
        full_path = os.path.join(save_dir, filename)
        
        with open(full_path, 'wb') as f:
            pickle.dump(level_trials_data, f)
            
        # 통계 출력 (검증용)
        avg_count = sum(len(x) for x in level_trials_data) / len(level_trials_data)
        print(f"[Saved] Level {level}: {filename} (Avg EVs: {avg_count:.1f})")

    print(f"\n[Done] All data files are saved in {save_dir}")