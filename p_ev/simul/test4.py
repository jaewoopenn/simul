'''
Created on 2015. 12. 11.

@author: cpslab
'''

from log.MLog import CLog
from anal.MGap import TD,CGap
import anal.MGap as mg
from simul.MSimul import CSimul
import simul.MSimul as ms

class gl:
#     path=1
#     path=2
    path=3
#     path=4
    idx=100
    b_gap=0
#     b_gap=1
#     b_sim=0
    b_sim=1


def dem_add3(gap,td,t):
    gap.prune(t)
    ret=mg.gap_add(gap,td,t)
    gap.compact()
    if not ret:
        print("error")

            
def test1(): 
    fn="opt1"
    cl=CLog("ev/"+fn+".txt")
    g=CGap()
    
    
    t=0
    end_t=20
    g.std=0
    while t<end_t:
        g.vec=g.after(t)
        while t==cl.getLast():
            w=cl.getW()
            dem_add3(g,TD(t+w[1],w[2]),t)
#         if t>=2 and t<4:
#             g.consume()
        print(t, g.vec)
        if g.vec:
            allow=int(g.vec[0][1]*0.2)
            print(allow)
#             print(g.vec[0][1])
        t+=1
    print(g.vec)


'''
..
'''
def job_add(cs,w,t):
    cs.add_ed((gl.idx,t+w[1],w[2],w[3]))
    gl.idx+=1
    
def test2():
    
#     cl=CLog("ev/test8.txt")
    cl=CLog("ev/preempt1.txt")
    cs=CSimul()
    t=0
    end_t=11
    while t<end_t:
        while t==cl.getLast():
            w=cl.getW()
            job_add(cs,w,t)
        cs.prn()
        ms.simul_t(cs,t)
        
        t+=1
#     print(g.vec)
#     v.prn()


'''
merged version: gap, simul 
using gap, implement opt exec. std 0~1
'''

def test3():
#     fn="preempt1"
#     fn="preempt2"
#     fn="test8"
    fn="opt1"
    g=CGap()
    cs=CSimul()
    cl=CLog("ev/"+fn+".txt")
    
    t=0
    end_t=20
    g.std=0
    while t<end_t:
        g.vec=g.after(t)
        while t==cl.getLast():
            w=cl.getW()
            dem_add3(g,TD(t+w[1],w[2]),t)
            job_add(cs,w,t)
#         if t>=2 and t<4:
#             g.consume()
        if gl.b_sim:
            cs.prn()
        if gl.b_gap:
            print(t, g.vec)    
        ms.simul_t(g,cs,t)
        t+=1
    print(g.vec)
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
