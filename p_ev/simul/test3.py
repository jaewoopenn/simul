'''
Created on 2015. 12. 11.

@author: cpslab
'''

from log.MLog import CLog
from anal.MQueue import CQueue
from anal.MGap import TD,CDemand,CGap
import anal.MGap as mg

class gl:
#     path=1
    path=2
#     path=3
#     path=4


def dem_add2(dem,gap,td,t):
    gap.prune(t)
    ret=mg.gap_add(gap,td,t)
    gap.compact()
    if ret:
        mg.dem_add(dem,td)
    else:
        print("error")
            
def test1(): 
    cl=CLog("ev/test.txt")
    
    t=0
    end_t=10
    while t<end_t:
        print(t, end =" ")
        while t==cl.getLast():
            w=cl.getW()
            print(w, end =" ")
        print("")
        t+=1

'''
edf 
'''

def test2():
    v= CDemand()
    g=CGap()
    
#     cl=CLog("ev/test2.txt")
#     cl=CLog("ev/test3.txt")
#     cl=CLog("ev/test4.txt")
    cl=CLog("ev/test5.txt")
    
    t=0
    end_t=20
    while t<end_t:
        g.vec=g.after(t)
        while t==cl.getLast():
            w=cl.getW()
            dem_add2(v,g,TD(t+w[1],w[2]),t)
#         if t>=2 and t<4:
#             g.consume()
        print(t, g.vec)    
        t+=1
    print(g.vec)
    v.prn()


'''
two process
'''

def test3():
    
    cl=CLog("ev/test2.txt")
    q=CQueue()
    t=0
    end_t=15
    while t<end_t:
        while t==cl.getLast():
            w=cl.getW()
            q.add([t+w[1],w[2]])
        q.prn(t)
        t+=1
        q.proceed()

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
