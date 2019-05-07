'''
Created on 2015. 12. 11.

@author: cpslab

Vector draw 

File Input

'''
import com.MCom as cu




def u_draw(c):
    c.add_st(0,0)
    c.add_hori(2)
    c.add_diag(2)
    c.add_hori(2)
    c.add_diag(2)
    c.add_hori(2)

def u_draw2(c):
    c.add_st(0,0)
    c.add_hori(2)
    c.add_vert(2)
    c.add_hori(3)
    c.add_vert(1)
    c.add_hori(1)
    
    
def main():
    c1=cu.C_Draw()
    u_draw(c1)
    c1.draw()
    c1.clear()
    u_draw2(c1)
    c1.draw()
    c1.show()
    

if __name__ == '__main__':
    main()
