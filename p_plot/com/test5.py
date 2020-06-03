'''
Created on 2015. 12. 11.

@author: cpslab

Vector draw 

File Input

'''

import com.MCom as mc



def u_load(c,fn):
    mc.load_fn(c,fn )
    
    
def main():
    c1=mc.C_Draw()
    u_load(c1,"com/ex1.txt")
    c1.draw()
    c2=mc.C_Draw()
    u_load(c2,"com/ex2.txt")
    c2.draw()
    c1.lim(21,15)
    c1.show()
    

if __name__ == '__main__':
    main()
