'''
Created on 2015. 12. 11.

@author: jaewo
'''
import matplotlib.pyplot as plt
import matplotlib

def plot(x,v):
    plt.plot(x,v)

def xlim(l,u):
    plt.xlim(l,u)
    
def ylim(l,u):
    plt.ylim(l,u)
def show():
    plt.show()
def log():
    plt.yscale('log')