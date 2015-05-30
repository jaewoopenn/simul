'''
Created on 2015. 5. 30.

@author: Jaewoo
'''
import util.TaskMng as utsk
import util.JobMng as ujob
from util.TaskMng import ts
from util.TaskMng import Task
from util.JobMng import js
class lc:
    plst=[]
    plen=0


def prepare(task_list):
    utsk.init(task_list)

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
    print "t:",t
    rel(t)

def run():
    t=0
    while t<12:
        loop(t)
        nt=ujob.next_t(t)
        dur=nt-t 
        ujob.progress(t,dur)
        t=nt
    print js.jobs

    
'''
backup

def simul_backup():
    for t in range(12):
        loop(t)
    print js.jobs

def init_backup():
    tsk=Task(4,2)
    utsk.insert(tsk)
    tsk=Task(3,2)
    utsk.insert(tsk)
'''
