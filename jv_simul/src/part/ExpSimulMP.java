package part;


import simul.SimulInfo;
import simul.TaskSimul;
import util.Log;
import util.MUtil;
//import util.Log;

public class ExpSimulMP {
	private TaskSimul[] g_tsim;
	private int g_ncpu;
	public ExpSimulMP(int core) {
		g_ncpu=core;
		g_tsim=new TaskSimul[core];
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
	

	
}
