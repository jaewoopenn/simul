'''
Created on 2015. 12. 11.

@author: cpslab
'''
import util.MPlot as mp;
def main():
    x=[60,70,80,90,100]
    v1=[1,1,1,0.8,0.4]
    v2=[1,1,0.9,0.7,0.1]
    mp.plot(x,v1)
    mp.plot(x,v2)
    mp.ylim(0, 1.1)
    mp.show()
    

if __name__ == '__main__':
    main()
