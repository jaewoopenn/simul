package exp;


import gen.ConfigGen;
import sim.SimulInfo;
import sim.com.TaskSimulCom;

public class ExpSimulCom extends ExpSimul{
	private TaskSimulCom g_tsim;
	public ExpSimulCom(ConfigGen cfg) {
		super(cfg);
	}
	




	public void initSim(TaskSimulCom tsim) {
		g_tsim=tsim;
	}

	@Override
	protected void simulStart() {
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
