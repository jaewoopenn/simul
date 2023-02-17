package exp;


import gen.ConfigGen;
import sim.SimulInfo;
import sim.TaskSimul;
import sim.mc.TaskSimulMC;
import task.TaskMng;
import comp.AnalComp;
import comp.CompFile;
import comp.CompMng;
import anal.Anal;
import anal.AnalEDF_AD_E;
import util.SLog;
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
	


	@Override
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




	
	//comp
	public CompMng loadCM(int i){
		String fn=g_cfg.get_fn(i);
		SLog.prn(2, fn);
		CompMng cm=CompFile.loadFile(fn);
		cm.prn();
		return cm;
	}
	public int analComp(CompMng cm,int kinds) {
		AnalComp a=new AnalComp(cm);
		a.computeX();
		a.part();
		return 	a.anal(kinds);

	}


}
