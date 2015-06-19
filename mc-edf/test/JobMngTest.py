'''
Created on 2015. 6. 19.

@author: Jaewoo Lee
https://docs.python.org/2/library/unittest.html
'''
import util.JobMng as ujob
def test1():
    ujob.clear()
    ujob.insert(1, 3, 4)
    ujob.insert(1, 2, 3)
    return ujob.size()

def test2():
    ujob.clear()
    ujob.insert(1, 3, 4)
    ujob.insert(1, 2, 3)
    ujob.insert(1, 5, 7)
    return ujob.size()

fmap={
      0:test1,
      1:test2
}
ans={
     0:2,
     1:3
}

def main():
    for i in range(len(fmap)):
        print "test",i
        if fmap[i]()!=ans[i]:
            print "error"
    print "end"    
if __name__ == '__main__':
    main()

