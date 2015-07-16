'''
Created on 2015. 5. 30.

@author: Jaewoo
'''
import util.TaskMng as utsk
import util.JobMng as ujob
import util.UtilLog as ulog
class lc:
    plst=[]
    plen=0

def clear():
    plst=[]
    plen=0

'''

'''
def prepare(task_list):
    utsk.init(task_list)
    lc.plst=utsk.getPeriods()
    lc.plen=len(lc.plst)
    ulog.prn("Periods:",lc.plst)

'''

'''
def rel(t):
    for idx in range(lc.plen):
        if t % lc.plst[idx] ==0:
            tsk=utsk.get(idx)
#             print "rel",idx,tsk.c,t+tsk.t
            ujob.insert(idx,tsk[1],t+tsk[0])
    
'''

'''
def loop(t):
    ulog.prn("t:",t)
    rel(t)

'''

'''
def run():
    t=0
    while t<12:
        loop(t)
        nt=ujob.next_t(t)
        dur=nt-t 
#         print dur
        ujob.progress(t,dur)
        t=nt
#     ujob.prn()

    
