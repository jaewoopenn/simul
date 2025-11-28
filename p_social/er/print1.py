'''
Draw Acceptance Ratio //// MC-FLEX

@author: cpslab
'''
import util.MFile as mf
class gl_input:
    fn="patient/rs.txt"
    fn2="patient/trace_lmic.txt"
    limit=1440*10
def main():
    anal1()
    anal2()
    
def anal1():
    i_f = mf.load(gl_input.fn)
    tot=0
    lo=0
    lo_fail=0
    liv=0
    hi_deny=0
    preempt=0
    wait=0
    for line in i_f:
        # print(line)
        val=line.strip().split(" ")
        # print(val)
        if val[1]=="HI":
            tot+=1
            if val[3]=='0':
                liv+=1
            if val[3]=='2':
                # hi_deny+=1
                print("2")
            if val[3]=='3':
                hi_deny+=1
        if val[1]=="LO":
            if val[3]=='0':
                lo+=1
            if val[3]=='1':
                lo_fail+=1
            if val[2]!='0':
                preempt+=int(val[2])
            wait+=int(val[4])
            # print(line)
    print(f"total:{tot:d} live:{liv:d}")
    liv_p=liv/tot*100    
    print(f"per:{liv_p:.1f}")
    print(f"deny:{hi_deny:d} ")
    avg_wait=wait/lo    
    tot_lo=lo+lo_fail
    print(f"total:{tot_lo:d} live:{lo_fail:d}")
    liv_p=lo_fail/tot_lo*100    
    print(f"per:{liv_p:.1f}")
    print(f"avg_wait:{avg_wait:.1f}")
    print(f"preempt:{preempt:d}")

def anal2():
    i_f = mf.load(gl_input.fn2)
    burst=0
    old=''
    burst_f=False
    for line in i_f:
        # print(line)
        val=line.strip().split(" ")
        if val[2]==old:
            burst_f=True
        else:
            if burst_f==True:
                burst+=1
                # print("burst")
            burst_f=False
        old=val[2]
        if int(val[2])>gl_input.limit:
            break
    
    print(f"burst:{burst:d}")
    
if __name__ == '__main__':
    main()
