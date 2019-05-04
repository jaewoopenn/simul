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

    def add_st(self,x,y):
        self.xl.append(x)
        self.vl.append(y)
        self.lasty=y
        
    def add_hori(self,x):
        self.xl.append(x)
        self.vl.append(self.lasty)

    def add_diag(self,x,inc):
        self.xl.append(x)
        self.vl.append(self.lasty)
        self.xl.append(x+inc)
        self.lasty+=inc
        self.vl.append(self.lasty)

    def add_vert(self,x,inc):
        self.xl.append(x-0.01)
        self.vl.append(self.lasty)
        self.xl.append(x)
        self.lasty+=inc
        self.vl.append(self.lasty)
    
    def draw(self):
        mp.plot(self.xl,self.vl)
        
    def show(self):
        mp.ylim(0,11)
        mp.ylabel("Resources")
        mp.xlabel("Time")
        mp.show()
        