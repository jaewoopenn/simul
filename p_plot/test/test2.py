'''
Created on 2015. 12. 11.

@author: cpslab
'''
import util.MPlot as mp;

if __name__ == '__main__':
    x1=[1,2,3,4]
    v1=[1,2,3,4]
    x2=[1,3,3.001,6]
    v2=[1,1,3,3]
    mp.plot(x1,v1);
    mp.plot(x2,v2);
    mp.show()
    
