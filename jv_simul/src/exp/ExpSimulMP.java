package exp;


import anal.Anal;
import basic.TaskMng;
import basic.TaskSet;
import basic.TaskSetFix;
import gen.ConfigGen;
import part.CoreMng;
import part.Partition;
import simul.SimulInfo;
import simul.TaskSimul;
import simul.TaskSimulGen;
import simul.TaskSimul_MP;
import util.Log;
//import util.Log;
import util.MUtil;

public class ExpSimulMP extends ExpSimul {
	private int g_ncpu;
	private CoreMng g_cm;
	
	public ExpSimulMP(ConfigGen cfg) {
		super(cfg);
	}
	
	public ExpSimulMP() {
		this(null);
	}
	
	public void initCores(int core){
		g_ncpu=core;
	}
	public void loadCM(CoreMng cm, Anal an, int simul_no) {
		// 1: EDF-VD
		// 2: EDF-AD-E
		// 3: MP
		g_cm=cm;
		for(int i:MUtil.loop(cm.size())){
			TaskSet ts=cm.getTS(i);
			ts.setCPU(i);
			TaskSetFix tsf=new TaskSetFix(ts);
			TaskMng tm=tsf.getTM();
			an.init(tm);
			an.prepare();
			tm.setX(an.getX());
			tm.set_cm(cm);
			TaskSimul_MP tsim=TaskSimulGen.get_MP(simul_no);
			tsim.setCore(i);
			tsim.init_tm(tm);
//			tm.prn();
			initSim(i,tsim);
		}
		
	}


	@Override
	public void initSim(int core, TaskSimul tsim) {
		g_cm.setSim(core, tsim);
//		g_tsim[core]=tsim;
		tsim.checkErr();
	}
	
	@Override
	protected void simulStart() {
		for(int j:MUtil.loop(g_ncpu)){
			g_cm.getSim(j).simulStart();
		}
	}

	@Override
	public void simul(int st, int et){
		if(st==0){
			simulStart();
			g_cm.getSim(0).isSchTab=false;
//			g_cm.getSim(1).isSchTab=false;
//			g_cm.getSim(0).getTM().prn();
//			g_cm.getSim(1).getTM().prn();
		}
		int t=st;
		while(t<et){
//			Log.prn(2, ""+t);
			for(int j:MUtil.loop(g_ncpu)){
				g_cm.getSim(j).simul_t(t);
//				Log.prn(2, t+","+j);
			}
			t++;
		}
	}
	
	@Override
	public SimulInfo getSI(int core){
		return g_cm.getSim(core).getSI();
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
		for(int j:MUtil.loop(g_ncpu)){
			SimulInfo si=g_cm.getSim(j).getSI();
			si.prn();
		}
		
	}




	public void check() {
		for(int j:MUtil.loop(g_ncpu)){
			if(g_cm.getSim(j)==null){
				Log.prn(9, "null"+j);
			}
		}
	}















	
}
