'''
Created on 2019. 4. 29.

@author: JWLEE
'''
from pathlib import Path
class gl:
#     path="d:/data/"
    path=str(Path.home())+"/data/"

def load(fn):
    i_f = open(gl.path+fn,"r")
    v=[]
    for line in i_f:
        val=line.strip()
        v.append(val)
    return v        

def filepath(fn):
    return gl.path+fn;    