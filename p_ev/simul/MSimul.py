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
    def prn(self):
        print(self.queue)

def simul_t(c,t):
    if not c.cur_job:
        if len(c.queue)!=0:
            c.cur_job=list(c.queue.pop(0))
        else:
            print(t,": idle")
            c.cur_opt=0
            return
    if c.cur_job[2]==0:
        # HERE, optional execution 
        if c.cur_job[3]>0 and c.cur_opt<c.opt_max:
            print(t,":",c.cur_job[0],"rem:",c.cur_job[2],c.cur_job[3]," dl:",c.cur_job[1])
            c.cur_job[3]-=1
            c.cur_opt+=1
            return
        
        if len(c.queue)!=0:
            c.cur_job=list(c.queue.pop(0))
        else:
            c.cur_job=None
            print(t,": idle")
            return
        
    if c.cur_job[2]>0:
        print(t,":",c.cur_job[0],"rem:",c.cur_job[2],c.cur_job[3]," dl:",c.cur_job[1])
        c.cur_job[2]-=1
        
