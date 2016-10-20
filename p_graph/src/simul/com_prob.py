'''
Created on 2015. 12. 11.

@author: cpslab
'''
import Util.MPlot as mp;
class gl:
    path="com/rs/"
#     path="final_rs/"
    RS="prob"
    x=[]
    vv=[]
    line=['r-','b--','m-.','g:','k:','k:']
    marker=['o','v','D','^','s','s']
    lab=['a=0.0','0<a<=0.25','0.25<a<=0.5','0.5<a<=0.75','0.75<a<=1.00','a=1']
#     data=[1,2,0,3]
    data=[0,1,2,3,4,5]
#     lab=['EDF-ADAMS','EDF-AA-E(EDF-VD)','EDF-AA','EDF','ICG',]
#     data=[4,1,2,0,3]
def load(fn):
    i_f = open("C:/Users/jaewoo/data/"+fn,"r")
    v=[]
    for line in i_f:
        val=line.strip()
        v.append(float(val))
    return v
def x_load():
    fn=gl.path+gl.RS+"_x_x.txt"
    gl.x=load(fn)

def iterate(s,t):
    for i in range(s,t):
#         fn="com/rs/anal_util_"+str(gl.data[i])+".txt"
        fn=gl.path+gl.RS+"_"+str(gl.data[i])+".txt"
        v=load(fn)
        gl.vv.append(v)

def main():
    x_load()
    iterate(0,5)
    no=0
    for v in gl.vv:
        mp.plot3(gl.x,v,gl.line[no],gl.lab[no],gl.marker[no])
        no+=1
    mp.xlim(0.0,1.0)
#     mp.xlim(0.70,1.0)
    mp.ylim(0, 1.02)
    mp.legend()
    mp.xlabel("Probability to be a HI-task")
    mp.ylabel("Acceptance Ratio")
    print "hihi"
    mp.savefig("/Users/jaewoo/data/fig/com_prob.pdf")
    mp.show()

if __name__ == '__main__':
    main()
