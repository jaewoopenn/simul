'''
Created on 2019. 5. 3.

@author: JWLEE
'''
import util.MPlot as mp

class C_Draw:
    def __init__(self):
        self.xl=[]
        self.vl=[]
        self.lasty=0
        
    def clear(self):
        self.xl=[]
        self.vl=[]

    def add_p(self,x,y):
        self.xl.append(x)
        self.vl.append(y)
        self.lasty=y

    def add_s(self,x,y):
        inc=y-self.lasty
        self.xl.append(x)
        self.vl.append(self.lasty)
        self.xl.append(x+inc)
        self.vl.append(y)
        self.lasty=y

    def add_d(self,x,y):
        self.xl.append(x-0.01)
        self.vl.append(self.lasty)
        self.xl.append(x)
        self.vl.append(y)
        self.lasty=y
    
    def draw(self):
        mp.plot(self.xl,self.vl)
        
    def show(self):
        mp.ylim(0,11)
        mp.ylabel("Resources")
        mp.xlabel("Time")
        mp.show()
        