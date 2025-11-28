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

# 부서 매핑 (PDF 기반)
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

# [수정됨] HI(중증) 키워드 리스트 - 'INTERNAL' 제거하여 현실성 확보
# 이 키워드가 포함된 부서만 HI로 분류됩니다. (전체의 약 15~20% 예상)
HIGH_KEYWORDS = [
    'TRAUMA',      # 외상 (교통사고 등)
    'SHOCK',       # 쇼크
    'RESUS',       # 소생실
    'SURGERY',     # 응급 수술 (Emergency Surgery)
    'OPERATION',   # 수술
    'CARDIAC',     # 심장
    'URGENT',      # 긴급
    'CRITICAL'     # 위독
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

    # 1. 응급실 환자만 필터링 (entry_group == 1)
    if 'entry_group' in df.columns:
        df = df[df['entry_group'].astype(str) == '1'].copy()
    
    if df.empty: return None

    # 2. 부서명 매핑
    dept_col = 'first_department' if 'first_department' in df.columns else 'department'
    if dept_col in df.columns:
        df['Dept_Name'] = df[dept_col].map(DEPT_MAPPING).fillna("Other ED")
    else:
        df['Dept_Name'] = "Unknown"

    # 3. 시간 및 실행시간 계산
    df['Arrival_Time'] = pd.to_datetime(df['entry_date'], errors='coerce')
    df['Exit_Time'] = pd.to_datetime(df['exit_date'], errors='coerce')
    df['Execution_Time'] = (df['Exit_Time'] - df['Arrival_Time']).dt.total_seconds() / 60
    
    # 필터링: 10분 ~ 24시간
    df = df.dropna(subset=['Execution_Time'])
    df = df[(df['Execution_Time'] > 10) & (df['Execution_Time'] < 1440)]

    # 4. Criticality 판별 (수정된 리스트 적용)
    def get_criticality(dept_name):
        name_upper = str(dept_name).upper()
        if any(k in name_upper for k in HIGH_KEYWORDS):
            return 'HI'
        return 'LO'

    df['Criticality'] = df['Dept_Name'].apply(get_criticality)

    # 5. 데드라인 설정 (HI: 90분, LO: 180분)
    # 근거: ED 평균 체류시간 3시간(180분)
    def get_deadline(row):
        if pd.isna(row['Arrival_Time']): return pd.NaT
        if row['Criticality'] == 'HI':
            return row['Arrival_Time'] + timedelta(minutes=90)
        else:
            return row['Arrival_Time'] + timedelta(minutes=180)

    df['Deadline_Time'] = df.apply(get_deadline, axis=1)

    return df[[key_col, 'Arrival_Time', 'Execution_Time', 'Criticality', 'Deadline_Time']]

def process_all_files():
    mdb_files = glob.glob(os.path.join(DATA_FOLDER, "*.mdb"))
    all_data = []
    for mdb in mdb_files:
        df = process_single_mdb(mdb)
        if df is not None and not df.empty: all_data.append(df)

    if not all_data: return

    final_df = pd.concat(all_data, ignore_index=True)
    final_df = final_df.sort_values(by='Arrival_Time')
    
    final_df['Arrival_Time'] = final_df['Arrival_Time'].dt.strftime('%Y-%m-%d %H:%M:%S')
    final_df['Deadline_Time'] = final_df['Deadline_Time'].dt.strftime('%Y-%m-%d %H:%M:%S')
    final_df.columns = ['Patient_ID', 'Arrival_Time', 'Execution_Time', 'Criticality', 'Deadline_Time']

    final_df.to_csv(OUTPUT_FILE, index=False)
    
    print(f"\n[성공] {len(final_df)}건 저장 완료.")
    
    # 비율 확인 (이게 중요합니다)
    hi_count = len(final_df[final_df['Criticality']=='HI'])
    lo_count = len(final_df[final_df['Criticality']=='LO'])
    total = hi_count + lo_count
    
    print(f"- HI 환자: {hi_count}명 ({hi_count/total*100:.1f}%)")
    print(f"- LO 환자: {lo_count}명 ({lo_count/total*100:.1f}%)")
    print("-> HI 비율이 15~25% 사이라면 선진국형 시뮬레이션으로 아주 적절합니다.")

if __name__ == "__main__":
    process_all_files()