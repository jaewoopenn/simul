'''
Created on 2024. 6. 12.

@author: jaewoo
'''
class CQueue:
    q=[]
    def add(self,o):
        self.q.append(o)
        self.q.sort(key=None, reverse=False)
    def proceed(self):
        if not self.q:
            return
        self.q[0][1]-=1
        if self.q[0][1]==0:
            self.q.pop(0)
    def prn(self,t):
        print(t,self.q)