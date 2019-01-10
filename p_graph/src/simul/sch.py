'''
Created on 2015. 12. 11.

@author: cpslab
'''
import util.MPlot as mp;
class gl:
#     path="exp/rs/"
    path="final_rs/"
    
    x=[0.55,0.60,0.65,0.70,0.75,0.80,0.85,0.90,0.95,1.0]
    vv=[]
    line=['r-','b--','m-.','g:','k--']
    marker=['o','v','D','^','s']
    lab=['EDF-AD-E ','EDF-VD','EDF-AD','ICG','EDF']
#     data=[1,2,0,3]
    data=[1,2,3,4,5]
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
        fn=gl.path+"util_X_"+str(gl.data[i])+".txt"
        v=load(fn)
        gl.vv.append(v)

def main():
    iterate(0,5)
    no=0
    mp.prepare()
    
    for v in gl.vv:
        mp.plot3(gl.x,v,gl.line[no],gl.lab[no],gl.marker[no])
        no+=1
    mp.xlim(0.5501,1.0)
#     mp.log()
    mp.ylim(0, 1.02)
    mp.legend()
    mp.xlabel("Utilization Bound")
    mp.ylabel("Acceptance Ratio")
    print "hihi"
    mp.savefig("/Users/jaewoo/data/fig/adams_sch.pdf")
    mp.show()

if __name__ == '__main__':
    main()
