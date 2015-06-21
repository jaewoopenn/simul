'''
Created on 2013. 2. 12.

@author: cpslab
'''
from _heapq import heappush,heappop
import UtilLog as Log
'''
deadline, id, exec.time
'''
class js:
    jobs=[]
    rels=[]


def clear():
    js.jobs=[]
    js.rels=[]

def insert(id,et,dl):
#     js.jobs.append([id,et,dl])
    heappush(js.jobs,[dl,id,et])
    if dl not in js.rels:
        heappush(js.rels,dl)
def size():
    return len(js.jobs)    

def next_t(t):
    if not js.rels:
        return t+1
#     print "rels:",js.rels
    nt=heappop(js.rels)
    return nt
#     if t<js.jobs[0][0]:
#         return js.jobs[0][0]
#     else:
#         return t+1
def log_prog(t,cur_job):
    if not Log.get_l():
        return
    print "at time:",t,"job exec id:",cur_job[1],"rem.exec:",cur_job[2]

def progress(t,dur):
    Log.prnln(js.jobs)
    while True:
        cur_job=js.jobs[0]
        log_prog(t,cur_job)
        if dur>=cur_job[2]: # dur > exec
            Log.prnln("case 1")
            dur-=cur_job[2]
            t+=cur_job[2]
            heappop(js.jobs)
            if len(js.jobs)==0 or dur==0:
                break
        else:
            Log.prnln("case 2")
            cur_job[2]-=dur
            break
    Log.prnln(js.jobs)
#     exit()