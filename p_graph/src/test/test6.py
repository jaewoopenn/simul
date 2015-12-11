'''
Created on 2015. 12. 11.

@author: cpslab
'''
import Util.MPlot as mp;
from numpy.core.multiarray import empty
class gl:
    x=[]
    vv=[]
def xload():
    i_f = open("C:/Users/jaewoo/data/graph3.txt","r")
    lines=[]
    for line in i_f:
        lines.append(line.strip())
    line=lines.pop()
    gl.x=line.split(",")
    for line in lines:
        v=[]
        words=line.split(",")
        for wd in words:
            v.append(float(wd))
        gl.vv.append(v)
#     print gl.x,len(gl.vv)
#     print gl.vv
def main():
    xload()
    for v in gl.vv:
        mp.plot(gl.x,v)
    mp.ylim(0, 1.1)
    mp.show()
    

if __name__ == '__main__':
    main()
