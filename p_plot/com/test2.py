'''
Created on 2015. 12. 11.

@author: cpslab

Vector draw 

File Input

'''
import com.MCom as cu




def u_draw(c):
    c.add_st(0,0)
    c.add_diag(4,2.3)
    c.add_diag(8,2.3)
    c.add_hori(12)

def u_draw2(c):
    c.add_st(0,0)
    c.add_vert(4,2)
    c.add_vert(5,1)
    c.add_vert(8,1)
    c.add_vert(10,1)
    c.add_hori(11)
    
    
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
