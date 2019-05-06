'''
Created on 2015. 12. 11.

@author: cpslab

Vector draw 

File Input

'''

import com.MCom as mc


def u_draw(c):
    c.add_st(0,0)
    c.add_diag(4,2.3)
    c.add_diag(8,2.3)
    c.add_hori(12)

def u_load(c):
#     mc.load_fn(c,"test/test_word.txt")
    mc.load_fn(c,"test/test_dbf.txt")
    
    
def main():
    c1=mc.C_Draw()
#     u_draw(c1)
    u_load(c1)
    c1.draw()
    c1.show()
    

if __name__ == '__main__':
    main()
