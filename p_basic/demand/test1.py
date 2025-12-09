'''
Created on 2015. 12. 11.

@author: cpslab
'''
class gl:
    path=1
#     path=2
#     path=3
    # path=4
    v=[]
def add_dem(t,d):
    gl.v.append((t,d))
def prn_dem():
    print(gl.v)
def test1():
    add_dem(5,1)
    add_dem(6,3)
    prn_dem()
    pass

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
