'''
Created on 2015. 12. 11.

@author: cpslab
'''
import util.MPlot as mp;
from numpy.core.multiarray import empty
class gl:
    x=[]
    v1=[]
    v2=[]
def xload():
    i_f = open("C:/Users/jaewoo/data/graph.txt","r")
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
        if len(gl.v1)==0 :
            gl.v1=v
        else:
            gl.v2=v
#     print gl.x,gl.v1,gl.v2
def main():
    xload()
    mp.plot(gl.x,gl.v1)
    mp.plot(gl.x,gl.v2)
    mp.ylim(0, 1.1)
    mp.show()
    

if __name__ == '__main__':
    main()
