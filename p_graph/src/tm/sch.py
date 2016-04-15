'''
Created on 2015. 12. 11.

@author: cpslab
'''
import Util.MPlot as mp;
class gl:
    x=[55,60,65,70,75,80,85,90,95,100]
    vv=[]
    line=['k:', 'm-.', 'b--', 'r-']
    lab=['EDF','EDF-VD','EDF-TM','EDF-TM-S']

def load(fn):
    i_f = open("C:/Users/jaewoo/data/"+fn,"r")
    v=[]
    for line in i_f:
        val=line.strip()
        v.append(float(val))
    return v

def iter(s,t):
    for i in range(s,t):
        fn="tm/rs/sim"+str(i)+".txt"
        v=load(fn)
        gl.vv.append(v)

def main():
    iter(0,4)
    no=0
    for v in gl.vv:
        mp.plot2(gl.x,v,gl.line[no],gl.lab[no])
        no+=1
    mp.xlim(50,100)
#     mp.log()
    mp.ylim(0, 1.02)
    mp.legend()
    print "hihi"
    mp.show()

if __name__ == '__main__':
    main()
