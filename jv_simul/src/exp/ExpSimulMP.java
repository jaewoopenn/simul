package exp;


import anal.Anal;
import basic.TaskMng;
import basic.TaskSetFix;
import gen.ConfigGen;
import part.CoreMng;
import part.Partition;
import simul.SimulInfo;
import simul.TaskSimul;
import simul.TaskSimul_EDF_VD;
//import util.Log;
import util.MUtil;

public class ExpSimulMP extends ExpSimul {
	private TaskSimul[] g_tsim;
	private int g_ncpu;
	
	public ExpSimulMP(ConfigGen cfg) {
		super(cfg);
	}
	
	public ExpSimulMP() {
		this(null);
	}
	
	public void initCores(int core){
		g_ncpu=core;
		g_tsim=new TaskSimul[core];
	}
	public void loadCM(CoreMng cm, Anal an){
		for(int i:MUtil.loop(cm.size())){
			TaskSetFix tsf=new TaskSetFix(cm.getTS(i));
			TaskMng tm=tsf.getTM();
			an.init(tm);
			an.prepare();
			tm.setX(an.getX());
			initSim(i,new TaskSimul_EDF_VD(tm));
		}
		
	}


	@Override
	public void initSim(int core, TaskSimul tsim) {
		g_tsim[core]=tsim;
		tsim.init();
		tsim.checkErr();
	}
	
	@Override
	protected void simulStart() {
		for(int j:MUtil.loop(g_ncpu)){
			g_tsim[j].simulStart();
		}
	}

	@Override
	public void simul(int st, int et){
		if(st==0){
			simulStart();
		}
		int t=st;
		while(t<et){
//			Log.prn(2, ""+t);
			for(int j:MUtil.loop(g_ncpu)){
				g_tsim[j].simul_t(t);
//				Log.prn(2, t+","+j);
			}
			t++;
		}
	}
	
	@Override
	public SimulInfo getSI(int core){
		return g_tsim[core].getSI();
	}

	
	@Override
	public int anal(TaskMng tm, Anal an) {
		Partition p=new Partition(an,tm.getTaskSet());
		p.anal();
		if(p.size()>g_ncpu)
			return 0;
		return 1;
	}
	
	
	




	public void prn() {
		for(int j=0;j<g_ncpu;j++){
			SimulInfo si=g_tsim[j].getSI();
			si.prn();
		}
		
	}



	// test
	public void move() {
		
	}














	
}
