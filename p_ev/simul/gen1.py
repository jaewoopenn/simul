'''
Created on 2015. 12. 11.

@author: cpslab
'''
from util.MFile import CFile


class gl:
    path=1
#     path=2
#     path=3
#     path=4

    
def test1():
    fn="ev/var/a_graph.txt"
    cf=CFile()
    cf.open_w(fn)
    cf.write("xx EDF FIFO")
    for i in range(10):
        cf.write(str(i*10)+" "+str(1-i*0.05)+" "+str(1-i*0.1))
    cf.end()
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
