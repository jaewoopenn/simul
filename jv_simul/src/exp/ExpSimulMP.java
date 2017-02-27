package exp;


import anal.Anal;
import basic.SysInfo;
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
	public void loadCM(CoreMng cm, Anal an, int simul_no, SysMng sm) {
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
			if(an.getDtm()>1){
				Log.prn(9, "DTM:"+an.getDtm());
				System.exit(1);
			}
			tm.setX(an.computeX());
//			Log.prn(2, "lo_max:"+an.getExtra(0));
			tm.setLo_max(an.getExtra(0));
			tm.set_cm(cm);
			TaskSimul_MP tsim=TaskSimulGen.get_MP(simul_no);
			tsim.setCore(i);
			tsim.init_sm(sm);
			tsim.init_tm(tm);
//			tm.prn();
			initSim(i,tsim);
			cm.set_tm(i,tm);
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
//		g_cm.getSim(0).isSchTab=false;
//		g_cm.getSim(0).isPrnMS=false;
		g_cm.getSim(1).isSchTab=false;
		g_cm.getSim(1).isPrnMS=false;
	}

	@Override
	public void simul(int st, int et){
		if(st==0){
			simulStart();
		}
		int t=st;
		while(t<et){
			simul_t(t);
			t++;
		}
	}
	
	private void simul_t(int t) {
		for(int j:MUtil.loop(g_ncpu)){
			g_cm.getSim(j).simul_t(t);
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
		for(int i:MUtil.loop(g_ncpu)){
			SimulInfo si=g_cm.getSim(i).getSI();
			si.prn();
		}
		
	}
	public void prnTasks(){
		Log.prn(2, "cpus:"+g_ncpu);
		for(int i:MUtil.loop(g_ncpu)){
			Log.prn(2, "core "+i);
			
//			g_cm.getTM(i).getTaskSet().prnPara();
			
			g_cm.getTM(i).prnOffline();
		}
		
	}



	public void check() {
		for(int i:MUtil.loop(g_ncpu)){
			if(g_cm.getSim(i)==null){
				Log.prn(9, "simul null"+i);
				System.exit(1);
			}
			if(g_cm.getTM(i)==null){
				Log.prn(9, "tm null"+i);
				System.exit(1);
			}
			SysInfo info =g_cm.getTM(i).getInfo();
			if(info.getLo_max()+MUtil.err<info.getLo_util()){
				Log.prn(9, "core "+i+": lo max "+info.getLo_max()+" < lo util "+info.getLo_util());
				System.exit(1);
				
			}
		}
		Log.prn(2, "check OK");
	}
	public boolean checkTasks(){
		for(int i:MUtil.loop(g_ncpu)){
			if(!g_cm.getTM(i).check()){
				Log.prn(2, "cpu "+i+" error");
				return false;
			}
		}
		return true;
	}
}
