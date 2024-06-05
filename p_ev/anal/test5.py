'''
Created on 2015. 12. 11.

@author: cpslab


'''
from anal.MGap import TD,CDemand,CGap
import anal.MGap as mg


class gl:
    path=1
#     path=2
#     path=3
#     path=4

def dem_add2(dem,gap,td):
    ret=mg.gap_add(gap,td)
    gap.compact()
    print(gap.vec)    
    if ret:
        mg.dem_add(dem,td)
    else:
        print("error")

def test1():
    print("hihi")
    v= CDemand()
    g=CGap()
    dem_add2(v,g,TD(13,2))
    dem_add2(v,g,TD(4,1))
    dem_add2(v,g,TD(10,2))
    dem_add2(v,g,TD(7,4))
    v.prn()
    v.remove(8)
    g.remove(8)
    print(g.vec)
    v.prn()

'''
gap list update 
'''

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
