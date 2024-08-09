'''
Created on 2024. 5. 30.

@author: jaewoo
'''
import sys
import math
from log.MLog import CLog
import simul.MSimCom as msc

class gl:

    end_t=40
    max_s=3
    

#     chr_r=1
    chr_r=0.7    
    
class CSimulF:
    cur_job=None
    queue=[]
    opt_queue=[]
    tot_opt=0
    chr_r=1
    
    def __init__(self):
        '''
        Constructor
        '''
        pass
    def clear(self):
        self.cur_job=None
        self.queue=[]
        self.opt_queue=[]
    def add(self,e):
        self.queue.append(e)
        
        
    def add_opt(self,e):
        self.opt_queue.append(e)
        
    def prn(self):
        if not msc.isPrn():
            return 
        print(self.queue, self.opt_queue)

    def prn_c(self,t,str1):
        if not msc.isPrn():
            return 
        print(t,":",str1,self.cur_job[0],
            "rem:",self.cur_job[2], self.cur_job[3],
            " dl:",self.cur_job[1])
    def prn_opt(self,t):
        if not msc.isPrn():
            return 
        print(t,": OPT",self.opt_queue[0][0],
            "rem:",self.opt_queue[0][3],
            " dl:",self.opt_queue[0][1])

def add_ok(cs,e,t):
    req=t
    if cs.cur_job:
        req+=cs.cur_job[2]
    for item in cs.queue:
        req+=item[2]
#     print(req, e[2],e[1])
    if req+e[2]>e[1]:
        return 0
    return 1
    
def simul_reload(c):
    if len(c.queue)!=0:
        c.cur_job=list(c.queue.pop(0))
        return 1
    else:
        c.cur_job=None
        return 0


def simul_opt(c,t):
    if not c.opt_queue:
        return 0
    while t>=c.opt_queue[0][1] or c.opt_queue[0][3]<=0:
        c.opt_queue.pop(0)
        if not c.opt_queue:
            return 0
    c.prn_opt(t)
    c.tot_opt+=1
    c.opt_queue[0][3]-=1
    return 1

def simul_t(c,t):
    # no current job
    if not c.cur_job:
        if not simul_reload(c):
            if not simul_opt(c,t):
                msc.prn("{} : idle1".format(t))
#                 print(t,": idle1")
                c.cur_opt=0
            return
    if t>=c.cur_job[1] and c.cur_job[2]>0:
        print("ERR: DL miss")
        c.prn_c(t,"ERR")
        sys.exit(0)
    
    # exec =0
    if c.cur_job[2]==0:
        c.add_opt(c.cur_job)
        if not simul_reload(c):
            if not simul_opt(c,t):
                msc.prn("{}: idle2".format(t))
                c.cur_opt=0
            return
    
    # exec >0
    if c.cur_job[2]>0:
        c.prn_c(t,"MAN")
        c.cur_job[2]-=1
        

def add_fifo_ok(t,w,cs): 
    if w[1]<w[2]:
        return 0
    mod=math.ceil(w[2]*cs.chr_r)
    e=(0,t+w[1],mod,w[3]) # id, deadline, req, opt
    
    return add_ok(cs,e,t)


    
def run_fifo(fn):
    csa=[]
    reject=0
    for i in range(gl.max_s):
        cs=CSimulF()
        cs.clear()
        if i==gl.max_s-1:
            cs.chr_r=gl.chr_r
        csa.append(cs)
    cl=CLog(fn)
    
    t=0
    while t<gl.end_t:
        while t==cl.getLast():
            w=cl.getW()
            add_ok=0
            for i in range(gl.max_s):
                if add_fifo_ok(t,w,csa[i]):
                    msc.add_com(t,w,csa[i])
                    add_ok=1
                    break
            if add_ok==0:
                reject+=1
                msc.prn("error {}".format(reject))
                
        for i in range(gl.max_s):
            simul_t(csa[i],t)
        t+=1
    return reject, cs.tot_opt
