'''
Created on 2025. 9. 18.

@author: jaewoo
'''
import matplotlib.pyplot as plt
import csv
plt.rcParams['axes.unicode_minus'] =False
plt.rcParams['font.family'] = 'AppleGothic'



# 파일명 지정
filename = 'stock.txt'

stocks = []
weights = []

# CSV 파일 읽기
with open(filename, 'r', encoding='utf-8') as file:
    reader = csv.reader(file)
    for row in reader:
        if len(row) == 2:  # 종목과 비중이 있는 경우
            stocks.append(row[0].strip())
            weights.append(float(row[1].strip()))

# 파이 차트 그리기
plt.figure(figsize=(8,8))
plt.pie(weights, labels=stocks, autopct='%1.1f%%', startangle=90,counterclock=False)
plt.title('주식 투자 현황')
plt.axis('equal')
plt.show()
