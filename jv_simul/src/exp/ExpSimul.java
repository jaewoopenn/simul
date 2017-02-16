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
	private int g_dur;
	public ExpSimul(ConfigGen cfg) {
		g_cfg=cfg;
	}
	public int size(){
		return g_cfg.readInt("num");
	}
	
	public TaskMng loadTM(int i){
		String fn=g_cfg.get_fn(i);
		TaskSetFix tmp=TaskSetFix.loadFile(fn);
		return tmp.getTM();
	}
	public CompMng loadCM(int i){
		String fn=g_cfg.get_fn(i);
		Log.prn(2, fn);
		CompMng cm=CompFile.loadFile(fn);
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
		ts.checkErr();
		int ret=ts.simulEnd(0,g_dur);
		if(ret==0){
			Log.prn(9, "ERROR: ExpSimul");
			System.exit(1);
		}
		return ts.getSI();
	}

	public void setDuration(int dur ) {
		this.g_dur = dur;
		
	}
	public int analComp(CompMng cm,int kinds) {
		AnalComp a=new AnalComp(cm);
		a.computeX();
		a.part();
		return 	a.anal(kinds);

	}
	public void prnInfo() {
//		Log.prn(2, "g_prob"+g_prob);
		
	}

	
}
