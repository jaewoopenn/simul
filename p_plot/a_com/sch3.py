'''
Sys level four graph (c..)

Created on 2015. 12. 11.

@author: cpslab
'''
import file.MFile as mf
import util.MPlot as mp;
class gl_inp:
    path="com/c"
    xlab= "Utilization Bound(%)"

    fn="_graph.txt"
    savename="com/csf"
    path="com/c"
    ylim=1.01
    ylab= "Acceptance Ratio"
    

class gl:
    lab=[]
    x=[]
    vv=[]
    line=['r:','m--','b-','g-.','k--']
    marker=['s','x','o','s','D']

def load(i):
    gl.lab=[]
    gl.x=[]
    gl.vv=[]

    i_f=mf.load(gl_inp.path+str(i)+"/"+gl_inp.fn)
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
        
        
def loop(i):
    load(i)
    no=0
    mp.prepare2()
    for v in gl.vv:
        mp.plot3(gl.x,v,gl.line[no],gl.lab[no],gl.marker[no])
        no+=1
    mp.ylim(0,gl_inp.ylim)
    mp.legendBL()
#     mp.legendUL()
#     mp.legendUR()
    mp.xlabel(gl_inp.xlab)
    mp.ylabel(gl_inp.ylab)
    mp.savefig(mf.filepath(gl_inp.savename+str(i)+".pdf"))
#     mp.show()
def main():
    loop(0)
    loop(1)
    loop(2)
    print("OK")

if __name__ == '__main__':
    main()
