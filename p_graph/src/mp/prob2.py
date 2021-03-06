'''
Created on 2015. 12. 11.

@author: cpslab
'''
import util.MPlot as mp;
class gl:
    path="final_rs/mp/"
#     path="mp/rs/"
    fn="prob_sim"
#     RS="260"
    RS="300"
#     RS="340"
    x=[]
    vv=[]
    line=['r-','b--','m-.','g:','k--']
    marker=['o','v','D','^','s']
    lab=['OURS','BASELINE','x','x','x']
    data=[1,2,3,4,5]

def load(fn):
    i_f = open("C:/Users/jaewoo/data/"+fn,"r")
    v=[]
    for line in i_f:
        val=line.strip()
        v.append(float(val))
    return v
def x_load():
    fn=gl.path+gl.fn+"_"+gl.RS+"_x.txt"
    gl.x=load(fn)

def iterate(s,t):
    for i in range(s,t):
        fn=gl.path+gl.fn+"_"+gl.RS+"_"+str(gl.data[i])+".txt"

        v=load(fn)
        gl.vv.append(v)

def main():
    x_load()
    iterate(0,2)
    no=0
    mp.prepare()
    for v in gl.vv:
        mp.plot3(gl.x,v,gl.line[no],gl.lab[no],gl.marker[no])
        no+=1
    mp.xlim(0.001,1)
    mp.ylim(-0.001, 0.35)
    mp.legendUL()
    mp.xlabel("The Probability of Mode Switch")
    mp.ylabel("Deadline Miss Ratio")
    mp.savefig("/Users/jaewoo/data/fig/"+gl.fn+"_"+gl.RS+".pdf")
    mp.show()

if __name__ == '__main__':
    main()
