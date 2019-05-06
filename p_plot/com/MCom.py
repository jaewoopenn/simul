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
        b=0
        if(len(wd)>2):
            b=float(wd[2])
        if wd[0]=='s':
            c.add_st(a,b)
        elif wd[0]=='d':
            c.add_diag(a,b)
        elif wd[0]=='v':
            c.add_vert(a,b)
        elif wd[0]=='h':
            c.add_hori(a)
    

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
        