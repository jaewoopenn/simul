'''
Created on 2015. 12. 11.

@author: cpslab
'''

from log.MLog import CLog
from simul.MSimul import CSimul
import simul.MSimul as ms
from simul.MSimulF import CSimulF
import simul.MSimulF as msf
import math
class gl:
#     path=1
#     path=2
#     path=3
#     path=4
    path=5

    idx=100
    end_t=40
    
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

def job_add_edf(t,w,cs):
    cs.chr_r=0.7
#     cs.chr_r=1
    mod=math.ceil(w[2]*cs.chr_r)
    e=(gl.idx,t+w[1],mod,w[3])
    ret=ms.add_ok(cs, e, t)
    if ret:
        cs.add(e)
        gl.idx+=1
        return 1
    else:
        prn("error ")
        return 0


def job_add_fifo(t,w,cs):
    cs.chr_r=0.7
#     cs.chr_r=1
    mod=math.ceil(w[2]*cs.chr_r)
    e=(gl.idx,t+w[1],mod,w[3])
    
    ret=msf.add_ok(cs,e,t)
    if ret:
        cs.add(e)
#         cs.prn()
        gl.idx+=1
        return 1
    else:
        prn("error ")
        return 0

                
def run_fifo(fn):
    cs=CSimulF()
    cs.clear()
#     cs.opt_r=0.9
    cl=CLog("ev/"+fn+".txt")
    
    t=0
    reject=0
    while t<gl.end_t:
        while t==cl.getLast():
            w=cl.getW()
            if job_add_fifo(t,w,cs)==0:
                reject+=1
        msf.simul_t(cs,t)
        t+=1
#     print("OPT:",cs.tot_opt)
    return reject, cs.tot_opt

def run_edf(fn):
    cs=CSimul()
    cs.clear()
#     cs.opt_r=0.5
    cs.opt_r=0.2
#     cs.opt_r=0
    cl=CLog("ev/"+fn+".txt")
    reject=0
    t=0
    while t<gl.end_t:
        cs.gap_after(t)
        while t==cl.getLast():
            w=cl.getW()
            if job_add_edf(t,w,cs)==0:
                reject+=1
        ms.simul_t(cs,t)
        t+=1
#     print("OPT:",cs.tot_opt)
    return reject, cs.tot_opt

def test1(): 
    sum=0
    opt_s=0
    for i in range(10):
        gl.reject=0
        fn="data/test"+str(i)
        ret,opt=run_fifo(fn)
        sum+=ret
        opt_s+=opt
        print(fn,ret,opt)
    print(sum,opt_s)

'''
..
'''

def test2():
    sum=0
    opt_s=0
    for i in range(10):
        fn="data/test"+str(i)
        ret,opt=run_edf(fn)
        sum+=ret
        opt_s+=opt
        print(fn,ret,opt)
    print(sum,opt_s)


'''
..
'''

def test3():
    sum1=[0,0]
    sum2=[0,0]
    for i in range(10):
        fn="data/test"+str(i)
        ret=run_fifo(fn)
        sum1=[sum1[0]+ret[0],sum1[1]+ret[1]]
        ret=run_edf(fn)
        sum2=[sum2[0]+ret[0],sum2[1]+ret[1]]
    print(sum1,sum2)


def test4():
    fn="ex/multiclass2"
    ret, opt=run_edf(fn)
    print(fn,ret,opt)


def test5():
    fn="ex/multiclass"
    ret,opt=run_fifo(fn)
    print(fn,ret,opt)

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
