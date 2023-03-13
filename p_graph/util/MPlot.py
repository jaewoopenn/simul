'''
Created on 2015. 12. 11.

@author: jaewo
'''
import matplotlib.pyplot as plt
import matplotlib
from matplotlib.backends.backend_pdf import PdfPages
matplotlib.rcParams.update({'font.size': 15})

def prepare():
    plt.figure(figsize=(8,5))

def prepare2():
    plt.figure(figsize=(7,7))

def prepare3():
    plt.figure(figsize=(9,6))
        
def legendBL():
    font= matplotlib.font_manager.FontProperties(weight="normal") 
    plt.legend(loc='lower left',prop=font)

def legendBR():
    font= matplotlib.font_manager.FontProperties(weight="normal") 
    plt.legend(loc='lower right',prop=font)
def legendUL():
    font= matplotlib.font_manager.FontProperties(weight="normal") 
    plt.legend(loc='upper left',prop=font)
def legendUR():
    font= matplotlib.font_manager.FontProperties(weight="normal") 
    plt.legend(loc='upper right',prop=font)
    
def plot(x,v):
    plt.plot(x,v)

def plot2(x,v,l,la):
    plt.plot(x,v, l, drawstyle='default',label=la )
def plot3(x,v,l,la,ma):
    plt.plot(x,v, l,marker=ma, drawstyle='default',label=la )

def xlim(l,u):
    plt.xlim(l,u)

def xlab_rot(size):
    plt.tick_params(axis="x", labelsize=size, labelrotation=-60)
#     plt.tick_params(axis="x", labelsize=14, labelrotation=-60, labelcolor="turquoise")
def xticks(x,xt):
    plt.xticks(x,xt)

def ylim(l,u):
    plt.ylim(l,u)
def show():
    plt.show()
def log():
    plt.yscale('log')
    plt.xscale('log')
def xlog():
    plt.xscale('log')
def ylabel(s):    
    plt.ylabel(s)
def xlabel(s):    
    plt.xlabel(s)
def savefig(fn):
    pp = PdfPages(fn)
    plt.savefig(pp, format='pdf')    
    pp.close()