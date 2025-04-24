'''
Created on 2015. 12. 11.

@author: cpslab
'''
import util.MRand as mr
class gl:
    path=1
#     path=2
#     path=3
    # path=4

def test1():
    p=mr.pick()
    print(p)

def test2():
    t=0
    while t<10:
        p=mr.pickU(0, 1)
        if p>0.5:
            print(t,p)
        t+=1
    pass

def test3():
    t=0
    while t<10:
        for i in range(3):
            p=mr.pick()
            if p>0.5:
                print(t,i,p)
        t+=1
    pass

def test4():
#     p=mr.pickInt(5, 10)
    p=mr.pickInt(-2, 3)
    p2=max(p,0)
    print(p,p2)

    
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
