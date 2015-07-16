'''
Created on 2015. 6. 19.

@author: Jaewoo Lee
'''
import simul.Simul as sim
import util.UtilLog as ulog

class gl:
    num=1
    case=2 # -1: all, #: test case number

def test1():
    sim.clear()
    tl=[[4,1],[3,1]]
    sim.prepare(tl)
    sim.run()
    return 0
def test2():
    sim.clear()
    tl=[[4,2],[3,1]]
    sim.prepare(tl)
    sim.run()
    return 0
def test3():
    sim.clear()
    tl=[[4,2],[3,2]]
    sim.prepare(tl)
    sim.run()
    return 0
def test4():
    return 0
def test5():
    return 0
def test6():
    return 0
def test7():
    return 0
def test8():
    return 0
def test9():
    return 0
def test10():
    return 0

fmap={
      0:test1,
      1:test2,
      2:test3,
      3:test4,
      4:test5,
      5:test6,
      6:test7,
      7:test8,
      8:test9,
      9:test10
}
ans={
     0:0,
     1:0,
     2:0,
     3:0,
     4:0,
     5:0,
     6:0,
     7:0,
     8:0,
     9:0
}
def runAll():
    for i in range(gl.num):
        print "test",i,
        ret=fmap[i]()
        if ret!=ans[i]:
            print "error",ret
        else:
            print "OK"
    print "end"    
def runOne(i):
    ulog.set_l(1)
    print "test",i,
    ret=fmap[i]()
    if ret!=ans[i]:
        print "error",ret
    else:
        print "OK"
def main():
    if gl.case==-1:
        runAll()
    else:
        runOne(gl.case)

if __name__ == '__main__':
    main()

