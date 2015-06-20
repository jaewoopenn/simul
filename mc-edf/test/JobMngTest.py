'''
Created on 2015. 6. 19.

@author: Jaewoo Lee
https://docs.python.org/2/library/unittest.html
'''
import util.JobMng as ujob
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

# second next 
def test4():
    return 0

fmap={
      0:test1,
      1:test2,
      2:test3,
      3:test4
}
ans={
     0:3,
     1:4,
     2:1,
     3:0
}
def runAll():
    for i in range(len(fmap)):
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
    runAll()
#     runOne(2)
if __name__ == '__main__':
    main()

