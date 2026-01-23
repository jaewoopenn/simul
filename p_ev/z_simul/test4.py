'''
Created on 2015. 12. 11.

@author: cpslab
'''

from z_log.MLog import CLog
from z_simul.MSimul import CSimul
import z_simul.MSimul as ms
from z_simul.MSimulF import CSimulF
import z_simul.MSimulF as msf

class gl:
#     path=1
    path=2
#     path=3
#     path=4
#     path=5
    idx=100
    reject=0
    
    b_gap=0
#     b_gap=1
    
    b_sim=0
#     b_sim=1

    bPrn=0
#     bPrn=1
def prn(str):
    if not gl.bPrn:
        return
    print(str)    

def job_add_edf(t,w,cs):
    e=(gl.idx,t+w[1],w[2],w[3])
    ret=ms.add_ok(cs, e, t)
    if ret:
        cs.add_ed(e)
        gl.idx+=1
    else:
        gl.reject+=1
        prn("error {}".format(gl.reject))


def job_add_fifo(t,w,cs):
    e=(gl.idx,t+w[1],w[2],w[3])
    ret=msf.add_ok(cs,e,t)
    if ret:
        cs.add(e)
#         cs.prn()
        gl.idx+=1
    else:
        gl.reject+=1
        prn("error {}".format(gl.reject))

                
def run_fifo(fn):
    end_t=30
#     fn="test2"
    cs=CSimulF()
    cs.clear()
#     cs.opt_r=0.9
    cl=CLog("ev/data/"+fn+".txt")
    
    t=0
    while t<end_t:
        while t==cl.getLast():
            w=cl.getW()
            job_add_fifo(t,w,cs)
        if gl.b_sim:
            cs.prn()
        msf.simul_t(cs,t)
        t+=1
    return gl.reject

def run_edf(fn):
    cs=CSimul()
    cs.clear()
#     cs.opt_r=0.5
#     cs.opt_r=0.2
    cs.opt_r=0
    cl=CLog("ev/data/"+fn+".txt")
    
    t=0
    end_t=30
    while t<end_t:
        cs.gap_after(t)
        while t==cl.getLast():
            w=cl.getW()
            job_add_edf(t,w,cs)
        if gl.b_gap:
            cs.prn_gap()
        if gl.b_sim:
            cs.prn()
        ms.simul_t(cs,t)
        t+=1
    return gl.reject

def test1(): 
    sum=0
    for i in range(10):
        gl.reject=0
        fn="test"+str(i)
        ret=run_fifo(fn)
        sum+=ret
        print(fn,ret)
    print(sum)

'''
..
'''

def test2():
    gl.reject=0
    fn="opt1"
    ret=run_edf(fn)
    print(fn,ret)


'''
..
'''

def test3():
    sum1=0
    sum2=0
    for i in range(10):
        fn="test"+str(i)
        gl.reject=0
        ret=run_fifo(fn)
        sum1+=ret
        gl.reject=0
        ret=run_edf(fn)
        sum2+=ret
    print(sum1,sum2)

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
