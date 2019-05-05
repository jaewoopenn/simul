'''
Created on 2019. 4. 29.

@author: JWLEE
'''

def load(fn):
    i_f = open(fn,"r")
    v=[]
    for line in i_f:
        val=line.strip()
        v.append(val)
    return v        