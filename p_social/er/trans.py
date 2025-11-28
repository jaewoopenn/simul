import pandas as pd
import pandas_access
import os
import shutil
from datetime import timedelta

# =========================================================
# [설정] 경로 및 환경 변수 (Mac MDBTools 경로)
# =========================================================
os.environ['PATH'] += os.pathsep + '/opt/homebrew/bin' + os.pathsep + '/usr/local/bin'

# 사용자 데이터 경로 (확인 필수!)
DATA_FOLDER = '/users/jaewoo/data/israel' 
MDB_FILE = 'April2004.mdb'
DB_PATH = os.path.join(DATA_FOLDER, MDB_FILE)
OUTPUT_FILE = 'simulation_input.csv'

# =========================================================
# [함수] 날짜와 시간 컬럼 합치기
# =========================================================
def combine_date_time(df, date_col, time_col):
    """
    Date 컬럼과 Time 컬럼을 합쳐서 하나의 Datetime 객체로 만듭니다.
    만약 Date 컬럼 자체가 이미 datetime을 포함하고 있다면 그것을 우선합니다.
    """
    # 1. 날짜 변환
    # 이스라엘 데이터이므로 일(Day)이 먼저 나옴을 명시 (DD/MM/YYYY)
    d = pd.to_datetime(df[date_col], errors='coerce', dayfirst=True)
    
    # 2. 시간 변환 (문자열일 경우 처리)
    t = pd.to_datetime(df[time_col], format='%H:%M:%S', errors='coerce', dayfirst=True).dt.time
    
    # 시간이 NaT(변환실패)면 00:00:00으로 가정하거나 제외
    # 여기서는 날짜만 있는 경우 시간을 00:00:00으로 처리
    
    # Combine (날짜 + 시간)
    # 벡터화 연산을 위해 apply 사용 (데이터가 클 경우 속도 개선 필요)
    def merge_dt(row):
        if pd.isna(row['d']): return pd.NaT
        if pd.isna(row['t']): return row['d'] # 시간 없으면 날짜만
        return pd.Timestamp.combine(row['d'].date(), row['t'])

    temp_df = pd.DataFrame({'d': d, 't': t})
    return temp_df.apply(merge_dt, axis=1)

def process_mdb_data():
    if not os.path.exists(DB_PATH):
        print(f"오류: 파일을 찾을 수 없습니다 -> {DB_PATH}")
        return

    print(f"[{MDB_FILE}] 처리 시작...")

    # ---------------------------------------------------------
    # 1. 테이블 로드 (제공해주신 컬럼 정보 기반 매핑)
    # ---------------------------------------------------------
    # 실제 테이블 이름 (로그에서 확인된 이름 사용)
    tbl_visits = 'visits'
    tbl_proc = 'ward_first_procedure'
    tbl_phys = 'physical_details'

    try:
        print("테이블 로딩 중...")
        # Access에서 데이터를 읽어옵니다.
        df_visit = pandas_access.read_table(DB_PATH, tbl_visits, encoding='latin1')
        df_proc = pandas_access.read_table(DB_PATH, tbl_proc, encoding='latin1')
        df_phys = pandas_access.read_table(DB_PATH, tbl_phys, encoding='latin1')
    except Exception as e:
        print(f"테이블 로드 중 오류 발생.\n{e}")
        return

    # 컬럼 공백 제거 (안전장치)
    for df in [df_visit, df_proc, df_phys]:
        df.columns = [c.strip() for c in df.columns] # 공백만 제거 (소문자 변환 X)

    print("테이블 로드 완료. 데이터 병합 시작...")

    # ---------------------------------------------------------
    # 2. 데이터 병합 (Key: medical_id)
    # ---------------------------------------------------------
    
    # A. Visits + Procedure (처치 시간 있는 경우만 Inner Join)
    # visits 테이블의 'medical_id'와 procedure 테이블의 'medical_id' 연결
    try:
        merged_df = pd.merge(df_visit, df_proc, on='medical_id', how='inner', suffixes=('_vis', '_proc'))
        
        # B. + Physical Details (병동 정보는 없어도 되므로 Left Join)
        merged_df = pd.merge(merged_df, df_phys, on='medical_id', how='left', suffixes=('', '_phys'))
    except KeyError as e:
        print(f"병합 실패! 'medical_id' 컬럼이 있는지 확인해주세요.\nError: {e}")
        # 디버깅용: 컬럼 출력
        print(f"Visits cols: {df_visit.columns}")
        return

    # ---------------------------------------------------------
    # 3. 시뮬레이션 파라미터 생성
    # ---------------------------------------------------------
    print("파라미터 계산 중...")

    # A. Arrival Time (도착 시간)
    # visits 테이블의 'entry_date'와 'entry_time' 사용
    merged_df['Arrival_Time'] = combine_date_time(merged_df, 'entry_date', 'entry_time')

    # B. Service Start Time (처치 시작 시간)
    # procedure 테이블의 'first_procedure_date', 'first_procedure_time' 사용
    merged_df['Service_Time'] = combine_date_time(merged_df, 'first_procedure_date', 'first_procedure_time')

    # C. Exit Time (퇴원 시간)
    # visits 테이블의 'exit_date', 'exit_time' 사용
    merged_df['Exit_Time'] = combine_date_time(merged_df, 'exit_date', 'exit_time')

    # D. Execution Time (처치 소요 시간 Ci)
    # 로직: (퇴원 시간 - 처치 시작 시간) -> 분 단위
    merged_df['Execution_Time'] = (merged_df['Exit_Time'] - merged_df['Service_Time']).dt.total_seconds() / 60
    
    # 이상치 필터링 (5분 미만, 24시간 초과 제거)
    merged_df = merged_df.dropna(subset=['Execution_Time'])
    merged_df = merged_df[(merged_df['Execution_Time'] > 5) & (merged_df['Execution_Time'] < 1440)]

    # E. Criticality (중증도)
    # physical_department 컬럼 활용
    # 컬럼 이름 충돌 방지를 위해 병합 시 suffix 확인 필요. 
    # physical_details 테이블의 컬럼명이 'physical_department'라고 명시됨.
    
    high_keywords = ['ICU', 'SHOCK', 'TRAUMA', 'URGENT', 'RESUS', 'CARDIAC', 'INTERNAL']
    
    def get_criticality(dept):
        if pd.isna(dept): return 'LO'
        dept_str = str(dept).upper()
        if any(k in dept_str for k in high_keywords):
            return 'HI'
        return 'LO'

    # physical_details 테이블에서 온 'physical_department' 사용
    # 병합 과정에서 이름이 변경되었을 수 있으니 확인 (보통 그대로 유지됨)
    target_col = 'physical_department'
    if target_col not in merged_df.columns:
        # 혹시 이름이 겹쳐서 _phys가 붙었는지 확인
        target_col = 'physical_department_phys'
    
    if target_col in merged_df.columns:
        merged_df['Criticality'] = merged_df[target_col].apply(get_criticality)
    else:
        print("주의: physical_department 컬럼을 찾을 수 없어 모두 'LO'로 설정합니다.")
        merged_df['Criticality'] = 'LO'

    # F. Deadline (골든타임)
    def get_deadline(row):
        if pd.isna(row['Arrival_Time']): return pd.NaT
        if row['Criticality'] == 'HI':
            return row['Arrival_Time'] + timedelta(minutes=60)
        else:
            return row['Arrival_Time'] + timedelta(minutes=240)

    merged_df['Deadline_Time'] = merged_df.apply(get_deadline, axis=1)

    # ---------------------------------------------------------
    # 4. 최종 결과 저장
    # ---------------------------------------------------------
    # medical_id를 Patient_ID로 사용
    final_df = merged_df[[
        'medical_id', 'Arrival_Time', 'Execution_Time', 'Criticality', 'Deadline_Time'
    ]].copy()
    
    final_df.columns = ['Patient_ID', 'Arrival_Time', 'Execution_Time', 'Criticality', 'Deadline_Time']
    
    # 도착 시간 순 정렬
    final_df = final_df.sort_values(by='Arrival_Time')
    
    # 날짜 포맷 깔끔하게 정리 (초 단위까지만)
    final_df['Arrival_Time'] = final_df['Arrival_Time'].dt.strftime('%Y-%m-%d %H:%M:%S')
    final_df['Deadline_Time'] = final_df['Deadline_Time'].dt.strftime('%Y-%m-%d %H:%M:%S')

    final_df.to_csv(OUTPUT_FILE, index=False)
    print(f"\n완료! 총 {len(final_df)}건의 데이터가 생성되었습니다.")
    print(f"저장 위치: {os.path.abspath(OUTPUT_FILE)}")
    print(final_df.head())

if __name__ == "__main__":
    process_mdb_data()