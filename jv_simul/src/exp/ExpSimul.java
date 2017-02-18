package exp;


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

public class ExpSimul {
	private ConfigGen g_cfg;
	public ExpSimul() {
		
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
	
	public SimulInfo simul(TaskSimul tsim,int dur){
		tsim.init();
		tsim.checkErr();
		tsim.simulEnd(0,dur);
		return tsim.getSI();
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
