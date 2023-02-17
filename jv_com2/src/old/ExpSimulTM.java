package old;


import gen.ConfigGen;
import sim.SimulInfo;
import sim.mc.TaskSimulMC;
import task.TaskMng;
import exp.ExpSimul;
import anal.Anal;
import util.MUtil;

public class ExpSimulTM extends ExpSimul{
	private TaskSimulMC g_tsim;
	public ExpSimulTM(ConfigGen cfg) {
		super(cfg);
	}
	

	@Override
	public int anal(TaskMng tm, Anal a) {
//		AnalEDF_AD_E a=new AnalEDF_AD_E();
		a.init(tm);
		a.prepare();
//		Log.prn(2, ""+a.getDtm());
		boolean b=a.is_sch();
		return MUtil.btoi(b);
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
	public SimulInfo getSI(int core) {
		return g_tsim.getSI();
	}


	@Override
	public void prn() {
		
	}




	

}
