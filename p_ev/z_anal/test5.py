'''
Created on 2015. 12. 11.

@author: cpslab


'''
from z_anal.MGap import TD,CGap
import z_anal.MGap as mg


class gl:
#     path=1
#     path=2
    path=3
#     path=4

def dem_add2(gap,td,t):
    ret=mg.gap_add(gap,td,t)
    if ret==0:
        print("error")
        return
    gap.compact()
    gap.prn_vec(t)

def test1():
    print("hihi")
    g=CGap()
    dem_add2(g,TD(13,2),0)
    dem_add2(g,TD(4,1),0)
    dem_add2(g,TD(10,2),0)
    dem_add2(g,TD(7,4),0)
'''
gap list update 
'''

def test2():
    g=CGap()
    dem_add2(g,TD(13,2),0)
#     dem_add2(g,TD(4,1),1)
#     dem_add2(g,TD(13,1),1)
    dem_add2(g,TD(15,1),1)
    dem_add2(g,TD(4,1),1)

def test3():
    g=CGap()
    dem_add2(g,TD(5,1),0)
#     dem_add2(g,TD(5,2),1)
    dem_add2(g,TD(10,2),1)
    dem_add2(g,TD(7,1),1)
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
