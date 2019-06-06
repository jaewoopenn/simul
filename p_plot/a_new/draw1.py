'''
Created on 2015. 12. 17.

@author: jaewo
'''
import file.MFile as mf
import util.MPlot as mp

class gl_inp:
    fn="com/test_g.txt"
    
class gl:
    xl=[]
    vl=[]
    vl2=[]

def select():
    path1()
#     path2()

def path1():
    lst=mf.load(gl_inp.fn)
    lst2=[]
    temp=[]
    for m in lst:
        if m=="---":
            lst2.append(temp)
            temp=[]
        else:
            temp.append(m)
    for l in lst2:
        print(l)

def load(): 
    lst=mf.load(gl_inp.fn)
        
    for m in lst:
        wd=m.split()
        gl.xl.append(float(wd[0]))
        gl.vl.append(float(wd[1]))
       
def path2():
    load()
    mp.plot(gl.xl,gl.vl)  # sbf
#     mp.plot(gl.xl,gl.vl2)   # rbf
    mp.xlim(0,25)
    mp.ylim(0,13)
    mp.ylabel("Resources")
    mp.xlabel("Time")
    mp.show()
    


def main():
    select()

if __name__ == '__main__':
    main()