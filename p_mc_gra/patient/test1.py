'''
Draw Acceptance Ratio //// MC-FLEX

@author: cpslab
'''
import util.MFile as mf
class gl_input:
    fn="patient/rs.txt"

def main():
    i_f = mf.load(gl_input.fn)
    tot=0
    liv=0
    for line in i_f:
        # print(line)
        val=line.strip().split(" ")
        # print(val)
        if val[1]=="HI":
            tot+=1
            if val[3]=='0':
                liv+=1
            # print(line)
    print(f"total:{tot:3d} live:{liv:3d}")
    liv_p=liv/tot    
    print(f"per:{liv_p:.3f}")

if __name__ == '__main__':
    main()
