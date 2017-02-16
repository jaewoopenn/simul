package part;


import gen.ConfigGen;
import comp.AnalComp;
import comp.CompFile;
import comp.CompMng;
import anal.Anal;
import basic.TaskMng;
import basic.TaskSetFix;
import simul.SimulInfo;
import simul.TaskSimul;
import util.Log;
import util.MUtil;

public class ExpSimulMP {
	private ConfigGen g_cfg;
	private TaskSimul[] g_tsim;
	private int g_ncpu;
	public ExpSimulMP(int core) {
		g_ncpu=core;
		g_tsim=new TaskSimul[core];
	}
	public void setCfg(ConfigGen cfg) {
		g_cfg=cfg;
	}
	public int size(){
		return g_cfg.readInt("num");
	}
	

	
	public int anal(TaskMng tm, Anal a) {
//		AnalEDF_AT a=new AnalEDF_AT();
		a.init(tm);
		a.prepare();
//		Log.prn(2, ""+a.getDtm());
		boolean b=a.isScheduable();
		return MUtil.btoi(b);
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
	public SimulInfo getSI(int core){
		return g_tsim[core].getSI();
	}
	// load 
	public TaskMng loadTM(int i){
		String fn=g_cfg.get_fn(i);
		return loadTM(fn);
	}
	public TaskMng loadTM(String fn){
		TaskSetFix tmp=TaskSetFix.loadFile(fn);
		return tmp.getTM();
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
