'''
Created on 2015. 12. 11.

@author: cpslab
'''

from util.MFile import CFile
from log.MLog import CLog
from log.MConfig import CConfig

class gl:
#     path=1
#     path=2
#     path=3
    path=4


def run(fn):
    print(fn)
    cf=CFile()
    cf.open_w("ev/var/"+fn+".txt")
    cf.writeKV("num","50")
    cf.writeKV("k2","v2")
    cf.writeKV("k3","v3")
    cf.end()

def run2(fn):
    cf=CFile()
    kv=dict()
    cf.open_r("ev/var/"+fn+".txt")
    while True:
        w=cf.getKV()
        if not w:
            break
        kv[w[0]]=w[1]
#         print(w[0],w[1])
    print(kv.keys())    
    print(kv.values())    
    for k in kv.keys():
        print(k,kv[k])
    print(kv['num'])
def test1():
    run("config1")
#     for i in range(10):
#         run("config"+str(i))


def test2():
    run2("config1")
#     for i in range(10):
#         run2("test"+str(i))
    

def test3():
    c=CConfig()
    c.init_w("ev/var/config1.txt")
    c.addKV("num","50")
    c.addKV("k2","v2")
    c.addKV("k3","v3")
    c.end()    

def test4():
    c=CConfig()
    c.init_r("ev/var/config1.txt")
    w=c.get('num')
    print(w)
    
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
