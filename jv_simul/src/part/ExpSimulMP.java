package part;


import anal.Anal;
import basic.TaskMng;
import gen.ConfigGen;
import simul.SimulInfo;
import simul.TaskSimul;
import util.Log;
//import util.Log;
import util.MUtil;

public class ExpSimulMP {
	private TaskSimul[] g_tsim;
	private int g_ncpu;
	private ConfigGen g_cfg;
	
	public ExpSimulMP(int core, ConfigGen cfg) {
		this(core);
		g_cfg=cfg;
	}
	

	
	
	public ExpSimulMP(int core) {
		g_ncpu=core;
		g_tsim=new TaskSimul[core];
	}


	public int size(){
		return g_cfg.readInt("num");
	}


	public void init(int core,TaskSimul tsim){
		g_tsim[core]=tsim;
		tsim.init();
		tsim.checkErr();
	}
	public void simulStart(){
		for(int j:MUtil.loop(g_ncpu)){
			g_tsim[j].simulStart();
		}
		
	}
	public void simul(int st, int et){
		int t=st;
		while(t<et){
//			Log.prn(2, ""+t);
			for(int j:MUtil.loop(g_ncpu)){
				g_tsim[j].simul_t(t);
				Log.prn(2, t+","+j);
			}
			t++;
		}
	}
	
	// get 
	public SimulInfo getSI(int core){
		return g_tsim[core].getSI();
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




	public int anal(TaskMng tm, Anal an) {
		Partition p=new Partition(an,tm.getTaskSet());
		p.anal();
		if(p.size()>g_ncpu)
			return 0;
		return 1;
	}
	

	
}
