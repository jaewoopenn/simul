'''
Created on 2015. 12. 11.

@author: cpslab

Vector draw 

File Input

    line=['r-','b--','m-.','g:','k--']
    marker=['o','v','D','^','s']

'''

import com.MCom as mc



def u_load(c):
    mc.load_fn(c,"com/sbf1.txt")
    
    
def main():
    c1=mc.C_Draw()
    u_load(c1)
    c1.draw2('r-')
    c1.lim(21,13)
    c1.show()
    

if __name__ == '__main__':
    main()
