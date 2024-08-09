'''
Created on 2015. 12. 11.

@author: cpslab
'''

from util.MFile import CFile
from log.MLog import CLog
from log.MConfig import CConfig
import util.MRand as mr

class gl:
#     path=1
    path=2
#     path=3
#     path=4



def test1():
    for i in range(1,10):
        c=CConfig()
        c.init_w("ev/var/config"+str(i)+".txt")
        c.addKV("num","10")
        c.addKV("prob",format(i*0.1,".2f"))
        c.addKV("k3","v3")
        c.end()    


def test2():
#     run(1)
    for i in range(1,10):
        run(i)

def gen(fn,c):
    cf=CFile()
    cf.open_w(fn)
    t=0
    end_t=20
    arr_p=float(c.get('prob'))
    while t<=end_t:
        if mr.pick()>arr_p:
            t+=1
            continue
        d=mr.pickInt(5, 20)
        m=mr.pickInt(2,7)
        o=mr.pickInt(1,4)
        str="%d %d %d %d"%(t,d,m,o)
        cf.write(str)
        t+=1
    cf.end()

    
def run(idx):
    c=CConfig()
    fn="ev/var/config"+str(idx)+".txt"
    c.init_r(fn)
    n=int(c.get('num'))
    for i in range(n):
        fn="ev/var/data"+str(idx)+"-"+str(i)+".txt"
        gen(fn,c)
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
