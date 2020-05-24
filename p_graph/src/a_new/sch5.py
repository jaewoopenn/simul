'''
Draw DMR

Created on 2015. 12. 11.

 DUR 
 
@author: cpslab
'''
import util.MFile as mf
import util.MPlot as mp;
class gl_input:
    path="sch_b/dur"
    fn="a_sim_graph.txt"
    xlab= "Duration (x1000)"
    ylab= "Deadline Miss Ratio"

class gl:
    lab=[]
    x=[]
    vv=[]
    line=['r-','b--','m-.','g:','k--']
    marker=['o','v','D','^','s']

def load():
    i_f = mf.load(gl_input.path+"/"+gl_input.fn)
    raw=[]
    for line in i_f:
        val=line.strip().split(" ")
        raw.append(val)
    itemlen=len(raw[0])
#     print(itemlen)

    for i in range(1,itemlen):
        gl.lab.append(raw[0][i])
    for i in range(1,len(raw)):
        gl.x.append(raw[i][0])
#     print(gl.x)

    for i in range(1,itemlen):
        v=[]
        for j in range(1,len(raw)):
            v.append(float(raw[j][i]))
#         print(v)
        gl.vv.append(v)
        
        

def main():
    load()
    no=0
    for v in gl.vv:
        mp.plot3(gl.x,v,gl.line[no],gl.lab[no],gl.marker[no])
        no+=1
#     mp.ylim(0, 1.02)
    mp.legendUR()
    mp.xlabel(gl_input.xlab)
    mp.ylabel(gl_input.ylab)
    mp.ylim(0,0.06)
    mp.show()

if __name__ == '__main__':
    main()
