'''
Created on 2013. 2. 12.

@author: cpslab
'''
from types import InstanceType
class gl:
    tasks=[]
    lo_tasks=[]
class Task:
    def __init__(self, t,cl):
        self.t=t
        self.cl=cl
class mcTask:
    def __init__(self, t,cl,ch,chi):
        self.t=t
        self.cl=cl
        self.ch=ch
        self.chi=chi


def insert(tsk):
    if isinstance(tsk,Task):
        gl.tasks.append(tsk)
        return
    if tsk.chi=='H':
        gl.tasks.append(tsk)
    else:
        gl.lo_tasks.append(tsk)

def size():
    return len(gl.tasks)    

def l_size():
    return len(gl.lo_tasks)    