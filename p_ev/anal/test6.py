'''
Created on 2015. 12. 11.

@author: cpslab


'''
from anal.MGap import TD,CGap
import anal.MGap as mg


class gl:
#     path=1
    path=2
#     path=3
#     path=4

def dem_add2(gap,td,t):
    ret=mg.gap_add(gap,td,t)
    if ret==0:
        print("error")
        return
    gap.prn_vec()
    gap.compact()
    gap.prn_vec()

def test1():
    g=CGap()
    g.vec=[TD(22,0),TD(30,5),TD(37,10)]
    g.prn_vec()
    dem_add2(g,TD(36,3),0)
'''
..
'''

def test2():
    g=CGap()
    g.vec=[TD(22,0),TD(30,5),TD(37,10)]
    g.prn_vec()
    dem_add2(g,TD(35,3),0)

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
