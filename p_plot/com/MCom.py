'''
Created on 2019. 5. 3.

@author: JWLEE
'''
import util.MPlot as mp
import file.MFile as mf


def load_fn(c,fn):
    lst=mf.load(fn)
    for m in lst:
        wd=m.split()
        print(wd)
        a=int(wd[1])
        if wd[0]=='s':
            b=float(wd[2])
            c.add_st(a,b)
        elif wd[0]=='d':
            c.add_diag(a)
        elif wd[0]=='v':
            c.add_vert(a)
        elif wd[0]=='h':
            c.add_hori(a)
    

class C_Draw:
    def __init__(self):
        self.xl=[]
        self.vl=[]
        self.lastx=0
        self.lasty=0
        
    def clear(self):
        self.xl=[]
        self.vl=[]

    def add_st(self,x,y):
        self.lastx=x
        self.lasty=y
        self.xl.append(self.lastx)
        self.vl.append(self.lasty)
        
    def add_hori(self,inc):
        self.lastx+=inc
        self.xl.append(self.lastx)
        self.vl.append(self.lasty)

    def add_diag(self,inc):
        self.lastx+=inc
        self.lasty+=inc
        self.xl.append(self.lastx)
        self.vl.append(self.lasty)

    def add_vert(self,inc):
        self.lastx+=0.01
        self.lasty+=inc
        self.xl.append(self.lastx)
        self.vl.append(self.lasty)
    
    def draw(self):
        mp.plot(self.xl,self.vl)
    def draw2(self, l):
        mp.plot4(self.xl,self.vl,l)

    def lim(self,x,y):
        mp.xlim(0,x)
        mp.ylim(0,y)
        
        
    def show(self):
        mp.ylabel("Resources")
        mp.xlabel("Time")
        mp.show()
        