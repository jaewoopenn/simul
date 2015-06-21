'''
Created on 2015. 6. 19.

@author: Jaewoo Lee
'''
class gl:
    num=13
    case=-1 # -1: all, #: test case number

def test1():
    return 0
def test2():
    return 0
def test3():
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
def test11():
    return 0
def test12():
    return 0
def test13():
    return 0
def test14():
    return 0
def test15():
    return 0
def test16():
    return 0
def test17():
    return 0
def test18():
    return 0
def test19():
    return 0
def test20():
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
      9:test10,
      10:test11,
      11:test12,
      12:test13,
      13:test14,
      14:test15,
      15:test16,
      16:test17,
      17:test18,
      18:test19,
      19:test20
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
     9:0,
     10:0,
     11:0,
     12:0,
     13:0,
     14:0,
     15:0,
     16:0,
     17:0,
     18:0,
     19:0
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

