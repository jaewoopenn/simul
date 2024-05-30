'''
Created on 2024. 5. 30.

@author: jaewoo
'''

class CSimul:
    '''
    classdocs
    '''
    cur_t=0
    cur_job=[]
    queue=[]

    def __init__(self):
        '''
        Constructor
        '''
        pass
    def add(self,e):
        self.queue.append(e)
        
    def add_ed(self,e):
        if self.cur_job:
            self.queue.append(tuple(self.cur_job))
            self.cur_job=[0,0,0]
        self.queue.append(e)
        self.queue.sort(key=lambda s: s[1])   

def simul_t(c):
    if not c.cur_job:
        c.cur_job=list(c.queue.pop(0))
        
    if c.cur_job[2]==0:
        if len(c.queue)!=0:
            c.cur_job=list(c.queue.pop(0))
    if c.cur_job[2]>0:
        print(c.cur_t,":",c.cur_job[0],"rem:",c.cur_job[2]," dl:",c.cur_job[1])
        c.cur_job[2]-=1
    else:
        print(c.cur_t,": idle")        
    c.cur_t+=1