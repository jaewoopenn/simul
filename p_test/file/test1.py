'''
Created on 2016. 9. 30.

@author: jaewo
'''
import util.FUtil as fu
def main():
    test3()
def test1():
    i_f = open("C:/Users/jaewoo/data/file/test.txt","r")
    for line in i_f:
        val=line.strip()
        print val
def test2():
    fu.loadPrn("file/test.txt")
def test3():
    ll=fu.load("file/test.txt")
    for v in ll:
        print v
if __name__ == '__main__':
    main()