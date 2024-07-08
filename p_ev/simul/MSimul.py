'''
Created on 2024. 5. 30.

@author: jaewoo
'''

class CSimul:
    '''
    classdocs
    '''
    cur_job=None
    queue=[]
    opt_queue=[]
    opt_max=2
    cur_opt=0

    def __init__(self):
        '''
        Constructor
        '''
        pass
    def add(self,e):
        self.queue.append(e)
        
    def add_ed(self,e):
        if self.cur_job:
            if e[1]<self.cur_job[1]:
                self.queue.append(tuple(self.cur_job))
                self.cur_job=None
        self.queue.append(e)
        self.queue.sort(key=lambda s: s[1])   
    def add_opt(self,e):
        self.opt_queue.append(e)
        self.opt_queue.sort(key=lambda s: s[1])   
        
    def prn(self):
        print(self.queue, self.opt_queue)


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
    c.opt_queue[0][3]-=1
    print(t,":",c.opt_queue[0][0],"opt rem:",c.opt_queue[0][3]," dl:",c.opt_queue[0][1])
    if c.opt_queue[0][3]==0:
        c.opt_queue.pop(0)
    return 1

def simul_t(g,c,t):
    # no current job
    if not c.cur_job:
        if not simul_reload(c):
            if not simul_opt(c,t):
                print(t,": idle1")
                c.cur_opt=0
            return

    # exec =0
    if c.cur_job[2]==0:
        # HERE, optional execution 
        c.opt_max=int(g.vec[0][1]*0.2)
        if c.cur_job[3]>0 and c.cur_opt<c.opt_max:
            print(t,":",c.cur_job[0],"opt rem:",c.cur_job[2],c.cur_job[3]," dl:",c.cur_job[1]," optmax:",c.opt_max)
            c.cur_job[3]-=1
            c.cur_opt+=1
            return
        c.add_opt(c.cur_job)
        if not simul_reload(c):
            if not simul_opt(c,t):
                print(t,": idle2")
                c.cur_opt=0
            return
    
    # exec >0
    if c.cur_job[2]>0:
        print(t,":",c.cur_job[0],"rem:",c.cur_job[2],c.cur_job[3]," dl:",c.cur_job[1])
        c.cur_job[2]-=1
        
