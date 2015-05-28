'''
Created on 2015. 5. 26.

@author: Jaewoo Lee
'''
import util.TaskMng as utsk
from util.TaskMng import gl
from util.TaskMng import Task
class lc:
    plst=[]
    plen=0
def init():
    tsk=Task(3,1)
    utsk.insert(tsk)
    tsk=Task(4,1)
    utsk.insert(tsk)

def prepare():
    for tsk in gl.tasks:
        lc.plst.append(tsk.t)
    lc.plen=len(lc.plst)
    print lc.plst

def loop(t):
    print t
    for idx in range(lc.plen):
        if t % lc.plst[idx] ==0:
            print "rel",idx

def simul():
    
    for t in range(12):
        loop(t)
        
def main():
    init()
    prepare()
    simul()
    #test()
    print "end"

def test():
    print gl.tasks[0].t
    print utsk.size()

if __name__ == "__main__":
    main()
