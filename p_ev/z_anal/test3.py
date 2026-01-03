'''
Created on 2015. 12. 11.

@author: cpslab
'''
import util.MRand as mr

class gl:
    path=1
    
def test1():
    vec=[]
    vec.append((4,3))
    vec.append((5,4))
    vec.append((12,1))
    vec.append((13,1))
    vec.append((20,5))
    tv=[]
    old_d=100
    for e in vec:
        if e[1]<=old_d:
            tv=[]
            tv.append(e)
            old_d=e[1]
        if e[1]>old_d:
            tv.append(e)
    print(tv)
    opt=0
    for t in range(0,25):
        if t==tv[0][0]:
            tv.remove(tv[0])
        if len(tv)==0:
            break
        if t<tv[0][0]:
            exe=tv[0][1]-opt
            print(t,exe)
            if exe>0:
                opt+=1
#         print(t,"opt",)
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
