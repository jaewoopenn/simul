'''
Created on 2015. 12. 11.

@author: cpslab
'''
import Util.MPlot as mp;
class gl:
    path="exp/rs/"
#     path="final_rs/"
    RS=""
    x=[]
    vv=[]
    line=['r-','b--','m-.','g:','k:']
    marker=['o','v','D','^','s']
    lab=['EDF-AD-E','EDF-VD','EDF','ICG',]
    data=[2,1]
#     lab=['EDF-ADAMS','EDF-AA-E(EDF-VD)','EDF-AA','EDF','ICG',]
#     data=[4,1,2,0,3]
def load(fn):
    i_f = open("C:/Users/jaewoo/data/"+fn,"r")
    v=[]
    for line in i_f:
        val=line.strip()
        v.append(float(val))
    return v

def iterate(s,t):
    for i in range(s,t):
        fn=gl.path+gl.RS+"_"+str(gl.data[i])+".txt"
        v=load(fn)
        gl.vv[i].append(v[0])

def x_load():
    gl.x=[1,2,3]
    gl.xticks=["10^3","10^4","10^5"]

def main():
    x_load()
    v=[]
    gl.vv.append(v)
    v=[]
    gl.vv.append(v)
    gl.RS="util_sim_D3"
    iterate(0,2)
    gl.RS="util_sim_D4"
    iterate(0,2)
    gl.RS="util_sim_D5"
    iterate(0,2)
    print gl.vv
#     return
    no=0
    mp.prepare()
    for v in gl.vv:
        mp.plot3(gl.x,v,gl.line[no],gl.lab[no],gl.marker[no])
        no+=1
#     mp.xlim(0.5501,1.0)
#     mp.ylim(0, 1.05)
    mp.ylim(-0.001, 0.65)
    mp.legendUL()
    mp.xlabel("Utilization Bound")
    mp.ylabel("Deadline Miss Ratio")
    print "hihi"
#     mp.savefig("/Users/jaewoo/data/fig/"+gl.RS+".pdf")
    mp.xticks(gl.x,gl.xticks)
    mp.show()


if __name__ == '__main__':
    main()
