'''
Created on 2025. 11. 19.

@author: jaewoo
'''
import pandas as pd
import matplotlib.pyplot as plt
import os
class gl_input:
    fn="test/rs.txt"
    xlab= "Utilization Bound(%)"
    ylab= "Acceptance Ratio"
    output="suc.pdf"
class gl:
    path="~/data/ev/"

# 데이터 파일을 읽습니다. 공백 문자를 구분자로 사용합니다.
df = pd.read_csv(gl.path+gl_input.fn, sep=r'\s+')

# 그래프를 그립니다.
plt.figure(figsize=(10, 6))

# 'xx'를 x축으로, 각 열을 y축으로 하는 라인 플롯을 그립니다.
plt.plot(df['xx'], df['Demand'], label='Demand', marker='o')
plt.plot(df['xx'], df['FIFO'], label='FIFO', marker='s')
plt.plot(df['xx'], df['util'], label='util', marker='^')

# 제목 및 축 레이블을 설정합니다.
plt.xlabel('xx')
plt.ylabel('Value')
plt.title('Demand, FIFO, and util vs. xx')

# 범례를 표시합니다.
plt.legend()

# 그리드를 추가합니다.
plt.grid(True)

# x축 눈금을 데이터의 'xx' 값으로 설정합니다.
plt.xticks(df['xx']) 

# 그래프를 파일로 저장합니다.
expanded_path = os.path.expanduser(gl.path+gl_input.output)
plt.savefig(expanded_path)