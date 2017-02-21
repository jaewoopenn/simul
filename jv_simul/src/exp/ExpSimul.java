package exp;


import gen.ConfigGen;
import anal.Anal;
import basic.TaskMng;
import simul.SimulInfo;
import simul.TaskSimul;

public abstract class ExpSimul {
	protected ConfigGen g_cfg;
	public ExpSimul(ConfigGen cfg) {
		g_cfg=cfg;
	}
	public int size(){
		return g_cfg.readInt("num");
	}
	

	public abstract int anal(TaskMng tm, Anal a) ;
	
	public abstract void initSim(int core,TaskSimul tsim);

	protected abstract void simulStart();
	public abstract void simul(int st,int et);
	public abstract SimulInfo getSI(int core);
	public abstract void prn() ;
	
}
