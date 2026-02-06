import pandas as pd
import pytz
import os
from datetime import datetime
from acnportal import acndata
API_TOKEN = 'afNdKWEwv69GZYHiZAOYO9K2oDAwrMn16IKPGXfqAf0'
SAVE_DIR = '/users/jaewoo/data/acn'

def fetch_and_save_acn_data():
    # ---------------------------------------------------------
    # 1. 설정 (Configuration)
    # ---------------------------------------------------------
    # 본인의 API 토큰이 있다면 여기에 입력하세요. (없으면 'DEMO_TOKEN' 사용)
    SITE = 'caltech'  # 'caltech' or 'jpl' or 'office001'
    
    # 저장할 파일 경로 설정
    if not os.path.exists(SAVE_DIR):
        os.makedirs(SAVE_DIR)
        print(f"📂 '{SAVE_DIR}' 폴더를 생성했습니다.")

    # ---------------------------------------------------------
    # 2. 날짜 및 시간대 설정 (Timezone: LA 기준)
    # ---------------------------------------------------------
    timezone = pytz.timezone('America/Los_Angeles')
    
    # 예: 2019년 9월 1일 ~ 9월 7일 (일주일치 데이터)
    start_time = timezone.localize(datetime(2019, 10, 1))
    end_time = timezone.localize(datetime(2019, 10, 31))

    print(f"🚀 데이터를 요청합니다... ({start_time.date()} ~ {end_time.date()})")

    # ---------------------------------------------------------
    # 3. 데이터 가져오기 (API Call)
    # ---------------------------------------------------------
    client = acndata.DataClient(API_TOKEN)
    
    try:
        # Generator를 반환받음
        docs = client.get_sessions_by_time(SITE, start_time, end_time)
        
        # Generator를 리스트로 변환 (이때 실제 다운로드가 일어남)
        data_list = list(docs)
        
        if not data_list:
            print("⚠️ 가져온 데이터가 없습니다. 날짜 범위나 토큰을 확인하세요.")
            return

        # ---------------------------------------------------------
        # 4. 데이터프레임 변환 및 전처리
        # ---------------------------------------------------------
        df = pd.DataFrame(data_list)

        # 분석에 필요한 주요 컬럼만 선택 (필요시 userInputs 등 추가 가능)
        selected_columns = [
            'sessionID', 'stationID', 'connectionTime', 'disconnectTime', 
            'kWhDelivered', 'doneChargingTime', 'spaceID'
        ]
        
        # 데이터에 해당 컬럼이 존재하는지 확인 후 선택
        existing_cols = [col for col in selected_columns if col in df.columns]
        df_clean = df[existing_cols].copy()

        # 날짜 포맷 정리 (timezone 정보가 있으면 엑셀에서 보기 힘들 수 있으므로 문자열로 변환)
        # 연구용으로 쓸 때는 datetime 객체 그대로 두는 게 좋지만, 파일 저장용으로는 문자열 추천
        df_clean['connectionTime'] = df_clean['connectionTime'].astype(str)
        df_clean['disconnectTime'] = df_clean['disconnectTime'].astype(str)
        if 'doneChargingTime' in df_clean.columns:
            df_clean['doneChargingTime'] = df_clean['doneChargingTime'].astype(str)

        # ---------------------------------------------------------
        # 5. 콘솔 출력 및 파일 저장 (Save to CSV)
        # ---------------------------------------------------------
        # (1) 콘솔 출력
        print(f"\n✅ 총 {len(df_clean)}개의 충전 세션을 성공적으로 가져왔습니다.")
        print("-" * 50)
        print(df_clean.head())
        print("-" * 50)

        # (2) 파일 저장
        filename = f"acn_data_{SITE}_{start_time.strftime('%Y%m%d')}_{end_time.strftime('%Y%m%d')}.csv"
        filename = f"acn_data_{SITE}_{start_time.strftime('%Y%m%d')}_{end_time.strftime('%Y%m%d')}.csv"
        file_path = os.path.join(SAVE_DIR, filename)
        
        df_clean.to_csv(file_path, index=False, encoding='utf-8-sig')
        print(f"💾 파일이 저장되었습니다: {file_path}")

    except Exception as e:
        print(f"❌ 에러 발생: {str(e)}")
        print("Tip: 인터넷 연결 상태나 API 토큰 유효성을 확인해주세요.")

# 함수 실행
if __name__ == "__main__":
    fetch_and_save_acn_data()