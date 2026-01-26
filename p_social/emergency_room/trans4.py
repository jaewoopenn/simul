import pandas as pd
import pandas_access
import os
import glob
from datetime import timedelta
import warnings

warnings.filterwarnings('ignore')

os.environ['PATH'] += os.pathsep + '/opt/homebrew/bin' + os.pathsep + '/usr/local/bin'
DATA_FOLDER = '/users/jaewoo/data/israel/input'
OUTPUT_FILE = '/users/jaewoo/data/israel/simulation_input_final.csv'

# =========================================================
# [설정] 부서 코드 매핑
# =========================================================
DEPT_MAPPING = {
    1: "Emergency Internal Medicine Unit",
    2: "Emergency Surgery Unit",
    3: "Emergency Traumatology Unit",
    4: "Emergency Orthopedic Unit",
    5: "Emergency Otorhinolaryngology Unit",
    6: "Emergency Ophthalmology Unit",
    7: "Emergency Psychiatry Unit",
    8: "Emergency Gynecology Unit",
    9: "Pediatric Emergency Unit",
    10: "Pediatric Emergency Surgery Unit",
    11: "Emergency Maternity Unit",
    25: "Trauma Unit (road accidents)",
    26: "Trauma Unit (industrial accident)",
    101: "Emergency Medicine Unit1",
    129: "Trauma Unit (road accidents)"
}

# [설정] HI 키워드
HIGH_KEYWORDS = [
    'TRAUMA', 'SHOCK', 'RESUS', 'SURGERY', 'OPERATION',
    'CARDIAC', 'URGENT', 'CRITICAL'
]

def process_single_mdb(db_path):
    file_name = os.path.basename(db_path)
    print(f"\n>> [{file_name}] 분석 시작...")

    try:
        tables = pandas_access.list_tables(db_path)
        target_table = next((t for t in tables if 'visit' in t.lower() and 'detail' not in t.lower()), None)
        if not target_table: return None
        df = pandas_access.read_table(db_path, target_table, encoding='latin1')
    except: return None

    df.columns = [c.strip().lower() for c in df.columns]
    key_col = 'medical_id' if 'medical_id' in df.columns else 'patient_id'
    df[key_col] = df[key_col].astype(str).str.strip()

    # 1. 응급실 환자만 필터링
    if 'entry_group' in df.columns:
        df = df[df['entry_group'].astype(str) == '1'].copy()
    if df.empty: return None

    # 2. 부서 매핑
    dept_col = 'first_department' if 'first_department' in df.columns else 'department'
    if dept_col in df.columns:
        df['Dept_Name'] = df[dept_col].map(DEPT_MAPPING).fillna("Other ED")
    else:
        df['Dept_Name'] = "Unknown"

    # 3. 시간 및 실행시간 (ED_dur 사용)
    df['Arrival_Time'] = pd.to_datetime(df['entry_date'], errors='coerce')
    
    # [핵심 수정] ED_dur (초 단위) -> 분 단위 변환
    # ed_dur 컬럼이 있는지 확인
    if 'ed_dur' in df.columns:
        # 초 단위를 분 단위로
        df['Execution_Time'] = pd.to_numeric(df['ed_dur'], errors='coerce') / 60.0
    else:
        print("  [Warning] 'ed_dur' 컬럼이 없어 기존 방식(Exit-Arrival)을 사용합니다.")
        exit_temp = pd.to_datetime(df['exit_date'], errors='coerce')
        df['Execution_Time'] = (exit_temp - df['Arrival_Time']).dt.total_seconds() / 60.0

    # 필터링: 10분 이상 (순수 응급실 체류도 10분 미만은 접수 취소 등일 확률 높음)
    df = df.dropna(subset=['Execution_Time'])
    df = df[df['Execution_Time'] > 10]

    # [재계산] Exit_Time = Arrival + ED_dur (순수 응급실 퇴실 시간)
    # 기존 exit_date는 병원 전체 퇴원이므로 덮어씀
    def calc_exit(row):
        if pd.isna(row['Arrival_Time']): return pd.NaT
        return row['Arrival_Time'] + timedelta(minutes=row['Execution_Time'])
    
    df['Exit_Time'] = df.apply(calc_exit, axis=1)

    # 4. Criticality
    def get_criticality(dept_name):
        name_upper = str(dept_name).upper()
        if any(k in name_upper for k in HIGH_KEYWORDS):
            return 'HI'
        return 'LO'
    df['Criticality'] = df['Dept_Name'].apply(get_criticality)

    # 5. Deadline (Slack 적용)
    def get_bed_deadline(row):
        if pd.isna(row['Arrival_Time']): return pd.NaT
        exec_time = row['Execution_Time']
        
        # HI: Slack 60분
        if row['Criticality'] == 'HI': slack = 60 
        # LO: Slack 240분
        else: slack = 240 
            
        return row['Arrival_Time'] + timedelta(minutes=(exec_time + slack))

    df['Deadline_Time'] = df.apply(get_bed_deadline, axis=1)

    return df[[key_col, 'Arrival_Time', 'Execution_Time', 'Criticality', 'Deadline_Time', 'Exit_Time']]

def process_all_files():
    mdb_files = glob.glob(os.path.join(DATA_FOLDER, "*.mdb"))
    all_data = []
    for mdb in mdb_files:
        df = process_single_mdb(mdb)
        if df is not None and not df.empty: all_data.append(df)

    if not all_data: return

    final_df = pd.concat(all_data, ignore_index=True)
    final_df = final_df.sort_values(by='Arrival_Time')
    
    # 포맷팅
    for col in ['Arrival_Time', 'Deadline_Time', 'Exit_Time']:
        final_df[col] = final_df[col].dt.strftime('%Y-%m-%d %H:%M:%S')

    final_df.columns = ['Patient_ID', 'Arrival_Time', 'Execution_Time', 'Criticality', 'Deadline_Time', 'Exit_Time']
    
    final_df.to_csv(OUTPUT_FILE, index=False)
    
    print(f"\n[성공] {len(final_df)}건 저장 완료.")
    
    # 통계
    hi_count = len(final_df[final_df['Criticality']=='HI'])
    total = len(final_df)
    print(f"- HI 환자: {hi_count}명 ({hi_count/total*100:.1f}%)")
    print(f"- 평균 응급실 체류 시간(ED_dur): {final_df['Execution_Time'].mean():.1f}분")

if __name__ == "__main__":
    process_all_files()