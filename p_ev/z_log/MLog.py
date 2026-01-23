'''
Created on 2024. 6. 7.

@author: jaewoo
'''
from util.MFile import CFile
class CLog:
    cf=CFile()
    last_w=[]
    def __init__(self,fn):
        self.cf.open_r(fn)
        self.last_w=self.cf.getInts()
    def getW(self):
        tw=self.last_w
        self.last_w=self.cf.getInts()
        return tw
        
    def getLast(self):
        if not self.last_w:
            return -1
        return self.last_w[0]