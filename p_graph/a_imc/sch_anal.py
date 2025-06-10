'''
Draw Acceptance Ratio //// MC-FLEX

@author: cpslab
'''
import util.MFile as mf
class gl_input:
    fn1="run/imc1/a_graph.txt"
    fn2="test/mc/a_graph.txt"


class gl:
    x=[]
    vv=[]
    # imc=1
    imc=0

def load(fn):
    i_f = mf.load(fn)
    raw=[]
    for line in i_f:
        val=line.strip().split(" ")
        raw.append(val)
    itemlen=len(raw[0])
    for i in range(1,len(raw)):
        gl.x.append(str(int(raw[i][0])/100))

    for i in range(1,itemlen):
        v=[]
        for j in range(1,len(raw)):
            v.append(float(raw[j][i]))
#         print(v)
        gl.vv.append(v)
        
        

def main():
    if gl.imc==1:
        load(gl_input.fn1)
    else:
        load(gl_input.fn2)
    # x_b=0
    # y_b=0
    # z_b=0
    # w_b=0
    # for i in range(11):
    #     x=gl.vv[0][i]-gl.vv[1][i]
    #     x_b=max(x_b,x)
    #     y=gl.vv[0][i]-gl.vv[2][i]
    #     y_b=max(y_b,y)
    #     z=gl.vv[0][i]-gl.vv[3][i]
    #     z_b=max(z_b,z)
    #     if gl.imc==1:
    #         print(gl.x[i],x,y,z)
    #     else:
    #         w=gl.vv[0][i]-gl.vv[4][i]
    #         w_b=max(z_b,z)
    #         print(gl.x[i],x,y,z,w)
    #
    # if gl.imc==1:
    #     print(x_b,y_b,z_b)
    # else:
    #     print(x_b,y_b,z_b,w_b)

if __name__ == '__main__':
    main()
