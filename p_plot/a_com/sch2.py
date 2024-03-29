'''
Task level four graph (u...)
Created on 2015. 12. 11.

@author: cpslab
'''
import file.MFile as mf
import util.MPlot as mp;
class gl_inp:
    path="com/u"
    xlab= "Utilization Bound(%)"
    

#     fn="_graph.txt"
#     savename="com/u_res"
#     path="com/u"
#     ylim=0.80
#     ybase=0.2
#     ylab= "Util. of Res. Model"
    
    fn="_graph2.txt"
    savename="com/u_ov"
    path="com/u"
    ylim=0.015
    ybase=-0.005
    ylab= "Overheads"

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
    mp.ylim(gl_inp.ybase,gl_inp.ylim)
#     mp.legendBL()
    mp.legendUL()
    mp.xlabel(gl_inp.xlab)
    mp.ylabel(gl_inp.ylab)
    mp.savefig(mf.filepath(gl_inp.savename+str(i)+".pdf"))
#     mp.show()

def main():
    loop(0)
#     loop(1)
#     loop(2)
#     loop(3)
    print("OK")
    
if __name__ == '__main__':
    main()
