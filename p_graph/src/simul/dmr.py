'''
Created on 2015. 12. 11.

@author: cpslab
'''
import Util.MPlot as mp;
class gl:
    x=[]
    vv=[]
    line=['r-','b--','m-.','g:','k:']
    marker=['o','v','D','^','s']
    lab=['EDF-VD','EDF-AT-S','EDF','ICG',]
    data=[1,2]
#     lab=['EDF-ADAMS','EDF-AA-E(EDF-VD)','EDF-AA','EDF','ICG',]
#     data=[4,1,2,0,3]
def load(fn):
    i_f = open("C:/Users/jaewoo/data/"+fn,"r")
    v=[]
    for line in i_f:
        val=line.strip()
        v.append(float(val))
    return v

def iter(s,t):
    for i in range(s,t):
        fn="exp/rs/sim"+str(gl.data[i])+".txt"
        v=load(fn)
        gl.vv.append(v)

def xiter():
    fn="exp/rs/x.txt"
    gl.x=load(fn)

def main():
    xiter()
    iter(0,2)
    no=0
    for v in gl.vv:
        mp.plot3(gl.x,v,gl.line[no],gl.lab[no],gl.marker[no])
        no+=1
    mp.xlim(0.55,1.0)
#     mp.log()
    mp.ylim(0, 1.02)
    mp.legend()
    mp.xlabel("Utilization Bound")
    mp.ylabel("Acceptance Ratio")
    print "hihi"
    mp.show()

if __name__ == '__main__':
    main()
