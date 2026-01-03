'''
Created on 2015. 12. 11.

@author: cpslab


'''

from z_anal.MDemand import CJob
from z_anal.MDemand import CDemand


class gl:
#     path=1
    path=2
#     path=3
#     path=4


class Gap:
    glist=[]
    def add(self,e):
        self.glist.append(e)
    def get_t(self,t):
        tv=[]
        for e in self.glist:
            if e[0]>=t:
                tv.append(e)
        return tv
    def add_c(self,e):
        self.glist.append(e)
def test1():
    v= CDemand()
    v.add(CJob(2,1,0))
    v.add(CJob(7,2,0))
    v.findMax()
    v.printMax()

def test2():
    g=Gap()
    g.add((5,1))
    g.add((12,3))
    g.add((10,4))
    g.glist.sort()
    print(g.glist)
    v=g.get_t(7)
    print(v)
    
    pass

def test3():
    pass

def test4():
    pass
    
def main():
    if gl.path==1:
        test1()
    elif gl.path==2:
        test2()
    elif gl.path==3:
        test3()
    elif gl.path==4:
        test4()

if __name__ == '__main__':
    main()
