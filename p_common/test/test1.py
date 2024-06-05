'''
Created on 2015. 12. 11.

@author: cpslab
'''
from util.MFile import CFile
class gl:
#     path=1
#     path=2
#     path=3
    path=4

def test1():
    cf=CFile()
    cf.open_r("test/log1.txt")
#     cf.prn()
    while True:
        v=cf.getLine()
        print(v)
        if not v:
            break
#     pass

def test2():
    cf=CFile()
    cf.open_w("test/test.txt")
    cf.write("5 3 2 1")
    cf.write("15 1 2 4")
    cf.write("20 1 2 2")
    cf.end()

def test3():
    cf=CFile()
    cf.open_r("test/test.txt")
#     cf.prn()
    while True:
        v=cf.getWords()
        if not v:
            break
        print(v)
        sum=0
        for e in v:
            sum+=int(e)
        print(sum)

def test4():
    cf=CFile()
    cf.open_r("test/test.txt")
#     cf.prn()
    while True:
        v=cf.getInts()
        if not v:
            break
        print(v)
    
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
