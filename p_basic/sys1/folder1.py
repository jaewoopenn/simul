'''
Created on 2025. 11. 27.

@author: jaewoo
'''
from pathlib import Path
# 현재 경로 객체 생성
p = Path('/users/jaewoo/data/subfolder')
def run(fn):
    try:
        with open(fn, 'r', encoding='utf-8') as f:
            content = f.read()
            print(content)
    except FileNotFoundError:
        print("파일을 찾을 수 없습니다.")
    except UnicodeDecodeError:
        print("파일을....")
        
# rglob('*')은 하위 폴더를 포함한 모든 패턴을 찾습니다.
for file in p.rglob('*'):
    if file.is_file():  # 폴더가 아닌 파일인 경우만 출력
        print(file)
        run(file)