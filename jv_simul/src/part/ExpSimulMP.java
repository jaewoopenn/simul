package part;


import basic.TaskMng;
import basic.TaskSetFix;
import simul.SimulInfo;
import simul.TaskSimul;
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
	
	public void simul(int dur){
		for(int t=0;t<dur;t++){
			for(int j=0;j<g_ncpu;j++){
				g_tsim[j].simulBy(t,t+1);
			}
		}
	}
	
	// get 
	public SimulInfo getSI(int core){
		return g_tsim[core].getSI();
	}
	
	// load 
	public TaskMng loadTM(String fn){
		TaskSetFix tmp=TaskSetFix.loadFile(fn);
		return tmp.getTM();
	}




	public void prn() {
		for(int j=0;j<g_ncpu;j++){
			SimulInfo si=g_tsim[j].getSI();
			si.prn();
		}
		
	}
	

	
}
