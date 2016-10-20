'''
Created on 2016. 10. 19.

@author: jaewo
'''
def load(fn):
    i_f = open("C:/Users/jaewoo/data/file/test.txt","r")
    for line in i_f:
        val=line.strip()
        print val
    