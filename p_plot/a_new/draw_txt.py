'''
Draw Acceptance Ratio
Created on 2015. 12. 11.

@author: cpslab
'''
import file.MFile as mf
import util.MPlot as mp;
class gl_inp:
    fn="com/draw/compare.txt"
    path="/data/com"
    xlab= "Time"
#     ylab= "Util. of Res. Model"
#     ylab= "Overheads"
    ylab= "Resource"

class gl:
    lab=[]
    x=[]
    vv=[]
    line=['r-','b--','m-.','g:','k--']
    marker=['o','v','D','^','s']

def load():
    i_f=mf.load(gl_inp.fn)
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
    mp.ylim(-0.25,5)
#     mp.legendBL()
#     mp.legendUL()
    mp.legendUR()
    mp.xlabel(gl_inp.xlab)
    mp.ylabel(gl_inp.ylab)
    mp.show()

if __name__ == '__main__':
    main()
