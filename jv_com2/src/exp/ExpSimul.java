package exp;


import gen.ConfigGen;
import sim.SimulInfo;
import sim.TaskSimul;
import sim.mc.TaskSimulMC;
import task.TaskMng;
import anal.Anal;

public abstract class ExpSimul {
	protected ConfigGen g_cfg;
	public ExpSimul(ConfigGen cfg) {
		g_cfg=cfg;
	}
	public int size(){
		return g_cfg.readInt("num");
	}
	

	public abstract int anal(TaskMng tm, Anal a) ;
	
	public abstract void initSim(int core,TaskSimulMC tsim);

	protected abstract void simulStart();
	public abstract void simul(int st,int et);
	public abstract SimulInfo getSI(int core);
	public abstract void prn() ;
	
}
