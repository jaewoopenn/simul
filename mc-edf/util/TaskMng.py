'''
Created on 2013. 2. 12.

@author: cpslab
'''
class ts:
    tasks=[]

def clear():
    ts.tasks=[]
def init(tlist):
    for tsk in tlist:
        tsk1=(tsk[0],tsk[1])
        insert(tsk1)

def insert(tsk):
    ts.tasks.append(tsk)

def size():
    return len(ts.tasks)    

def get(i):
    return ts.tasks[i]