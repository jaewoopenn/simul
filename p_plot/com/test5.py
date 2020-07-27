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
    u_load(c1,"com/draw/ex1.txt")
    c1.draw2('b-')
    c2=mc.C_Draw()
    u_load(c2,"com/draw/ex2.txt")
    c2.draw2('r-')
    c1.lim(21,14)
    c1.show()
    

if __name__ == '__main__':
    main()
