'''
Created on 2015. 12. 11.

@author: cpslab
'''
import Util.MPlot as mp;
class gl:
    x=[0.55,0.60,0.65,0.70,0.75,0.80,0.85,0.90,0.95,1.0]
    vv=[]
    line=['r-','b--','g-.','k:']
#     marker=['o','v','D','^','s']
    lab=['sortMC_WF','sortMC_FF','sortHI_FF','sortLO_FF']
    data=[0,1,2,3]
#     cpus=2
    cpus=4
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
        fn="com/rs/mp_"+str(gl.cpus)+"_"+str(gl.data[i])+".txt"
        v=load(fn)
        gl.vv.append(v)

def main():
    iter(0,3)
    no=0
    for v in gl.vv:
        mp.plot2(gl.x,v,gl.line[no],gl.lab[no])
#         mp.plot3(gl.x,v,gl.line[no],gl.lab[no],gl.marker[no])
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
