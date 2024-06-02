'''
Created on 2015. 12. 11.

@author: cpslab


'''



class gl:
    path=1
#     path=2
#     path=3
#     path=4

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
    def prn(self):
        for e in self.vec:
            e.prn()

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
    old_g=0
    
    return tv

def test1():
    v= Demand()
    dem_add(v,TD(4,1))
    dem_add(v,TD(10,2))
    dem_add(v,TD(7,1))
    dem_add(v,TD(7,2))
    v.prn()
    gap=dem_gap(v)
    print(gap)

def test2():
   
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

if __name__ == '__main__':
    main()
