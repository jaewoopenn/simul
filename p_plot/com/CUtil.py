'''
Created on 2019. 5. 3.

@author: JWLEE
'''
import util.MPlot as mp

class C_Draw:
    def __init__(self):
        self.xl=[]
        self.vl=[]
        
    def clear(self):
        self.xl=[]
        self.vl=[]

    def add_p(self,x,y):
        self.xl.append(x)
        self.vl.append(y)

    def add_d(self,x,y1,y2):
        self.xl.append(x-0.01)
        self.vl.append(y1)
        self.xl.append(x)
        self.vl.append(y2)
    
    def add_s(self,x,y1,y2):
        inc=y2-y1
        self.xl.append(x)
        self.vl.append(y1)
        self.xl.append(x+inc)
        self.vl.append(y2)
    def draw(self):
        mp.plot(self.xl,self.vl)
        
    def show(self):
        mp.ylim(0,11)
        mp.ylabel("Resources")
        mp.xlabel("Time")
        mp.show()
        