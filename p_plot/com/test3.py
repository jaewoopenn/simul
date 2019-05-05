'''
Created on 2015. 12. 11.

@author: cpslab

Vector draw 

File Input

'''

import com.MCom as cu
import file.MFile as mf



def u_draw(c):
    c.add_st(0,0)
    c.add_diag(4,2.3)
    c.add_diag(8,2.3)
    c.add_hori(12)

def u_load(c):
    lst=mf.load("test/test_word.txt")
    for m in lst:
        wd=m.split()
        print(wd)
        a=int(wd[1])
        if wd[0]=='s':
            b=float(wd[2])
            c.add_st(a,b)
        elif wd[0]=='d':
            b=float(wd[2])
            c.add_diag(a,b)
        elif wd[0]=='h':
            c.add_hori(a)
    
    
def main():
    c1=cu.C_Draw()
#     u_draw(c1)
    u_load(c1)
    c1.draw()
    c1.show()
    

if __name__ == '__main__':
    main()
