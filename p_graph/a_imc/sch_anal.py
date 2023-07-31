'''
Draw Acceptance Ratio //// MC-FLEX

@author: cpslab
'''
import util.MFile as mf
import util.MPlot as mp;
class gl_input:
    fn="run/imc1/a_graph.txt"

class gl:
    x=[]
    vv=[]

def load():
    i_f = mf.load(gl_input.fn)
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
    mp.prepare3()
    load()
    for i in range(11):
        x=gl.vv[0][i]-gl.vv[1][i]
        y=gl.vv[0][i]-gl.vv[2][i]
        z=gl.vv[0][i]-gl.vv[3][i]
        print(gl.x[i],x,y,z)

if __name__ == '__main__':
    main()
