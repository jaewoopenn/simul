'''
Created on 2013. 2. 12.

@author: cpslab
'''
import random
def pickU(a,b):
    return random.uniform(a,b)
def pickInt(a,b):
    return random.randint(a,b)
def computeUtil(tasks):
    u=0.0
    for t in tasks:
        u+=float(t[1])/t[0]
    return u    
def computeHiUtil(tasks):
    u=0.0
    for t in tasks:
        if t[3]==1:
            u+=float(t[2])/t[0]
    return u    
