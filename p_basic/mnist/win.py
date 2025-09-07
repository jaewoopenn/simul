'''
Created on 2025. 9. 7.

@author: jaewo
'''
import torch
import ctypes
try:
    ctypes.WinDLL("cudnn64_9.dll")  # 이름은 설치된 cuDNN 버전에 따라 다를 수 있음
    print("cuDNN DLL 로딩 성공: 설치 정상")
except OSError:
    print("cuDNN DLL 로딩 실패: 설치 문제 발생")
    
print("CUDA 사용 가능:", torch.cuda.is_available())
print("CUDA 버전:", torch.version.cuda)
print("PyTorch 버전:", torch.__version__)

if torch.cuda.is_available():
    print("GPU 개수:", torch.cuda.device_count())
    print("첫 번째 GPU 이름:", torch.cuda.get_device_name(0))
