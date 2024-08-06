'''

@author: cpslab
'''
import util.MFile as mf
import util.MPlot as mp;
class gl_input:
    path="ev/var/"
    out_path="ev/out/"
    prefix="data"
    xlab= "Utilization bound"
    ylab= "Acceptance ratio"

class gl:
    lab=[]
    x=[]
    vv=[]
    line=['k-','r-','b-','g-','m--']
    marker=['o','v','D','^','s']

def load():
    fn=gl_input.path+gl_input.prefix+"_gra.txt"
    i_f = mf.load(fn)
    raw=[]
    for line in i_f:
#         val=line.strip().split("\t")
        val=line.strip().split()
#         print(val)
        raw.append(val)
    itemlen=len(raw[0])
#     print(itemlen)

    for i in range(1,itemlen):
        gl.lab.append(raw[0][i])
    for i in range(1,len(raw)):
        gl.x.append(str(int(raw[i][0])/100))
#         gl.x.append(raw[i][0])
#     print(gl.x)

    for i in range(1,itemlen):
        v=[]
        for j in range(1,len(raw)):
            v.append(float(raw[j][i]))
#         print(v)
        gl.vv.append(v)
        
        

def main():
#     mp.prepare()
    mp.prepare4()
    load()
    no=0
    for v in gl.vv:
        print(v)
        mp.plot3(gl.x,v,gl.line[no],gl.lab[no],gl.marker[no])
        no+=1
#     mp.ylim(0, 1.02)
    mp.legendBL()
    mp.xlabel(gl_input.xlab)
    mp.ylabel(gl_input.ylab)
    mp.xlab_rot(13)
#     mp.show()
    fn=gl_input.out_path+gl_input.prefix+"_gra.pdf"
    mp.savefig(mf.filepath(fn))

if __name__ == '__main__':
    main()
