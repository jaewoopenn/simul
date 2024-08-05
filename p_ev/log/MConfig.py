'''
Created on 2024. 8. 5.

@author: jaewoo
'''
from util.MFile import CFile
class CConfig:
    cf=CFile()
    fn=""
    kv={}
    def __init__(self):
        pass
    def init_w(self,fn):
        self.cf.open_w(fn)
    def addKV(self,k,v):
        self.cf.writeKV(k,v)
    def end(self):
        self.cf.end()
    def init_r(self,fn):
        self.cf.open_r(fn)
        self.kv=dict()
        while True:
            w=self.cf.getKV()
            if not w:
                break
            self.kv[w[0]]=w[1]        
    def get(self,k):
        return self.kv.get(k)