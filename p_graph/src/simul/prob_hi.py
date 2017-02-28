'''
Created on 2015. 12. 11.

@author: cpslab
'''
import Util.MPlot as mp;
class gl:
#     path="exp/rs/"
    path="final_rs/"
    RS="prob_sim_4"
    x=[]
    vv=[]
    line=['r-','b--','m-.','g:','k:']
    marker=['o','v','D','^','s']
    lab=['EDF-AD-E','EDF-VD','EDF','ICG',]
    data=[1,2]
#     lab=['EDF-ADAMS','EDF-AA-E(EDF-VD)','EDF-AA','EDF','ICG',]
#     data=[4,1,2,0,3]
def load(fn):
    i_f = open("/Users/jaewoo/data/"+fn,"r")
    v=[]
    for line in i_f:
        val=line.strip()
        v.append(float(val))
    return v

def iter(s,t):
    for i in range(s,t):
        fn=gl.path+gl.RS+"_"+str(gl.data[i])+".txt"
        v=load(fn)
        gl.vv.append(v)

def x_load():
    fn=gl.path+gl.RS+"_x.txt"
    gl.x=load(fn)

def main():
    x_load()
    iter(1,2)
    iter(0,1)
    no=0
    mp.prepare()

    for v in gl.vv:
        mp.plot3(gl.x,v,gl.line[no],gl.lab[no],gl.marker[no])
        no+=1
    mp.xlim(0.0501,0.95)
    mp.ylim(-0.001, 1.05)
#     mp.ylim(0, 0.65)
    mp.legendUL()
    mp.xlabel("Probability to be a HI-task")
    mp.ylabel("Deadline Miss Ratio")
    print "hihi"
    mp.savefig("/Users/jaewoo/data/fig/"+gl.RS+".pdf")
    mp.show()

if __name__ == '__main__':
    main()
