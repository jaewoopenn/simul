'''
Created on 2015. 12. 11.

@author: cpslab
'''
import util.MPlot as mp
class gl:
    xl=[]
    vl=[]

def clear():
    gl.xl=[]
    gl.vl=[]

def add_p(x,y):
    gl.xl.append(x)
    gl.vl.append(y)

def add_d(x,y1,y2):
    gl.xl.append(x-0.01)
    gl.vl.append(y1)
    gl.xl.append(x)
    gl.vl.append(y2)

def add_s(x,y1,y2):
    inc=y2-y1
    gl.xl.append(x)
    gl.vl.append(y1)
    gl.xl.append(x+inc)
    gl.vl.append(y2)

def u_draw():
    add_p(0,0)
    add_d(4,0,2)
    add_d(5,2,3)
    add_d(8,3,5)
    add_d(10,5,6)
    add_p(11,6)
    
    

def main():
    u_draw()
    mp.plot(gl.xl,gl.vl)
    mp.ylim(0,11)
    mp.ylabel("Resources")
    mp.xlabel("Time")
    mp.show()
    

if __name__ == '__main__':
    main()
