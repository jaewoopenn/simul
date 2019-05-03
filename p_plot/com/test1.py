'''
Created on 2015. 12. 11.

@author: cpslab
'''
import com.CUtil as cut


def u_draw(c):
    c.add_p(0,0)
    c.add_s(4,0,2)
    c.add_s(8,2,4)
    c.add_p(12,4)

def u_draw2(c):
    c.add_p(0,0)
    c.add_d(4,0,2)
    c.add_d(5,2,3)
    c.add_d(8,3,5)
    c.add_d(10,5,6)
    c.add_p(11,6)
    
    
def main():
    cu=cut.C_Draw()
    u_draw(cu)
    cu.draw()
    cu.clear()
    u_draw2(cu)
    cu.draw()
    cu.show()
    

if __name__ == '__main__':
    main()
