'''
Created on 2015. 6. 20.

@author: Jaewoo Lee
'''
class gl:
    debug=0
def prn(s,v):
    if gl.debug==0:
        return
    print(s,v)

def prnc(s,v):
    if gl.debug==0:
        return
    print(s,v)

def prnln(s):
    if gl.debug==0:
        return
    print(s)
def set_l(lv):
    gl.debug=lv
def get_l():
    return gl.debug