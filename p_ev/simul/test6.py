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
#     path=3
#     path=4
    path=5
    idx=100
    end_t=40
    max_s=3
    
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
    csa=[]
    reject=0
    for i in range(gl.max_s):
        cs=CSimulF()
        cs.clear()
        csa.append(cs)
    cl=CLog("ev/data/"+fn+".txt")
    
    t=0
    while t<gl.end_t:
        while t==cl.getLast():
            w=cl.getW()
            add_ok=0
            for i in range(gl.max_s):
                if add_fifo_ok(t,w,csa[i]):
                    add_com(t,w,csa[i])
                    add_ok=1
                    break
            if add_ok==0:
                reject+=1
                prn("error {}".format(gl.reject))
                
        for i in range(gl.max_s):
            msf.simul_t(csa[i],t)
        t+=1
    return reject

def run_edf(fn):
    csa=[]
    reject=0
    for i in range(gl.max_s):
        cs=CSimul()
        cs.clear()
        csa.append(cs)

    cl=CLog("ev/data/"+fn+".txt")
    
    t=0
    while t<gl.end_t:
        while t==cl.getLast():
            w=cl.getW()
            add_ok=0
            for i in range(gl.max_s):
                if add_edf_ok(t,w,csa[i]):
                    add_com(t,w,csa[i])
                    add_ok=1
                    break
            if add_ok==0:
                reject+=1
                prn("error {}".format(gl.reject))
                
        for i in range(gl.max_s):
            ms.simul_t(csa[i],t)

        t+=1
    return reject


def test1(): 
    fn="test8"
#     fn="test5"
    ret=run_fifo(fn)
    print(fn,ret)

'''
..
'''

def test2():
    fn="test5"
#     fn="test5"
    ret=run_edf(fn)
    print(fn,ret)


'''
..
'''

def test3():
    sum=0
    for i in range(10):
        fn="test"+str(i)
        ret=run_fifo(fn)
        sum+=ret
        print(fn,ret)
    print(sum)


def test4():
    sum=0
    for i in range(10):
        gl.reject=0
        fn="test"+str(i)
        ret=run_edf(fn)
        sum+=ret
        print(fn,ret)
    print(sum)

def test5():
    sum1=0
    sum2=0
    for i in range(10):
        fn="test"+str(i)
        ret=run_fifo(fn)
        sum1+=ret
        ret=run_edf(fn)
        sum2+=ret
    print(sum1,sum2)

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