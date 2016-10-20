'''
Created on 2016. 10. 19.

@author: jaewo
'''
def loadPrn(fn):
    i_f = open("C:/Users/jaewoo/data/file/test.txt","r")
    for line in i_f:
        val=line.strip()
        print val
    
def load(fn):
    i_f = open("C:/Users/jaewoo/data/file/test.txt","r")
    v=[]
    for line in i_f:
        val=line.strip()
        v.append(val)
    return v
    