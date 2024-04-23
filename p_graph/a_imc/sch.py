'''
Draw Acceptance Ratio //// MC-FLEX

@author: cpslab
'''
import util.MFile as mf
import util.MPlot as mp;
class gl_input:
    fn="run/mc1/a_graph.txt"
    xlab= "Utilization Bound"
    ylab= "Acceptance Ratio"

class gl:
    lab=[]
    x=[]
    vv=[]
    line=['r-','k:','b-.','g--','m-.','y-']
    marker=['o','v','D','^','s','o']

def load():
    i_f = mf.load(gl_input.fn)
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
#         gl.x.append(raw[i][0])
#     print(gl.x)

    for i in range(1,itemlen):
        v=[]
        for j in range(1,len(raw)):
            v.append(float(raw[j][i]))
#         print(v)
        gl.vv.append(v)
        
        

def main():
    mp.prepare3()
    load()
    no=0
    for v in gl.vv:
        print(v)
        mp.plot3(gl.x,v,gl.line[no],gl.lab[no],gl.marker[no])
        no+=1
#     mp.ylim(0, 1.02)
    mp.legendBL()
    mp.xlabel(gl_input.xlab)
    mp.ylabel(gl_input.ylab)
#     mp.show()
    mp.savefig(mf.filepath("run/sch.pdf"))

if __name__ == '__main__':
    main()
