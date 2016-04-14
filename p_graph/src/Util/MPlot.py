'''
Created on 2015. 12. 11.

@author: jaewo
'''
import matplotlib.pyplot as plt
import matplotlib
def legend():
    font= matplotlib.font_manager.FontProperties(weight="normal") 
#     plt.legend(loc='upper right',prop=font)
    plt.legend(loc='lower left',prop=font)
#    plt.legend(loc='upper left',prop=font)

def plot(x,v):
    plt.plot(x,v)

def plot2(x,v,l,la):
    plt.plot(x,v, l,drawstyle='default',label=la )

def xlim(l,u):
    plt.xlim(l,u)
    
def ylim(l,u):
    plt.ylim(l,u)
def show():
    plt.show()
def log():
    plt.yscale('log')