'''
Created on 2015. 12. 11.

@author: cpslab
'''

from log.MLog import CLog
from anal.MGap import TD,CGap
import anal.MGap as mg
from simul.MSimul import CSimul
import simul.MSimul as ms
from simul.MSimulF import CSimulF
import simul.MSimulF as msf

class gl:
#     path=1
#     path=2
#     path=3
    path=4
#     path=5
    idx=100
    reject=0
    
#     b_gap=0
    b_gap=1
    
#     b_sim=0
    b_sim=1
    


def dem_add3(gap,td,t):
    gap.prune(t)
    ret=mg.gap_add(gap,td,t)
    if not ret:
        gl.reject+=1
        print("error",gl.reject)
        return 0
    gap.compact()
    return 1

def job_add(cs,w,t):
    cs.add_ed((gl.idx,t+w[1],w[2],w[3]))
    gl.idx+=1

def job_add2(cs,w,t):
    ret=cs.add((gl.idx,t+w[1],w[2],w[3]),t)
    if not ret:
        gl.reject+=1
        print("error",gl.reject)
        return 0

    gl.idx+=1
                
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
#     fn="opt1"
    fn="opt2"
    g=CGap()
    cs=CSimul()
    cs.opt_r=0.4
    cl=CLog("ev/ex/"+fn+".txt")
    
    t=0
    end_t=20
    while t<end_t:
        g.vec=g.after(t)
        while t==cl.getLast():
            w=cl.getW()
            ret=dem_add3(g,TD(t+w[1],w[2]),t)
            if ret:
                job_add(cs,w,t)
#         if t>=2 and t<4:
#             g.consume()
        if gl.b_sim:
            cs.prn()
        if gl.b_gap:
            g.prn_vec(t)
        ms.simul_t(g,cs,t)
        t+=1
    print(g.vec)
def test4():
    fn="test"
    g=CGap()
    cs=CSimul()
#     cs.opt_r=0.9
    cs.opt_r=0
    cl=CLog("ev/data/"+fn+".txt")
    
    t=0
    end_t=30
    while t<end_t:
        g.vec=g.after(t)
        while t==cl.getLast():
            w=cl.getW()
            ret=dem_add3(g,TD(t+w[1],w[2]),t)
            if ret:
                job_add(cs,w,t)
        if gl.b_sim:
            cs.prn()
        if gl.b_gap:
            g.prn_vec(t)
        ms.simul_t(g,cs,t)
        t+=1
    g.prn_vec(t)
    print(gl.reject)

def test5():
    fn="test"
#     fn="test2"
    cs=CSimulF()
#     cs.opt_r=0.9
    cl=CLog("ev/data/"+fn+".txt")
    
    t=0
    end_t=30
    while t<end_t:
        while t==cl.getLast():
            w=cl.getW()
            job_add2(cs,w,t)
        if gl.b_sim:
            cs.prn()
        msf.simul_t(cs,t)
        t+=1
    print(gl.reject)
def main():
    if gl.path==1:
        test1()
    elif gl.path==2:
        test2()
    elif gl.path==3:
        test3()
    elif gl.path==4:
        test4()
    elif gl.path==5:
        test5()

if __name__ == '__main__':
    main()
