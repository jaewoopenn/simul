'''
Created on 2019. 4. 29.

@author: JWLEE
'''
class gl:
#     path="d:/data/"
    path="/data/"

def load(fn):
    i_f = open(gl.path+fn,"r")
    v=[]
    for line in i_f:
        val=line.strip()
        v.append(val)
    return v        