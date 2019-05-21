'''
Draw Acceptance Ratio
Created on 2015. 12. 11.

@author: cpslab
'''
import util.MPlot as mp;
class gl_input:
    fn="t1/_graph.txt"
    path="/data/com"
    xlab= "Utilization Bound"
    ylab= "Overheads"

class gl:
    lab=[]
    x=[]
    vv=[]
    line=['r-','b--','m-.','g:','k--']
    marker=['o','v','D','^','s']

def load():
    fn=gl_input.path+"/"+gl_input.fn
    i_f = open(fn,"r")
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
    mp.ylim(0, 0.5)
#     mp.legendBL()
    mp.legendUL()
    mp.xlabel(gl_input.xlab)
    mp.ylabel(gl_input.ylab)
    mp.show()

if __name__ == '__main__':
    main()
