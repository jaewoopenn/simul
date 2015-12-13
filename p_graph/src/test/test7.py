'''
Created on 2015. 12. 11.

@author: cpslab
'''
import Util.MPlot as mp;
from numpy.core.multiarray import empty
class gl:
    x=[55,60,65,70,75,80,85,90,95,100]
    vv=[]
    pass

def load(fn):
    i_f = open("C:/Users/jaewoo/data/"+fn,"r")
    v=[]
    for line in i_f:
        val=line.strip()
        v.append(float(val))
    return v

def iter(n):
    for i in range(n):
        fn="rs/sim"+str(i)+".txt"
        v=load(fn)
        gl.vv.append(v)

def main():
    iter(2)
    for v in gl.vv:
        mp.plot(gl.x,v)
    mp.xlim(55,100)
    mp.ylim(0, 1.1)
    print "hihi"
    mp.show()

if __name__ == '__main__':
    main()
