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

def legend2():
    font= matplotlib.font_manager.FontProperties(weight="normal") 
    plt.legend(loc='lower right',prop=font)
def legend3():
    font= matplotlib.font_manager.FontProperties(weight="normal") 
    plt.legend(loc='upper left',prop=font)
def plot(x,v):
    plt.plot(x,v)

def plot2(x,v,l,la):
    plt.plot(x,v, l, drawstyle='default',label=la )
def plot3(x,v,l,la,ma):
    plt.plot(x,v, l,marker=ma, drawstyle='default',label=la )

def xlim(l,u):
    plt.xlim(l,u)
    
def ylim(l,u):
    plt.ylim(l,u)
def show():
    plt.show()
def log():
    plt.yscale('log')
    plt.xscale('log')
def ylabel(s):    
    plt.ylabel(s)
def xlabel(s):    
    plt.xlabel(s)
    