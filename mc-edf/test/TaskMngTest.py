'''
Created on 2015. 6. 19.

@author: Jaewoo Lee
'''
import util.TaskMng as utsk

class gl:
    num=4
    case=-1 # -1: all, #: test case number
#     case=3
def test1(): # init
    utsk.clear()
    tl=[[4,1],[3,1]]
    utsk.init(tl)
    return utsk.size()
def test2(): # init add
    utsk.clear()
    tl=[[4,1],[3,1]]
    utsk.init(tl)
    utsk.insert([5,1])
    return utsk.size()
def test3(): # init clear add
    utsk.clear()
    tl=[[4,1],[3,1]]
    utsk.init(tl)
    utsk.clear()
    utsk.insert([5,1])
    return utsk.size()
def test4():
    utsk.clear()
    tl=[[4,1],[3,1]]
    utsk.init(tl)
    return (utsk.get(0) == (4,1)) and (utsk.get(1) == (3,1))
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
     0:2,
     1:3,
     2:1,
     3:1,
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

