'''
Draw DMR varying utilization

Created on 2015. 12. 11.

// MC-FLEX
@author: cpslab
'''
import util.MFile as mf
import util.MPlot as mp;
class gl_input:
    savename="run/pmsi"
    path="run/pi"
    ylim=0.50

#     savename="run/lmsi"
#     path="run/li"
#     ylim=0.55
       
      




    fn="a_sim_graph.txt"
    xlab= "Utilization Bound"
    ylab= "Percentage of Fully-serviced Job"

class gl:
    lab=[]
    x=[]
    vv=[]
    line=['r--','b-.','k-','g--','m:']
    marker=['o','v','D','^','s']

def load(i):
    gl.lab=[]
    gl.x=[]
    gl.vv=[]
    
    i_f = mf.load(gl_input.path+str(i)+"/"+gl_input.fn)
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
            val=1-float(raw[j][i]);
            v.append(val)
#         print(v)
        gl.vv.append(v)
        

def loop(i):
    load(i)
    no=0
    mp.prepare()
#     mp.prepare2()
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
    mp.legendBL()
    
    mp.xlabel(gl_input.xlab)
    mp.ylabel(gl_input.ylab)
#     mp.ylim(0,gl_input.ylim)
    mp.savefig(mf.filepath(gl_input.savename+str(i)+".pdf"))
    
def main():
    loop(0)
    loop(1)
    loop(2)
    print("end")

if __name__ == '__main__':
    main()
