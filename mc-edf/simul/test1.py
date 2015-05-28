'''
Created on 2015. 5. 26.

@author: Jaewoo Lee
'''
import util.TaskMng as utsk
import util.JobMng as ujob
from util.TaskMng import ts
from util.TaskMng import Task
from util.JobMng import js

class lc:
    plst=[]
    plen=0

def init():
    tsk=Task(4,1)
    utsk.insert(tsk)
    tsk=Task(3,1)
    utsk.insert(tsk)

def prepare():
    for tsk in ts.tasks:
        lc.plst.append(tsk.t)
    lc.plen=len(lc.plst)
    print "Periods:",lc.plst

def rel(t):
    for idx in range(lc.plen):
        if t % lc.plst[idx] ==0:
            tsk=ts.tasks[idx]
#             print "rel",idx,tsk.c,t+tsk.t
            ujob.insert(idx,tsk.c,t+tsk.t)
    
def loop(t):
    print t
    rel(t)

def simul():
    t=0
    while t<12:
        loop(t)
        t+=1
    print js.jobs


def simul_for():
    for t in range(12):
        loop(t)
    print js.jobs

def main():
    init()
    prepare()
    simul()
    #test()
    print "end"

def test():
    print utsk.size()

if __name__ == "__main__":
    main()
