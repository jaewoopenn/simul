'''
Created on 2015. 12. 11.

@author: cpslab

Vector draw 

File Input

'''

import com.MCom as mc



def u_load(c):
    mc.load_fn(c,"com/draw/rbf2_ex1.txt")
#     mc.load_fn(c,"com/draw/rbf3_ex1.txt")
#     mc.load_fn(c,"test/test_dbf.txt")
    
    
def main():
    c1=mc.C_Draw()
    u_load(c1)
    c1.draw2('b-')
    c1.lim(21,14)
    c1.show()
    

if __name__ == '__main__':
    main()
