'''
Created on 2015. 12. 11.

@author: cpslab


'''



class gl:
#     path=1
#     path=2
#     path=3
    path=4

class TD:
    t=0
    d=0
    def __init__(self,t,d):
        self.t=t
        self.d=d
    def prn(self):
        print(self.t,self.d)

class Demand:
    vec=[]
    gap=[]
    def prn(self):
        for e in self.vec:
            e.prn()

def dem_add2(dem,td):
    ret=gap_add(dem,td)
    dem_gap_compact(dem)
    print(dem.gap)    
    if ret:
        dem_add(dem,td)
        
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

def dem_gap(dem):
    tv=[]
    for e in dem.vec:
        tv.append((e.t,e.t-e.d))
    dem.gap=tv
    
def dem_gap_compact(dem):
    old_g=100
    tv=[]
    for e in dem.gap:
        if e[1]<=old_g:
            tv=[]
            tv.append(e)
            old_g=e[1]
        if e[1]>old_g:
            tv.append(e)
    dem.gap=tv

def in_gap_after(v,t):
    tv=[]
    for e in v:
        if e[0]>=t:
            tv.append(e)
    return tv    
def in_add_check(v,d):
    for e in v:
        if d>e[1]:
            return 0
    return 1    
def gap_add(dem,td):
    tv=[]
    gap2=in_gap_after(dem.gap,td.t)
#     print(gap2)
    c=in_add_check(gap2,td.d)
    if c==0:
        return 0
    add_flag=0
    old_d=0
    mod_d=td.t-td.d
    for e in dem.gap:
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
    dem.gap=tv
    return 1
            

def test1():
    v= Demand()
    dem_add(v,TD(4,1))
    dem_add(v,TD(10,2))
    dem_add(v,TD(7,1))
    dem_add(v,TD(7,2))
    v.prn()
    gap=dem_gap(v)
    print(gap)

'''
gap list update 
'''

def test2():
    v= Demand()
    dem_add(v,TD(4,1))
    dem_add(v,TD(7,4))
    dem_add(v,TD(10,2))
    dem_add(v,TD(13,2))
    v.prn()
    dem_gap(v)
    dem_gap_compact(v)
    print(v.gap)
    add_t=TD(8,2)
#     add_t=TD(8,3)
#     add_t=TD(8,4)
    ret=gap_add(v,add_t)
    if ret:
        print("OK")
        dem_gap_compact(v)
    else:
        print("Not")
        
    print(v.gap)

def test3():
    v= Demand()
    dem_add(v,TD(4,1))
    dem_add(v,TD(7,4))
    dem_add(v,TD(10,2))
    dem_add(v,TD(13,2))
    v.prn()
    dem_gap(v)
    dem_gap_compact(v)
    print(v.gap)
#     add_t=TD(13,1)
    add_t=TD(15,1)
    ret=gap_add(v,add_t)
    if ret:
        print("OK")
    else:
        print("Not")
        
    print(v.gap)

def test4():
    v= Demand()
    dem_add2(v,TD(13,2))
    dem_add2(v,TD(4,1))
    dem_add2(v,TD(10,2))
    dem_add2(v,TD(7,4))
    v.prn()


    
def main():
    if gl.path==1:
        test1()
    elif gl.path==2:
        test2()
    elif gl.path==3:
        test3()
    elif gl.path==4:
        test4()

if __name__ == '__main__':
    main()
