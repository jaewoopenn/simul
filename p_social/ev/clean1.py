'''
Created on 2025. 11. 29.

@author: jaewoo
'''
import pandas as pd
import numpy as np
SAVE_DIR = '/users/jaewoo/data/acn'

# [수정] 새 데이터 파일명
CSV_FILENAME = SAVE_DIR+'/202410DatasetEVOfficeParking_v0.csv'

# 분석 기간 (2024년 9월)
TARGET_START_DATE = '2024-09-01'
TARGET_END_DATE = '2024-09-30'

def load_and_clean_data():
    print(">>> 데이터 로드 및 전처리 시작...")
    
    try:
        # 1. 데이터 로드
        df = pd.read_csv(CSV_FILENAME, sep=';')
        
        # 2. 컬럼 매핑
        df.rename(columns={
            'start_datetime': 'connectionTime',
            'end_datetime': 'disconnectTime',
            'total_energy': 'kWhDelivered',
            'evse_uid': 'stationID'
        }, inplace=True)
        
        # 3. 날짜 변환
        for col in ['connectionTime', 'disconnectTime']:
            df[col] = pd.to_datetime(df[col])
            
        # 4. 숫자형 변환 (kWh)
        if df['kWhDelivered'].dtype == object:
             df['kWhDelivered'] = df['kWhDelivered'].astype(str).str.replace(',', '.').astype(float)

        # 5. 기간 필터링
        mask = (df['connectionTime'] >= TARGET_START_DATE) & (df['connectionTime'] <= f"{TARGET_END_DATE} 23:59:59")
        df = df.loc[mask].copy()
        
        print(f"   - 원본 데이터 건수 (기간 필터링 후): {len(df)}건")

        # ---------------------------------------------------------
        # [핵심] 중복(Overlap) 제거 로직
        # ---------------------------------------------------------
        # 충전소별, 도착시간별 정렬
        df = df.sort_values(by=['stationID', 'connectionTime'])
        
        overlap_count = 0
        modified_sessions = []
        
        # 충전소별로 그룹화하여 처리
        for station, group in df.groupby('stationID'):
            group = group.sort_values(by='connectionTime')
            
            # 이전 세션 정보 초기화
            prev_idx = None
            prev_disconnect = None
            
            for idx, row in group.iterrows():
                curr_connect = row['connectionTime']
                curr_disconnect = row['disconnectTime']
                
                # 이전 세션과 겹치는지 확인
                if prev_idx is not None and prev_disconnect > curr_connect:
                    # 겹침 발생! -> 이전 세션의 종료 시간을 현재 세션 시작 시간으로 강제 조정
                    # (뒷 차가 오려면 앞 차가 비켜줬어야 하므로)
                    df.at[prev_idx, 'disconnectTime'] = curr_connect
                    overlap_count += 1
                    
                    # 만약 수정 후 주차 시간이 0이나 음수가 되면 삭제 대상이 될 수 있음
                    # 여기서는 일단 시간만 수정하고 나중에 일괄 필터링
                
                # 현재 세션 정보를 '이전 세션'으로 업데이트
                prev_idx = idx
                
                # 방금 수정되었을 수도 있으므로 df에서 다시 값을 가져오거나 논리적으로 갱신
                # (여기서는 현재 세션의 종료 시간은 그대로이므로 이것을 다음 비교의 기준(prev)으로 삼음)
                prev_disconnect = curr_disconnect

        print(f"   - 중복 시간 수정 완료: 총 {overlap_count}건의 세션 종료 시간 조정됨.")

        # 6. 데이터 유효성 재검사 (전처리 후처리)
        # 종료 시간이 시작 시간보다 빠르거나 같은 경우 (수정 과정에서 생길 수 있음) 제거
        # 에너지 값이 0 이하인 경우 제거
        
        # 주차 시간 재계산
        df['parking_duration_hours'] = (df['disconnectTime'] - df['connectionTime']).dt.total_seconds() / 3600
        
        valid_mask = (df['parking_duration_hours'] > 0.05) & (df['kWhDelivered'] > 0) # 최소 3분 이상 주차
        clean_df = df.loc[valid_mask].copy()
        
        removed_count = len(df) - len(clean_df)
        print(f"   - 유효하지 않은 세션 제거 (짧은 주차/에너지0): {removed_count}건")
        print(f"   - 최종 유효 데이터 건수: {len(clean_df)}건")
        
        return clean_df

    except Exception as e:
        print(f"오류 발생: {e}")
        return pd.DataFrame()

# 테스트 실행
if __name__ == "__main__":
    df_cleaned = load_and_clean_data()
    
    if not df_cleaned.empty:
        print("\n[샘플 데이터 확인]")
        print(df_cleaned[['stationID', 'connectionTime', 'disconnectTime', 'kWhDelivered']].head(10))
        
        # 전처리된 데이터를 CSV로 저장해두면 다른 코드에서 쓰기 편함
        output_csv = SAVE_DIR+"/cleaned_ev_data_2024_09.csv"
        df_cleaned.to_csv(output_csv, index=False)
        print(f"\n전처리된 데이터가 '{output_csv}'로 저장되었습니다.")