'''
Created on 2015. 12. 11.

@author: cpslab
'''
import Util.MPlot as mp;
class gl:
    path="com/rs/"
#     path="final_rs/"
#     RS="util_sim_1"
    RS="util_sim_4"
#     RS="util_sim_7"
    x=[]
    vv=[]
    line=['r-','b--','m-.','g:','k:','k:']
    marker=['o','v','D','^','s','s']
    lab=['FC-MCS','Naive-Drop']
    data=[1,2,3,4,5]
def load(fn):
    i_f = open("C:/Users/jaewoo/data/"+fn,"r")
    v=[]
    for line in i_f:
        val=line.strip()
        v.append(float(val))
    return v
def x_load():
    fn=gl.path+gl.RS+"_x.txt"
    gl.x=load(fn)

def iterate(s,t):
    for i in range(s,t):
        fn=gl.path+gl.RS+"_"+str(gl.data[i])+".txt"
        v=load(fn)
        gl.vv.append(v)

def main():
    x_load()
    iterate(0,2)
    no=0
    for v in gl.vv:
        mp.plot3(gl.x,v,gl.line[no],gl.lab[no],gl.marker[no])
        no+=1
    mp.xlim(0.55,1.0)
    mp.ylim(0, 0.5)
#     mp.ylim(0, 1.02)
    mp.legend2()
    mp.xlabel("Utilization Bound")
    mp.ylabel("Deadline Miss Ratio")
    print "hihi"
    mp.savefig("/Users/jaewoo/data/fig/com_dmr.pdf")
    mp.show()

if __name__ == '__main__':
    main()
