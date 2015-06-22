'''
Created on 2015. 6. 19.

@author: Jaewoo Lee
'''
import util.JobMng as ujob
import util.UtilLog as ulog
class gl:
    num=9
    case=-1
#     case=8
def test1():
    ujob.clear()
    ujob.insert(0, 3, 4)
    ujob.insert(1, 2, 3)
    if ujob.size()!=2:
        return -1
    ujob.insert(1, 5, 7)
    return ujob.size()

# next t "test"
def test2():
    ujob.clear()
    ujob.insert(0, 3, 4)
    ujob.insert(1, 2, 3)
    t=ujob.next_t(0)
    if t!=3:
        return -1
    return ujob.next_t(t)

# first next
def test3():
    ujob.clear()
    ujob.insert(0, 3, 4)
    ujob.insert(1, 2, 3)
    ujob.progress(0, 3)
    return ujob.size()

def test4():
    ujob.clear()
    ujob.insert(0, 4, 6)
    ujob.insert(1, 4, 5)
    ujob.progress(0, 3)
    return ujob.size()

def test5():
    ujob.clear()
    ujob.insert(0, 4, 8)
    ujob.insert(1, 4, 5)
    ujob.progress(0, 4)
    return ujob.size()

def test6():
    ujob.clear()
    ujob.insert(0, 2, 3)
    ujob.insert(1, 1, 3)
    t=0
    dur=3
    t=ujob.progress(t, dur)
    return ujob.checkDl(t)

def test7():
    ujob.clear()
    ujob.insert(0, 2, 3)
    ujob.insert(1, 2, 3)
    t=0
    dur=3
    t=ujob.progress(t, dur)
    return ujob.checkDl(t)
def test8():
    ujob.clear()
    ujob.insert(0, 2, 3)
    ujob.insert(1, 1, 3)
    ujob.insert(1, 1, 4)
    t=0
    dur=3
    t=ujob.progress(t, dur)
    if ujob.checkDl(t):
        return 1
    dur=1
    t=ujob.progress(t, dur)
    return ujob.checkDl(t)
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
     0:3,
     1:4,
     2:1,
     3:2,
     4:1,
     5:0,
     6:1,
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

