'''
Created on 2015. 12. 11.

@author: cpslab
'''
import Util.MPlot as mp
def func(x):
    return x*(pow(2,(1.0/x))-1)
def x_prepare():
    xl=[]
    for i in range(11):
        xl.append(pow(2,i))
    return xl
def main():
    xl=x_prepare()
    vl=[]
    for x in xl:
        vl.append(func(x)) 
#     print xl
#     print vl
    mp.plot(xl,vl)
    mp.xlog()
    mp.xlim(0, 1024)
    mp.xlabel("The number of tasks")
    mp.ylabel("Utilization")
    mp.show()
    

if __name__ == '__main__':
    main()
