'''
Created on 2019. 4. 29.

@author: JWLEE
'''
from pathlib import Path
import sys
class gl:
#     path="d:/data/"
    path=str(Path.home())+"/data/"

class CFile:
    i_f=0
    def __init__(self):
        pass
    def open_r(self,fn):
        self.i_f=open(gl.path+fn,"r")
    def open_w(self,fn):
        self.i_f=open(gl.path+fn,"w")
        
    def getLine(self):
        v=self.i_f.readline()
        return v.strip()

    def getWords(self):
        v=self.i_f.readline()
        return v.strip().split()
    def getKV(self):
        v=self.i_f.readline()
        if not v:
            return 
        kv=v.strip().split()
        return kv[0],kv[1]

    def getInts(self):
        v=0
        while True:
            v=self.i_f.readline()
            w=v.strip().split()
            if not w:
                return 
            if w[0]!="#":
                break
        tv=[]
        for e in w:
            tv.append(int(e))
        return tv

    def prn(self):
        for line in self.i_f:
            val=line.strip()
            print(val)
    def write(self,str1):
        self.i_f.write(str1+"\n")
    def writeKV(self,key,value):
        self.i_f.write(key+" "+value+"\n")
    def end(self):
        self.i_f.close()

def load(fn):
    i_f = open(gl.path+fn,"r")
    v=[]
    for line in i_f:
        val=line.strip()
        v.append(val)
    return v    
def filepath(fn):
    return gl.path+fn;    

