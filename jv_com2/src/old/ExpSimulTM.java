package old;


import gen.ConfigGen;
import sim.SimulInfo;
import sim.mc.TaskSimulMC;
import exp.ExpSimul;

public class ExpSimulTM extends ExpSimul{
	private TaskSimulMC g_tsim;
	public ExpSimulTM(ConfigGen cfg) {
		super(cfg);
	}
	




	public void initSim(int core, TaskSimulMC tsim) {
		g_tsim=tsim;
	}

	@Override
	protected void simulStart() {
//		g_tsim[0].simulStart();
	}

	@Override
	public void simul(int st, int et) {
		g_tsim.simul(st,et);
		g_tsim.simul_end();
	}


	@Override
	public SimulInfo getSI() {
		return g_tsim.getSI();
	}


	@Override
	public void prn() {
		
	}




	

}
