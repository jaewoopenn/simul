package exp;


import gen.ConfigGen;
import sim.SimulInfo;

public abstract class ExpSimul {
	protected ConfigGen g_cfg;
	public ExpSimul(ConfigGen cfg) {
		g_cfg=cfg;
	}
	

	

	protected abstract void simulStart();
	public abstract void simul(int st,int et);
	public abstract SimulInfo getSI();
	public abstract void prn() ;
	
}
