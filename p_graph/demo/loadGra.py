'''
Created on Feb 11, 2013

@author: Jaewoo
'''
from TaskSetU import ts
#from task.TaskSetL import ts

import matplotlib.pyplot as plt
import matplotlib
import util
matplotlib.rcParams['font.size'] = 15
matplotlib.rcParams['font.weight'] = "bold"
class lc:
    data=[]
    line=['r-', 'b--', 'm-.', 'k:']
#     line=['r-', 'b--', 'm-.']
#    line=['r-', 'b--']
#    line=['r-']
#     lab=['MC-DP-Fair','PART','GLO','FP']
    lab=['MC-Fluid','P-EDF-VD','fpEDF-VD','G-MC-FP']
#     lab=['P-EDF-VD','fpEDF-VD','G-MC-FP']
#    lab=['MC-DP-Fair']
#    lab=['OPT', 'HEURISTIC','H2']
#    lab=['prev', 'lm']
    algo=["ne2","pa","fp","opa"]
#     algo=["pa","fp","opa"]
#    algo=["ne","ne2"] #,"gl"
    sel=0
    
    
def run():
    for m in ts.proc:
        for u in range(ts.u_l,ts.u_u+1,ts.u_step):
            for p in ts.v_prob:
                for t_u in ts.t_u:
                    for z_u in ts.z_u:
                        for p_u in ts.p_u:
                            loop(m,u,p,t_u,z_u,p_u)
    draw()
def loop(m,u,p,t_u,z_u,p_u):
    prefix=str(m)+"_"+str(u)+"_"+str(p)+"_"+str(t_u)+"_"+str(z_u)+"_"+str(p_u)
#    print m,u,p,t_u,z_u,n_u,
#    print m,u,
#    print t_u,
#    print p,
#    print z_u,
#    print n_u,
#    print m,'\t',u,
    for algo in lc.algo:
        v=readTask(algo,prefix)
        lc.data.append(v)
#        print '\t',v,
#    print ""
def draw():
    xl=[]
    vl=[]
    if lc.sel==0:
        xRan=range(ts.u_l,ts.u_u+1,ts.u_step)
        xLab="Normalized Utilization Bound"
    elif lc.sel==1:
        xRan=ts.v_prob
        xLab="Probability"
    elif lc.sel==2:
        xRan=ts.t_u
        xLab="Utilization"
    elif lc.sel==3:
        xRan=ts.p_u
        xLab="Period"
    else:
        xRan=ts.z_u
        xLab="Ratio"
    xSt=xRan[0]*1.0/100
    xEd=xRan[-1]*1.0/100
    for i in range(len(lc.algo)):
        vl.append([])
    for u in xRan:
        xl.append(u*1.0/100)
        for i in range(len(lc.algo)):
            vl[i].append(lc.data.pop(0))
#    print xl
#    for i in range(len(lc.algo)):
#        print vl[i]
    for i in range(len(lc.algo)):
        util.plot(xl, vl[i], lc.line[i], lc.lab[i])
    plt.xlabel(xLab)
    plt.ylabel("Acceptance Ratio")
#    plt.xlim(0.1,0.9)
    plt.xlim(xSt,xEd)
    plt.ylim(0.01,1.05)
    util.legend()
    util.show()
def readTask(algo,prefix):
    i_f = open(ts.out_path+ts.fn+"_"+algo+"_"+prefix+".out","r")
    suc=0
    for line in i_f:
        if int(line)==1:
            suc+=1
    #print prefix,'\t',float(suc)/ts.numTask
    return float(suc)/ts.numTask
        #print line
    #procTask(tempV)
    #analTask()
def main():
    run()

main()