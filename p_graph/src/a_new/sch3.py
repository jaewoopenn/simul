'''
Draw DMR varying utilization

Created on 2015. 12. 11.

// MC-FLEX
@author: cpslab
'''
import util.MFile as mf
import util.MPlot as mp;
class gl_input:
    savename="sch/pms"
    path="sch/p"
    ylim=0.10
      
#     savename="sch/hc"
#     path="sch/h"
#     ylim=0.10

#     savename="sch/ratio"
#     path="sch/r"
#     ylim=0.10

    fn="a_sim_graph.txt"
    xlab= "Utilization Bound"
    ylab= "Deadline Miss Ratio"

class gl:
    lab=[]
    x=[]
    vv=[]
    line=['r-','b--','m-.','g:','k--']
    marker=['o','v','D','^','s']

def load(i):
    gl.lab=[]
    gl.x=[]
    gl.vv=[]
    
    i_f = mf.load(gl_input.path+str(i)+"/"+gl_input.fn)
    raw=[]
    for line in i_f:
        val=line.strip().split(" ")
        raw.append(val)
    itemlen=len(raw[0])
#     print(itemlen)

    for i in range(1,itemlen):
        gl.lab.append(raw[0][i])
    for i in range(1,len(raw)):
        gl.x.append(str(int(raw[i][0])/100))
#     print(gl.x)

    for i in range(1,itemlen):
        v=[]
        for j in range(1,len(raw)):
            v.append(float(raw[j][i]))
#         print(v)
        gl.vv.append(v)
        
        

def loop(i):
    load(i)
    no=0
    mp.prepare2()
    for v in gl.vv:
        mp.plot3(gl.x,v,gl.line[no],gl.lab[no],gl.marker[no])
        no+=1
#     mp.ylim(0, 1.02)
    mp.legendUL()
    mp.xlabel(gl_input.xlab)
    mp.ylabel(gl_input.ylab)
    mp.ylim(0,gl_input.ylim)
    mp.savefig(mf.filepath(gl_input.savename+str(i)+".pdf"))
    
def main():
    loop(0)
    loop(1)
    loop(2)

if __name__ == '__main__':
    main()
