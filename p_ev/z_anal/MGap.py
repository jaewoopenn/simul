'''
Created on 2024. 5. 31.

@author: jaewoo
'''
class TD:
    t=0
    d=0
    def __init__(self,t,d):
        self.t=t
        self.d=d
    def prn(self):
        print(self.t,self.d)

# class CDemand:
#     vec=[]
#     def prn(self):
#         for e in self.vec:
#             e.prn()
#     def remove(self,t):
#         tv=[]
#         for e in self.vec:
#             if e.t>t:
#                 tv.append(e)
#         self.vec=tv
        
class CGap:
    vec=[]
    std=0 # used slack 
    def gap_after(self,t):
        self.vec=self.after(t)
    def after(self,t):
        tv=[]
        for e in self.vec:
            if e.t>t:
                tv.append(e)
        return tv
    def prune(self,t):
        if not self.vec:
            return
        last=self.vec[-1]
        if last.t-last.d<t:
            self.vec=[]
    def add_check(self,td):
        tv=self.after(td.t)
        for e in tv:
            if td.d>e.d:
                return 0
        return 1  
    def compact(self):
        old_g=self.vec[-1].d
        tv=[]
        for e in self.vec:
            if e.d<=old_g:
                tv.append(e)
        tv2=[]
        old_g=-1
        for e in tv:
            if e.d!=old_g:
                tv2.append(e)
            else:
                tv2.pop(-1)
                tv2.append(e)
            old_g=e.d
        self.vec=tv2

    def remove(self,t):
        tv=[]
        for e in self.vec:
            if e.t>t:
                tv.append(e)
        self.vec=tv
    def prn_vec(self):
        print(" [", end="")
        for e in self.vec:
            print("(",e.t,e.d,")", end="")
        print("]")



def gap_add(gap,td,t):
    tv=[]
    c=gap.add_check(td)
    if c==0:
        return 0
    add_flag=0
    old_g=t
    for e in gap.vec:
        mod=e.d-td.d
        if td.t>e.t:
#             print("case 1")
            tv.append(e)
            old_g=e.t-e.d
        if td.t==e.t:
#             print("case 2")
            if mod<0:
                return 0
            tv.append(TD(e.t,mod))
            old_g=mod
            add_flag=1
        if td.t<e.t:
#             print("case 3")
            if add_flag==0:
                mod_g=td.t-td.d-old_g
                tv.append(TD(td.t,mod_g))
                add_flag=1
            if mod<0:
                return 0
            tv.append(TD(e.t,mod))
            
    if add_flag==0:
        mod_g=td.t-td.d-old_g
        tv.append(TD(td.t,mod_g))

    for e in tv:
        if e.d<gap.std:
            return 0
    gap.vec=tv
    return 1
            
