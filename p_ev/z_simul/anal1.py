'''
Created on 2015. 12. 11.

@author: cpslab
'''
from util.MFile import CFile

import z_simul.MSimulF as msf
import z_simul.MSimul as ms

class gl:
#     path=1
    path=2
#     path=3
#     path=4
    prefix="data"

def run_one(idx,i,algo):
    fn="ev/var/"+gl.prefix+str(idx)+"-"+str(i)+".txt"
    print(fn)
    ret=0
    if algo==1:
        ret=ms.run_edf(fn)
        return ret[0]
    else:
        ret=msf.run_fifo(fn)
        return ret[0]    
def run(idx):
    sum1=0
    sum2=0
    for i in range(10):
        ret=run_one(idx,i,1)
        sum1+=ret
        ret=run_one(idx,i,2)
        sum2+=ret
    return sum1/10,sum2/10

def test1():
    fn="ev/var/a_graph.txt"
    cf=CFile()
    cf.open_w(fn)
    cf.write("xx EDF FIFO")
    for i in range(10):
        cf.write(str(i*10)+" "+str(1-i*0.05)+" "+str(1-i*0.1))
    cf.end()
def test2():
    fn="ev/var/"+gl.prefix+"_gra.txt"
    cf=CFile()
    cf.open_w(fn)
    cf.write("xx EDF FIFO")
    
    for i in range(1,10):
        ret=run(i)
        cf.write(str(i*10)+" "+str(ret[0])+" "+str(ret[1]))
    cf.end()

def test3():
    pass

def test4():
    pass
    
def main():
    if gl.path==1:
        test1()
    elif gl.path==2:
        test2()
    elif gl.path==3:
        test3()
    elif gl.path==4:
        test4()

if __name__ == '__main__':
    main()
