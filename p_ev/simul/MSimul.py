'''
Created on 2024. 5. 30.

@author: jaewoo
'''
import sys
from anal.MGap import TD,CGap
import anal.MGap as mg

class gl:
    bPrn=0
#     bPrn=1
    
def prn(str):
    if not gl.bPrn:
        return
    print(str)
class CSimul:
    '''
    classdocs
    '''
    cur_job=None
    queue=[]
    opt_queue=[]
    opt_max=2
    cur_opt=0
    opt_r=0
    tot_opt=0
    gap=None
    def __init__(self):
        '''
        Constructor
        '''
        pass
    def clear(self):
        self.cur_job=None
        self.cur_opt=0        
        self.queue=[]
        self.opt_queue=[]
        self.gap=CGap()
    def gap_after(self,t):
        self.gap.gap_after(t)

    def add(self,e):
        if self.cur_job:
            if e[1]<self.cur_job[1]:
                if self.cur_job[2]==0:
                    self.add_opt(list(self.cur_job))
                    self.cur_job=None
                else:
                    self.queue.append(tuple(self.cur_job))
                    self.cur_job=None
        self.queue.append(e)
        self.queue.sort(key=lambda s: s[1])   
    def add_opt(self,e):
        self.opt_queue.append(e)
        self.opt_queue.sort(key=lambda s: s[1])   
        
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
    def prn_gap(self):
        self.gap.prn_vec()

def add_ok(cs,e,t):
    td=TD(e[1],e[2])
    cs.gap.prune(t)
    ret=mg.gap_add(cs.gap,td,t)
    if ret:
        cs.gap.compact()
        return 1
    return 0
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
    while t>=c.opt_queue[0][1]:
        c.opt_queue.pop(0)
        if not c.opt_queue:
            return 0
    c.prn_opt(t)
    c.tot_opt+=1
#     print(c.opt_queue[0])
    c.opt_queue[0][3]-=1
    if c.opt_queue[0][3]==0:
        c.opt_queue.pop(0)
    return 1

def simul_t(c,t):

    # no current job
    if not c.cur_job:
        if not simul_reload(c):
            if not simul_opt(c,t):
                prn("{} : idle1".format(t))
                c.cur_opt=0
            return
        
    if t>=c.cur_job[1] and c.cur_job[2]>0:
        print("ERR: DL miss")
        c.prn_c(t,"ERR")
        sys.exit(0)
    
    # exec =0
    if c.cur_job[2]==0:
        # HERE, optional execution 
        if c.gap.vec:
            c.opt_max=int(c.gap.vec[0].d*c.opt_r)
        else:
            c.opt_max=0
        if c.cur_job[3]>0 and c.cur_opt<c.opt_max:
#             prn(c.opt_max)
            c.prn_c(t,"OPTE")
            c.tot_opt+=1
            c.cur_job[3]-=1
            c.cur_opt+=1
            c.gap.std=c.cur_opt
            return
        # when opt exec ended, change to next job 
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
        
