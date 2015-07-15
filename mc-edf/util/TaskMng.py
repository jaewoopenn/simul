'''
Created on 2013. 2. 12.

@author: cpslab
'''
import numpy as np
class gl:
    tasks=[]

def clear():
    gl.tasks=[]
def init(tlist):
    gl.tasks=np.array(tlist)

def size():
#     print gl.tasks
    return len(gl.tasks)    

def get(i):
    return gl.tasks[i]
def getPeriods():
    plst=[]
    for tsk in gl.tasks:
        plst.append(tsk[0])
    return plst
    