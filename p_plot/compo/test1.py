'''
Created on 2015. 12. 11.

@author: cpslab
'''
import compo.CUtil as cut


def u_draw(c):
    c.add_p(0,0)
    c.add_s(4,0,2)
    c.add_s(8,2,4)
    c.add_p(12,4)

    
def main():
    cu=cut.C_Draw()
    u_draw(cu)
    cu.draw()
    

if __name__ == '__main__':
    main()
