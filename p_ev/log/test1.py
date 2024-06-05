'''
Created on 2015. 12. 11.

@author: cpslab
'''

from util.MFile import CFile


class gl:
#     path=1
    path=2
#     path=3
#     path=4

    
def test1():
    cf=CFile()
    cf.open_w("ev/test.txt")
    t=0
    while True:
        t+=1
        d=t+10
        m=5
        o=1
        str="%d %d %d %d"%(t,d,m,o)
        cf.write(str)
        if t==10:
            break
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
