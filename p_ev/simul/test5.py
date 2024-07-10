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
    path=1
#     path=2
#     path=3
#     path=4
#     path=5
    idx=100
    reject=0
    
#     b_gap=0
    b_gap=1
    
#     b_sim=0
    b_sim=1

    bPrn=0
#     bPrn=1
def prn(str):
    if not gl.bPrn:
        return
    print(str)    

def dem_add3(gap,td,t):
    gap.prune(t)
    ret=mg.gap_add(gap,td,t)
    if not ret:
        gl.reject+=1
        prn("error {}".format(gl.reject))
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
        prn("error {}".format(gl.reject))
        return 0

    gl.idx+=1
                
def run(fn):
    end_t=30
#     fn="test2"
    cs=CSimulF()
#     cs.opt_r=0.9
    cl=CLog("ev/data/"+fn+".txt")
    
    t=0
    while t<end_t:
        while t==cl.getLast():
            w=cl.getW()
            job_add2(cs,w,t)
        if gl.b_sim:
            cs.prn()
        msf.simul_t(cs,t)
        t+=1
    return gl.reject

def test1(): 
    for i in range(10):
        gl.reject=0
        fn="test"+str(i)
        ret=run(fn)
        print(fn,ret)

'''
..
'''

def test2():
    pass

'''
..
'''

def test3():
    pass

def test4():
    pass

def test5():
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
    elif gl.path==5:
        test5()

if __name__ == '__main__':
    main()
