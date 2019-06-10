'''
Created on 2015. 12. 17.

@author: jaewo
'''
import file.MFile as mf
import util.MPlot as mp

class gl_inp:
#     fn="com/test_g.txt"
    fn="com/cfg1_copy.txt"    
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
    print(lst)
def load(): 
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
        temp=[]
        temp2=[]
        for m in l:
            wd=m.split()
            temp.append(float(wd[0]))
            temp2.append(float(wd[1]))
        gl.xl.append(temp)
        gl.vl.append(temp2)
       
def path2():
    load()
    for i in range(0,len(gl.xl)):
#         print(gl.xl[i])
#         print(gl.vl[i])
        mp.plot(gl.xl[i],gl.vl[i]) 
    mp.xlim(0,25)
    mp.ylim(0,13)
    mp.ylabel("Resources")
    mp.xlabel("Time")
    mp.show()
    


def main():
    select()

if __name__ == '__main__':
    main()