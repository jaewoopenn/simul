'''
Created on 2015. 12. 11.

@author: cpslab
'''
import util.MFile as mf

import util.MPlot as mp;
class gl:
    path="fc/rs/"
#     path="final_rs/"
    RS="util_sch"
    x=[]
    vv=[]
    line=['r-','g--','b:','k-.','m-','b:']
    marker=['o','s','D','^','v','o']
    lab=['FC-MCS', 'MC-ADAPT','EDF-VD']
    data=['FC','IS','VD']
def load(fn):
    i_f = mf.load(fn)
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
        fn=gl.path+"util_"+str(gl.data[i])+".txt"
        v=load(fn)
        gl.vv.append(v)

def main():
    x_load()
    iterate(0,3)
    no=0
    mp.prepare3()
    for v in gl.vv:
        mp.plot3(gl.x,v,gl.line[no],gl.lab[no],gl.marker[no])
        no+=1
    mp.xlim(0.55,1.0)
    mp.ylim(0, 1.02)
    mp.legendBL()
    mp.xlabel("Utilization Bound")
    mp.ylabel("Acceptance Ratio")
#     mp.savefig(mf.filepath("fc/fig/com_sch.pdf"))
    mp.show()

if __name__ == '__main__':
    main()
