'''
Created on 2015. 12. 11.

@author: cpslab

Vector draw 

File Input

'''
import com.CUtil as cu




def u_draw(c):
    c.add_p(0,0)
    c.add_s(4,2.3)
    c.add_s(8,2.3)
    c.add_end(12)

def u_draw2(c):
    c.add_p(0,0)
    c.add_d(4,2)
    c.add_d(5,1)
    c.add_d(8,1)
    c.add_d(10,1)
    c.add_end(11)
    
    
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
