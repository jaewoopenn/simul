'''
Created on 2024. 5. 31.

@author: jaewoo
'''
class CJob:
    t=0
    d=0
    o=0
    def __init__(self, t, d, o):
        self.t = t 
        self.d= d
        self.o  = o    
    
class CDemand:
    vec=[]
    cur_t=0
    max_s=0
    max_td=(0,0)

    def proceed(self):
        self.cur_t+=1
        
    def add(self,j):
        if len(self.vec)==0:
            self.vec.append((j.t,j.d))
            return
        tv=[]
        old_d=0
        add_flag=0
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