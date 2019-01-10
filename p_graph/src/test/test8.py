'''
Created on 2015. 12. 11.

@author: cpslab
'''
import util.MPlot as mp;
class gl:
    x=[30,35,40,45,50,55,60,65,70,75]
    vv=[]

def load(fn):
    i_f = open("C:/Users/jaewoo/data/"+fn,"r")
    v=[]
    for line in i_f:
        val=line.strip()
        v.append(float(val))
    return v

def iter(s,t):
    for i in range(s,t):
        fn="tm/rs/sim"+str(i)+".txt"
        v=load(fn)
        gl.vv.append(v)

def main():
    iter(0,3)
    for v in gl.vv:
        mp.plot(gl.x,v)
    mp.xlim(50,100)
    mp.log()
#     mp.ylim(0, 1.1)
    print "hihi"
    mp.show()

if __name__ == '__main__':
    main()
