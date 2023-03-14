'''
Created on 2015. 12. 11.

@author: cpslab
'''
import util.MFile as mf
import util.MPlot as mp;
class gl:
#     path="com/rs/"
    path="fc/rs/"
#     RS="util_sim_1"
    RS="util_sim_4"
#     RS="util_sim_7"
    x=[]
    vv=[]
    line=['r-','g--','b:','k-.','m-','b:']
    marker=['o','s','D','^','v','o']
    lab=['EDF-VD','FC-MCS_v1','FC-MCS_v2']
    data=[2,1,0,3,4,5]
def load(fn):
    i_f = mf.load(fn)
    v=[]
    for line in i_f:
        val=line.strip()
        v.append(float(val))
    return v
def x_load():
    fn=gl.path+gl.RS+".txt"
    gl.x=load(fn)

def iterate(s,t):
    for i in range(s,t):
        fn=gl.path+gl.RS+"_"+str(gl.data[i])+".txt"
        print(fn)
        v=load(fn)
        gl.vv.append(v)

def main():
    mp.prepare3()
    x_load()
    iterate(0,3)
    no=0
    for v in gl.vv:
        mp.plot3(gl.x,v,gl.line[no],gl.lab[no],gl.marker[no])
        no+=1
    mp.xlim(0.55,1.00)
    mp.ylim(0, 0.35)
#     mp.ylim(0, 1.02)
    mp.legendUL()
    mp.xlabel("Utilization Bound")
    mp.ylabel("Deadline Miss Ratio")
    mp.savefig(mf.filepath("fc/fig/com_dmr.pdf"))
#     mp.show()

if __name__ == '__main__':
    main()
