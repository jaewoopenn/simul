'''
Created on 2015. 12. 11.

@author: cpslab
'''
from simul.MSimul import CSimul
import simul.MSimul as ms

class gl:
#     path=1
    path=2


    
def test1(): # FIFO
    cs=CSimul()
    cs.add((1,10,3))
    cs.add((2,10,2))
    t=0
    while True:
        if t==6:
            cs.add((3,10,3))
        ms.simul_t(cs)
        if t==10:
            break
        t+=1
        

def test2():
    cs=CSimul()
    cs.add_ed((2,10,3))
    cs.add_ed((1,3,1))
    t=0
    while True:
        if t==2:
            cs.add_ed((3,5,2))
        ms.simul_t(cs)
        if t==10:
            break
        t+=1
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
