'''
Created on 2013. 4. 3.

@author: cpslab
'''
import matplotlib.pyplot as plt
import matplotlib
def plot(x,v,line,lab):
    plt.plot(x,v, line,drawstyle='default',label=lab )
def legend():
    font= matplotlib.font_manager.FontProperties(weight="normal") 
#     plt.legend(loc='upper right',prop=font)
    plt.legend(loc='lower left',prop=font)
#    plt.legend(loc='upper left',prop=font)
def show():
    plt.show()

