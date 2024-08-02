'''
Created on 2024. 5. 30.

@author: jaewoo
'''
import sys

class gl:
    bPrn=0
#     bPrn=1
    
def prn(str):
    if not gl.bPrn:
        return
    print(str)
    
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
        if not gl.bPrn:
            return
        print(self.queue, self.opt_queue)

    def prn_c(self,t,str1):
        if not gl.bPrn:
            return
        print(t,":",str1,self.cur_job[0],
            "rem:",self.cur_job[2], self.cur_job[3],
            " dl:",self.cur_job[1])
    def prn_opt(self,t):
        if not gl.bPrn:
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
                prn("{} : idle1".format(t))
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
                prn("{}: idle2".format(t))
                c.cur_opt=0
            return
    
    # exec >0
    if c.cur_job[2]>0:
        c.prn_c(t,"MAN")
        c.cur_job[2]-=1
        
