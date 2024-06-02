'''
Created on 2015. 12. 11.

@author: cpslab
'''
import util.MRand as mr
from anal.MDemand import CJob
from anal.MDemand import CDemand

class gl:
    path=2

'''
vector 
item (t,rd,od)
'''

        
def test1():
    v= CDemand()
    v.add((1,2))
    v.add((4,7))
    v.findMax()
    v.printMax()
    v.add((3,5))
    v.sort()
    v.findMax()
    v.printMax()
#     print(vec)

def test2():
    v= CDemand()
    v.add(CJob(2,1,0))
    v.add(CJob(7,2,0))
    v.findMax()
    v.printMax()
    v.add(CJob(5,1,0))
    v.findMax()
    v.printMax()
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
    print("hihi")

if __name__ == '__main__':
    main()
