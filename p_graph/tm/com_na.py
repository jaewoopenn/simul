'''
Created on 2015. 12. 11.

@author: cpslab
'''
import util.MPlot as mp;
class gl:
    path="fc/rs/"
#     path="final_rs/"
    RS="util_X"
    x=[]
    vv=[]
    line=['r-','b--','m-.','g:','k:','k:']
    marker=['o','v','D','^','s','s']
    lab=['FC-MCS', 'Naive-Drop','MC-ADAPT']
#     data=[1,2,0,3]
#     data=['FC','NA']
    data=[5,1,2]
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
        fn=gl.path+"util_X_"+str(gl.data[i])+".txt"
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
    mp.ylim(0, 1.02)
    mp.legendBL()
    mp.xlabel("Utilization Bound")
    mp.ylabel("Acceptance Ratio")
    mp.savefig("/Users/jaewoo/data/fig/com_na.pdf")
    mp.show()

if __name__ == '__main__':
    main()
