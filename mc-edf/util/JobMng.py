'''
Created on 2013. 2. 12.

@author: cpslab
'''
from _heapq import heappush

class js:
    jobs=[]


def insert(id,et,dl):
#     js.jobs.append([id,et,dl])
    heappush(js.jobs,[dl,id,et])

def size():
    return len(js.jobs)    

