'''
Created on 2015. 12. 11.

@author: cpslab
'''

class gl:
#     path=1
    path=2

def simul(q):
    cur_id=0
    cur_dl=0
    cur_rem=0
    for i in range(10):
        if cur_rem==0:
            if len(q)!=0:
                (cur_id,cur_dl,cur_rem)=q.pop(0)
        if cur_rem>0:
            print(i,":",cur_id,"rem:",cur_rem," dl:",cur_dl)
            cur_rem-=1
        else:
            print(i,": idle")
    
def test1(): # FIFO
    q=[(1,10,3),(2,10,2),(3,10,3)]
    simul(q)

def test2():
    q=[(1,3,1),(2,10,3)]
    q.append((3,5,2))
    q.sort(key=lambda s: s[1])
    print(q)
    simul(q)

def test3():
    pass

def test4():
    pass
    
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
