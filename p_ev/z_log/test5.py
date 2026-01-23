'''
Created on 2015. 12. 11.

@author: cpslab
'''

from util.MFile import CFile
from z_log.MLog import CLog
from z_log.MConfig import CConfig
import util.MRand as mr

class gl:
    path=1
#     path=2
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
    for i in range(1,10):
        c=CConfig()
        c.init_r("ev/var/config"+str(i)+".txt")
        w=c.get('num')
        print(w)
        w=c.get('prob')
        print(w)


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
