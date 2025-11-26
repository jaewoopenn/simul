'''
Draw Acceptance Ratio //// MC-FLEX

@author: cpslab
'''
import util.MFile as mf
class gl_input:
    fn="patient/rs.txt"
    fn2="patient/test.txt"

def main():
    anal1()
    # anal2()
    
def anal1():
    i_f = mf.load(gl_input.fn)
    tot=0
    lo=0
    liv=0
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
        if val[1]=="LO":
            if val[3]=='0':
                lo+=1
            if val[2]!='0':
                preempt+=int(val[2])
            wait+=int(val[4])
            # print(line)
    print(f"total:{tot:3d} live:{liv:3d}")
    liv_p=liv/tot    
    print(f"per:{liv_p:.3f}")
    print(f"preempt:{preempt:3d}")
    avg_wait=wait/lo    
    print(f"avg_wait:{avg_wait:.3f}")

def anal2():
    i_f = mf.load(gl_input.fn2)
    burst=0
    old=''
    for line in i_f:
        print(line)
        val=line.strip().split(" ")
        if val[2]==old:
            burst+=1
        old=val[2]
    print(f"total:{burst:3d}")
    
if __name__ == '__main__':
    main()
