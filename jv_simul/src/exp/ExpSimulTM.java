package exp;


import gen.ConfigGen;
import comp.AnalComp;
import comp.CompFile;
import comp.CompMng;
import anal.Anal;
import basic.TaskMng;
import simul.SimulInfo;
import simul.TaskSimul;
import util.Log;
import util.MUtil;

public class ExpSimulTM extends ExpSimul{
	public ExpSimulTM(ConfigGen cfg) {
		super(cfg);
		g_tsim=new TaskSimul[0];
	}
	

	@Override
	public int anal(TaskMng tm, Anal a) {
//		AnalEDF_AT a=new AnalEDF_AT();
		a.init(tm);
		a.prepare();
//		Log.prn(2, ""+a.getDtm());
		boolean b=a.isScheduable();
		return MUtil.btoi(b);
	}
	


	@Override
	public void initSim(int core, TaskSimul tsim) {
		g_tsim[0]=tsim;
	}

	@Override
	protected void simulStart() {
//		g_tsim[0].simulStart();
	}

	@Override
	public void simul(int st, int et) {
		g_tsim[0].init();
		g_tsim[0].checkErr();
		if(st==0){
			g_tsim[0].simulStart();
		}
		g_tsim[0].simulEnd(st,et);
	}


	@Override
	public SimulInfo getSI(int core) {
		return g_tsim[0].getSI();
	}


	@Override
	public void prn() {
		
	}




	
	//comp
	public CompMng loadCM(int i){
		String fn=g_cfg.get_fn(i);
		Log.prn(2, fn);
		CompMng cm=CompFile.loadFile(fn);
		return cm;
	}
	public int analComp(CompMng cm,int kinds) {
		AnalComp a=new AnalComp(cm);
		a.computeX();
		a.part();
		return 	a.anal(kinds);

	}
	
}
