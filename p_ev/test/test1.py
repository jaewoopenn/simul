'''
Created on 2015. 12. 11.

@author: cpslab
'''
import util.MRand as mr

class gl:
    path=1
    
def test1():
    a=mr.pickInt(1, 5)
    print(a)

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
    print("hihi")

if __name__ == '__main__':
    main()
