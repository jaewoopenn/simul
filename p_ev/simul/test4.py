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
    v= CDemand()
    g=CGap()
    
    cl=CLog("ev/test8.txt")
    
    t=0
    end_t=20
    g.std=0
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
..
'''

def test2():
    pass

'''
...
'''

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
