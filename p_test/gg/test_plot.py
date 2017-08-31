'''
Created on 2015. 12. 11.

@author: cpslab
'''
import matplotlib.pyplot as plt
import matplotlib

if __name__ == '__main__':
    x=[1,2,3,4]
    v1=[3,4,2,4]
    v2=[4,3,2,1]
    plt.plot(x,v1)
    plt.plot(x,v2)
    plt.ylim(0,5)
    print "hihi"
    plt.show()