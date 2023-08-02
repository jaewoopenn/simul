'''
Draw DMR varying utilization

Created on 2015. 12. 11.

// MC-FLEX
@author: cpslab
'''
import util.MFile as mf
class gl_input:
    path="run_f/pi"
#     path="run_f/li"
    fn="a_sim_graph.txt"

class gl:
    x=[]
    vv=[]

def load(i):
    gl.x=[]
    gl.vv=[]
    
    i_f = mf.load(gl_input.path+str(i)+"/"+gl_input.fn)
    raw=[]
    for line in i_f:
        val=line.strip().split(" ")
        raw.append(val)
    itemlen=len(raw[0])
#     print(len(raw))
    for i in range(1,len(raw)):
        gl.x.append(str(int(raw[i][0])/100))

    for i in range(1,itemlen):
#         z=itemlen-i
        v=[]
        for j in range(1,len(raw)):
            val=1-float(raw[j][i]);
            v.append(val)
#         print(v)
        gl.vv.append(v)
        

def loop(i):
    load(i)
    x_b=0
    y_b=0
    for i in range(8):
        x=gl.vv[0][i]-gl.vv[1][i]
        x_b=max(x_b,x)
        y=gl.vv[0][i]-gl.vv[2][i]
        y_b=max(y_b,y)
#         z=gl.vv[0][i]-gl.vv[3][i]
#         z_b=max(z_b,z)
        print(gl.x[i],x,y)
    print(x_b,y_b)
    
def main():
    loop(0)
    print("--1")
    loop(1)
    print("--2")
    loop(2)
    print("end")

if __name__ == '__main__':
    main()
