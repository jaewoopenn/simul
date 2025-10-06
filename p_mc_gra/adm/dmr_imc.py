'''
Draw DMR varying utilization

Created on 2015. 12. 11.

// MC-FLEX
@author: cpslab
'''
import util.MFile as mf
import util.MPlot as mp;
class gl_input:
    savename="adm/graph_"
    path="adm/"
    # ylim=0.30
    # ylim=0.60
    ylim=0.80

      
      




    fn="a_sim_graph.txt"
    xlab= "Utilization Bound"
    ylab= "Percentage of Degraded Execution"

class gl:
    lab=[]
    x=[]
    vv=[]
    line=['r--','k-','b-.','g--','m:']
    marker=['o','v','D','^','s']

def load(s):
    gl.lab=[]
    gl.x=[]
    gl.vv=[]
    
    i_f = mf.load(gl_input.path+s+"/"+gl_input.fn)
    raw=[]
    for line in i_f:
        val=line.strip().split(" ")
        raw.append(val)
    itemlen=len(raw[0])
#     print(itemlen)

    for i in range(1,itemlen):
#         z=itemlen-i
        gl.lab.append(raw[0][i])
    for i in range(1,len(raw)):
        gl.x.append(str(int(raw[i][0])/100))
#     print(gl.x)

    for i in range(1,itemlen):
#         z=itemlen-i
        v=[]
        for j in range(1,len(raw)):
            val=float(raw[j][i]);
            v.append(val)
#         print(v)
        gl.vv.append(v)
        

def loop(s):
    load(s)
    no=0
#     mp.prepare()
    mp.prepare2()
    for v in gl.vv:
#         if no==2:
#             no+=1
#             continue
        mp.plot3(gl.x,v,gl.line[no],gl.lab[no],gl.marker[no])
        no+=1
#     if(i==2):
#         mp.legendBR()
#     else:
#         mp.legendUL()
    # mp.legendBL()
    mp.legendUL()
    
    mp.xlabel(gl_input.xlab)
    mp.ylabel(gl_input.ylab)
    mp.ylim(-0.04,gl_input.ylim)
    mp.savefig(mf.filepath(gl_input.savename+s+".pdf"))
    
def main():
    loop("sim_rs")
    # loop(1)
    # loop(2)
    print("end")

if __name__ == '__main__':
    main()
