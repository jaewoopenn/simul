'''
Created on 2015. 12. 17.

@author: jaewo
'''
import util.MPlot as mp;

def main():
    x=load("graph/label.txt")
    for i in range(2):
        v=load("graph/test"+str(i+1)+".txt")
        mp.plot(x,v)
    print "hihi"
    mp.ylim(0,1.1)
    mp.show()

def load(fn):
    i_f = open("C:/Users/jaewoo/data/"+fn,"r")
    v=[]
    for line in i_f:
        val=line.strip()
        v.append(float(val))
    return v

if __name__ == '__main__':
    main()