'''
Created on 2024. 5. 30.

@author: jaewoo
'''
import sys

class gl:
    bPrn=0
    
def prn(str):
    if not gl.bPrn:
        return
    print(str)
class CSimulF:
    cur_job=None
    queue=[]
    opt_queue=[]
    
    def __init__(self):
        '''
        Constructor
        '''
        pass
    def add(self,e,t):
        if not self.cur_job:
            self.cur_job=list(e)
            return 1
            
        capa=self.cur_job[1]-t
        req=self.cur_job[2]
        for item in self.queue:
            req+=item[2]
#         print(req+e[2],capa)
        if req+e[2]>capa:
            return 0
        if req+e[2]>e[1]:
            return 0
        self.queue.append(e)
        return 1
        
        
    def add_opt(self,e):
        self.opt_queue.append(e)
        
    def prn(self):
        if not gl.bPrn:
            return
        print(self.queue, self.opt_queue)

    def prn_c(self,t,str):
        if not gl.bPrn:
            return
        print(t,":",str,self.cur_job[0],
            "rem:",self.cur_job[2], self.cur_job[3],
            " dl:",self.cur_job[1])
    def prn_opt(self,t):
        if not gl.bPrn:
            return
        print(t,"OPT :",self.opt_queue[0][0],
            "rem:",self.opt_queue[0][3],
            " dl:",self.opt_queue[0][1])

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
    c.opt_queue[0][3]-=1
    if c.opt_queue[0][3]==0:
        c.opt_queue.pop(0)
    return 1

def simul_t(c,t):
    # no current job
    if not c.cur_job:
        if not simul_reload(c):
            if not simul_opt(c,t):
                prn("{}: idle1".format(t))
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
                print(t,": idle2")
                c.cur_opt=0
            return
    
    # exec >0
    if c.cur_job[2]>0:
        c.prn_c(t,"MAN")
        c.cur_job[2]-=1
        
