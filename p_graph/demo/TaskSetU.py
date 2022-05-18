'''
Created on Feb 11, 2013

@author: Jaewoo
'''

class ts:
    # real : util
    # task : task util
    # pro :probalilty
    # log : ratio
#    fn="real"  # vd, load(demand, LCM), 
    fn="real"   
    path="C:/data/mc/in/"
    out_path="C:/my/data/"
    p_l=20
    p_u=[300]
#    p_u=[100,200,300,400]
    proc=[4] #[2,4]
    u_l=30
    u_u=100
    u_step=5
#    numTask=300
    numTask=10000
    t_l=0.02
    z_l=1
    # configure
    t_u=[0.7]
#    t_u=[0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0]
    v_prob=[0.5]
#    v_prob=[0,0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1]
#    z_u=[4]
    z_u=[4]
#    z_u=[1,2,4,8,16,32,64,128,256,512]
