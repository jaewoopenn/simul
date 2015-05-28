'''
Created on 2013. 2. 12.

@author: cpslab
'''
class ts:
    tasks=[]
    lo_tasks=[]
class Task:
    def __init__(self, t,c):
        self.t=t
        self.c=c
class mcTask:
    def __init__(self, t,c,ch,chi):
        self.t=t
        self.c=c
        self.ch=ch
        self.chi=chi


def insert(tsk):
    if isinstance(tsk,Task):
        ts.tasks.append(tsk)
        return
    if tsk.chi=='H':
        ts.tasks.append(tsk)
    else:
        ts.lo_tasks.append(tsk)

def size():
    return len(ts.tasks)    

def l_size():
    return len(ts.lo_tasks)    