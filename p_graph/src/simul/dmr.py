'''
Created on 2015. 12. 11.

@author: cpslab
'''
import Util.MPlot as mp;
class gl:
#     RS="util_1"
#     RS="util_4"
    RS="util_7"
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
        fn="exp/rs/"+gl.RS+"_"+str(gl.data[i])+".txt"
        v=load(fn)
        gl.vv.append(v)

def x_load():
    fn="exp/rs/"+gl.RS+"_x.txt"
    gl.x=load(fn)

def main():
    x_load()
    iter(0,2)
    no=0
    for v in gl.vv:
        mp.plot3(gl.x,v,gl.line[no],gl.lab[no],gl.marker[no])
        no+=1
    mp.xlim(0.55,1.0)
#     mp.ylim(0, 1.05)
    mp.ylim(0, 0.65)
    mp.legend3()
    mp.xlabel("Utilization Bound")
    mp.ylabel("Deadline Miss Ratio")
    print "hihi"
    mp.show()

if __name__ == '__main__':
    main()
