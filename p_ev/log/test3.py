'''
Created on 2015. 12. 11.

@author: cpslab
'''

from util.MFile import CFile
from log.MLog import CLog
import util.MRand as mr

class gl:
    path=1
#     path=2
#     path=3
#     path=4

    
def test1():
    cf=CFile()
    cf.open_w("ev/data/test.txt")
    t=0
    end_t=20
    arr_p=0.6
    while t<=end_t:
        if mr.pick()>arr_p:
            t+=1
            continue
        d=mr.pickInt(7, 12)
        m=mr.pickInt(2,4)
        o=mr.pickInt(1,3)
        str="%d %d %d %d"%(t,d,m,o)
        cf.write(str)
        t+=1
    cf.end()
    
    pass


def test2():
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
