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

def progress(t,dur):
    Log.prnln(js.jobs)
    cur_job=js.jobs[0]
    while True:
        if dur>cur_job[2]:
            Log.prn("at time:",t)
            Log.prn("job exec id:",cur_job[1])
            Log.prn(" exec:",cur_job[2])
            dur-=cur_job[2]
            t+=cur_job[2]
            cur_job[2]=0
            heappop(js.jobs)
            if len(js.jobs)==0:
                break
            cur_job=js.jobs[0]
        else:
#             print "dur2",dur
            Log.prn("at time:",t)
            Log.prn("job exec id:",cur_job[1])
            Log.prn(" exec:",cur_job[2])
            cur_job[2]-=dur
            if cur_job[2]==0:
                heappop(js.jobs)
            break
#     print js.jobs
#     exit()