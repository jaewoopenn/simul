'''
Created on 2025. 11. 28.

@author: jaewoo
'''
import pandas as pd
import pytz
from datetime import datetime
from acnportal import acndata

API_TOKEN = 'afNdKWEwv69GZYHiZAOYO9K2oDAwrMn16IKPGXfqAf0'


# ---------------------------------------------------------
# 1. API 토큰 및 클라이언트 설정
# ---------------------------------------------------------
# 주의: 'DEMO_TOKEN'은 작동하지 않을 수 있습니다. 
# https://ev.caltech.edu/dataset 에서 가입 후 발급받은 본인의 토큰을 넣는 것을 권장합니다.

client = acndata.DataClient(API_TOKEN)

# ---------------------------------------------------------
# 2. 날짜 및 시간대 설정 (가장 중요한 부분!)
# ---------------------------------------------------------
# ACN 데이터는 캘리포니아 기준이므로 'America/Los_Angeles' 타임존을 설정해야 정확합니다.
timezone = pytz.timezone('America/Los_Angeles')

# 문자열이 아닌 datetime 객체로 생성하고 timezone을 입혀야(localize) 합니다.
start_time = timezone.localize(datetime(2019, 9, 1))
end_time = timezone.localize(datetime(2019, 9, 2)) # 하루치 데이터만 테스트

print(f"데이터 요청 중... ({start_time} ~ {end_time})")

# ---------------------------------------------------------
# 3. 데이터 가져오기 (Generator 처리)
# ---------------------------------------------------------
try:
    # get_sessions_by_time은 리스트가 아닌 'Generator'를 반환합니다.
    # site는 'caltech' 또는 'jpl', 'office001' 등을 사용할 수 있습니다.
    session_generator = client.get_sessions_by_time('caltech', start_time, end_time)

    # Generator를 리스트로 변환하여 메모리에 로드
    data = list(session_generator)
    
    if not data:
        print("반환된 데이터가 없습니다. 날짜 범위나 토큰을 확인해주세요.")
    else:
        # ---------------------------------------------------------
        # 4. 데이터프레임 변환 및 확인
        # ---------------------------------------------------------
        df = pd.DataFrame(data)
        
        # 주요 컬럼만 선택해서 보여주기
        cols = ['connectionTime', 'disconnectTime', 'kWhDelivered', 'sessionID', 'stationID']
        # 실제 데이터에 해당 컬럼이 있는지 확인 후 출력
        available_cols = [c for c in cols if c in df.columns]
        
        print(f"총 {len(df)}개의 충전 세션을 가져왔습니다.")
        print(df[available_cols].head())

        # (선택) CSV로 저장
        # df.to_csv("acn_data_sample.csv", index=False)

except Exception as e:
    print(f"에러 발생: {e}")
    print("팁: API 토큰 문제일 수 있습니다. 웹사이트에서 직접 다운로드 받는 것이 더 빠를 수 있습니다.")

