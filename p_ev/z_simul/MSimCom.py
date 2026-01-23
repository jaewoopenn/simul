'''
Created on 2024. 8. 7.

@author: jaewoo
'''
import math
class gl:
    idx=100
    bPrn=0
#     bPrn=1

def prn(str):
    if not gl.bPrn:
        return
    print(str)

def isPrn():
    return gl.bPrn

def add_com(t,w,cs):
    mod=math.ceil(w[2]*cs.chr_r)
    e=(gl.idx,t+w[1],mod,w[3]) # id, deadline, req, opt
    cs.add(e)
    gl.idx+=1