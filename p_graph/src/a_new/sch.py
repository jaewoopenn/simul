'''
Created on 2015. 12. 11.

@author: cpslab
'''
import util.MPlot as mp;
class gl_input:
    path="/data/test/t1/a_graph.txt"
    lab=['EDF-AD','EDF-VD','ICG','EDF','x']
    xlab= "Utilization Bound"
    ylab= "Acceptance Ratio"
class gl:
    x=[]
    vv=[]
    line=['r-','b--','m-.','g:','k--']
    marker=['o','v','D','^','s']

def load():
    i_f = open(gl_input.path,"r")
    raw=[]
    for line in i_f:
        val=line.strip().split(" ")
        raw.append(val)
    itemlen=len(raw[0])
#     print(itemlen)
    for i in range(len(raw)):
        gl.x.append(raw[i][0])
#     print(gl.x)
    for i in range(1,itemlen):
        v=[]
        for j in range(len(raw)):
            v.append(float(raw[j][i]))
#         print(v)
        gl.vv.append(v)
        
        

def main():
    load()
    no=0
    for v in gl.vv:
        mp.plot3(gl.x,v,gl.line[no],gl_input.lab[no],gl.marker[no])
        no+=1
#     mp.ylim(0, 1.02)
    mp.legend()
    mp.xlabel(gl_input.xlab)
    mp.ylabel(gl_input.ylab)
    mp.show()

if __name__ == '__main__':
    main()
