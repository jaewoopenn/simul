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
    u_load(c1,"com/draw/ex_sbf.txt")
    c1.draw3('b-',"SBF")
    c2=mc.C_Draw()
    u_load(c2,"com/draw/ex_isbf.txt")
    c2.draw3('r-',"iSBF")
    c1.lim(21,10)
    c1.show2()
    

if __name__ == '__main__':
    main()
