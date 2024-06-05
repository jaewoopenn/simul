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

class CDemand:
    vec=[]
    def prn(self):
        for e in self.vec:
            e.prn()
    def remove(self,t):
        tv=[]
        for e in self.vec:
            if e.t>t:
                tv.append(e)
        self.vec=tv
        
class CGap:
    vec=[]
    def after(self,t):
        tv=[]
        for e in self.vec:
            if e[0]>=t:
                tv.append(e)
        return tv
    def add_check(self,td):
        tv=self.after(td.t)
        for e in tv:
            if td.d>e[1]:
                return 0
        return 1  
    def compact(self):
        old_g=100
        tv=[]
        for e in self.vec:
            if e[1]<=old_g:
                tv=[]
                tv.append(e)
                old_g=e[1]
            if e[1]>old_g:
                tv.append(e)
        self.vec=tv

    def remove(self,t):
        tv=[]
        for e in self.vec:
            if e[0]>t:
                tv.append(e)
        self.vec=tv
        

        
def dem_add(dem,td):
    tv=[]
    old_d=0
    add_flag=0
    for e in dem.vec:
        if td.t>e.t:
            tv.append(e)
            old_d=e.d
        if td.t==e.t:
            dd=e.d+td.d
            tv.append(TD(td.t,dd))
            add_flag=1
            old_d=dd
        if td.t<e.t:
            if add_flag==0:
                tv.append(TD(td.t,old_d+td.d))
                add_flag=1
            tv.append(TD(e.t,e.d+td.d))
    if add_flag==0:
        tv.append(TD(td.t,old_d+td.d))
    dem.vec=tv

 

def gap_add(gap,td):
    tv=[]
    c=gap.add_check(td)
    if c==0:
        return 0
    add_flag=0
    old_d=0
    mod_d=td.t-td.d
    for e in gap.vec:
        mod=e[1]-td.d
        if e[0]<td.t:
            tv.append(e)
            old_d=e[0]-e[1]
        mod_d=td.t-td.d-old_d
        if e[0]>=td.t and mod!=0:
            if add_flag==0:
                tv.append((td.t,mod_d))
                add_flag=1
            tv.append((e[0],mod))
    if add_flag==0:
        tv.append((td.t,mod_d))
    gap.vec=tv
    return 1
            
