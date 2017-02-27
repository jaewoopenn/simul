'''
Created on 2015. 12. 11.

@author: cpslab
'''
import Util.MPlot as mp;
import math
class gl:
#     x=[0.01,0.03,0.05,0.07,0.09, 0.11,0.13,0.15,0.17,0.19]
    x=[]
    vv=[]
    line=['b--', 'r-']
    lab=['EDF-VD','EDF-ADAM-S']
#     is_Log=1
    is_Log=0
    
def load(fn):
    i_f = open("C:/Users/jaewoo/data/"+fn,"r")
    v=[]
    for line in i_f:
        val=line.strip()
        v.append(float(val))
    print v
    return v

def iter(s,t):
    for i in range(s,t):
#         fn="tm/rs/drop_80_"+str(i)+".txt"
        fn="tm/rs/drop_90_"+str(i)+".txt"
        v=load(fn)
        gl.vv.append(v)

def main():
    iter(6,8)
    no=0
    for i in range(0,len(gl.vv[0])):
        gl.x.append(0.1/math.pow(2,i))
    print gl.x
    for v in gl.vv:
        mp.plot2(gl.x,v,gl.line[no],gl.lab[no])
        no+=1
#     mp.xlim(50,100)
    if gl.is_Log:
        mp.log()
        mp.xlim(0,0.10)
#         mp.ylim(-0.01,0.65)
        mp.legendBR()
    else:
        mp.xlim(0,0.10)
        mp.ylim(-0.01,0.65)
        mp.legendUL()
    mp.xlabel("The probability to show HI-behavior")
    mp.ylabel("Expected Drop Ratio")
    mp.show()

if __name__ == '__main__':
    main()
