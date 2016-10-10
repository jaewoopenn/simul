package exp;


import comp.CompMng;

import anal.Anal;
import anal.AnalComp;
import anal.ConfigGen;
import basic.TaskMng;
import basic.TaskMngPre;
import utilSim.Log;
import utilSim.MUtil;

public class ExpSimul {
	private ConfigGen g_cfg;
	private int g_dur;
	public ExpSimul(ConfigGen cfg) {
		g_cfg=cfg;
	}
	public int size(){
		return g_cfg.readInt("num");
	}
	
	public TaskMng loadTM(int i){
		String fn=g_cfg.get_fn(i);
		TaskMngPre tmp=TaskMngPre.loadFile(fn);
		return tmp.freezeTasks();
	}
	public CompMng loadCM(int i){
		String fn=g_cfg.get_fn(i);
		CompMng cm=CompMng.loadFile(fn);
		return cm;
	}

	
	public int anal(TaskMng tm, Anal a) {
//		AnalEDF_AT a=new AnalEDF_AT();
		a.init(tm);
		a.prepare();
//		Log.prn(2, ""+a.getDtm());
		boolean b=a.isScheduable();
		return MUtil.btoi(b);
	}
	public SimulInfo simul(TaskSimul ts){
		ts.init();
		int ret=ts.simulEnd(0,g_dur);
		if(ret==0){
			Log.prn(9, "error ExpSimul");
			System.exit(1);
		}
		return ts.getSI();
	}

	public void setDuration(int dur ) {
		this.g_dur = dur;
		
	}
	public int analComp(CompMng cm) {
		AnalComp a=new AnalComp(cm);
		a.computeX();
		a.part();
		a.anal();
		
		return 0;
	}
	
}
