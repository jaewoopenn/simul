'''
Created on 2015. 12. 11.

@author: cpslab
'''

from log.MLog import CLog
from simul.MSimulF import CSimulF
import simul.MSimulF as msf
from simul.MSimul import CSimul
import simul.MSimul as ms

class gl:
#     path=1
#     path=2
    path=3
#     path=4
#     path=5
    idx=100
    end_t=40
    reject=0
    
    
    b_sim=0
#     b_sim=1

    bPrn=0
#     bPrn=1
def prn(str):
    if not gl.bPrn:
        return
    print(str)    


def add_fifo_ok(t,w,cs): 
    e=(gl.idx,t+w[1],w[2],w[3])
    return msf.add_ok(cs,e,t)
def add_edf_ok(t,w,cs): 
    if w[1]<w[2]:
        return 0
    e=(gl.idx,t+w[1],w[2],w[3])
    return ms.add_ok(cs,e,t)
def add_com(t,w,cs):
    e=(gl.idx,t+w[1],w[2],w[3])
    cs.add(e)
    gl.idx+=1

                
def run_fifo(fn):
#     fn="test2"
    cs=CSimulF()
    cs.clear()
    cs2=CSimulF()
    cs2.clear()
#     cs.opt_r=0.9
    cl=CLog("ev/data/"+fn+".txt")
    
    t=0
    while t<gl.end_t:
        while t==cl.getLast():
            w=cl.getW()
            if add_fifo_ok(t,w,cs):
                add_com(t,w,cs)
            elif add_fifo_ok(t,w,cs2):
                add_com(t,w,cs2)
            else:
                gl.reject+=1
                prn("error {}".format(gl.reject))
                
        msf.simul_t(cs,t)
        msf.simul_t(cs2,t)
        t+=1
    return gl.reject

def run_edf(fn):
#     fn="test2"
    cs=CSimul()
    cs.clear()
    cs2=CSimul()
    cs2.clear()
#     cs.opt_r=0.9
    cl=CLog("ev/data/"+fn+".txt")
    
    t=0
    while t<gl.end_t:
        while t==cl.getLast():
            w=cl.getW()
            if add_edf_ok(t,w,cs):
                add_com(t,w,cs)
            elif add_edf_ok(t,w,cs2):
                add_com(t,w,cs2)
            else:
                gl.reject+=1
                prn("error {}".format(gl.reject))
                
        ms.simul_t(cs,t)
        if gl.b_sim:
            cs2.prn()
            cs2.prn_gap()
        ms.simul_t(cs2,t)
        t+=1
    return gl.reject


def test1(): 
    gl.reject=0
    fn="test9"
#     fn="test5"
    ret=run_fifo(fn)
    print(fn,ret)

'''
..
'''

def test2():
    gl.reject=0
    fn="test9"
#     fn="test5"
    ret=run_edf(fn)
    print(fn,ret)


'''
..
'''

def test3():
    sum=0
    for i in range(10):
        gl.reject=0
        fn="test"+str(i)
        ret=run_fifo(fn)
#         ret=run_edf(fn)
        sum+=ret
        print(fn,ret)
    print(sum)


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
