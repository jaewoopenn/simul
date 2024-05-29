'''
Created on 2015. 12. 11.

@author: cpslab
'''
import util.MRand as mr

class gl:
    path=2

# vector 
# item (t,rd,od)
class Job:
    t=0
    d=0
    o=0
    def __init__(self, t, d, o):
        self.t = t 
        self.d= d
        self.o  = o    
    
class Demand:
    vec=[]
    cur_t=0
    max_s=0
    max_td=(0,0)
    def proceed(self):
        self.cur_t+=1
        
    def add(self,j):
        tv=[]
        old_d=0
        add_flag=0
        if len(self.vec)==0:
            self.vec.append((j.t,j.d))
            return
        print(len(self.vec))
        for e in self.vec:
            if j.t<e[0]:
                if add_flag==0:
                    tv.append((j.t,old_d+j.d))
                    tv.append((e[0],e[1]+j.d))
                    add_flag=1
                else:
                    tv.append((e[0],e[1]+j.d))
            if j.t==e[0]:
                tv.append((j.t,e[0]+j.d))
                old_d=e[1]+j.d
            if j.t>e[0]:
                tv.append(e)
                old_d=e[1]
        if add_flag==0:
            tv.append((j.t,old_d+j.d))
            
        self.vec=tv
    def sort(self):
        self.vec.sort(key=None, reverse=False)
    def findMax(self):
        for e in self.vec:
            print(e)
            t=e[0]
            d=e[1]
            s=d/t
            if s>self.max_s:
                self.max_s=s
                self.max_td=(t,d)
            print(s)

    def printMax(self):
        
        print("max_s:",self.max_s)
        print("max_td:",self.max_td)
        
def test1():
    v= Demand()
    v.add((1,2))
    v.add((4,7))
    v.findMax()
    v.printMax()
    v.add((3,5))
    v.sort()
    v.findMax()
    v.printMax()
#     print(vec)

def test2():
    v= Demand()
    v.add(Job(2,1,0))
    v.add(Job(7,2,0))
    v.findMax()
    v.printMax()
    v.add(Job(5,1,0))
    v.findMax()
    v.printMax()
    pass

def test3():
    pass

def test4():
    pass
    
def main():
    if gl.path==1:
        test1()
    elif gl.path==2:
        test2()
    elif gl.path==3:
        test3()
    elif gl.path==4:
        test4()
    print("hihi")

if __name__ == '__main__':
    main()
