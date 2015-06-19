'''
Created on 2015. 6. 19.

@author: Jaewoo Lee
https://docs.python.org/2/library/unittest.html
'''

import util.JobMng as ujob
ans=[2]
def test1():
    ujob.clear()
    ujob.insert(1, 3, 4)
    ujob.insert(1, 2, 3)
    ujob.insert(1, 2, 3)
    return ujob.size()

def main():
    print test1()    
if __name__ == '__main__':
    main()

