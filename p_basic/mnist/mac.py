'''
Created on 2025. 9. 4.

@author: jaewoo
'''
import torch

# MPS(Mac GPU) 지원 여부 확인
print(f"PyTorch 버전: {torch.__version__}")
print(f"MPS 빌드 지원 여부: {torch.backends.mps.is_built()}")
print(f"MPS 사용 가능 여부: {torch.backends.mps.is_available()}")

# MPS가 가능하면 Mac GPU, 아니면 CPU 사용
device = torch.device("mps") if torch.backends.mps.is_available() else torch.device("cpu")

print(f"선택된 device: {device}")

# 텐서 또는 모델을 해당 디바이스로 이동
x = torch.ones(5, device=device)
print(x)

# 모델도 동일하게 이동 가능
import torch.nn as nn
model = nn.Linear(5, 3).to(device)
output = model(x)
print(output)
