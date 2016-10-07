package exp;


import basic.TaskGenMC;
import basic.TaskMng;
import simul.Anal;
import simul.ConfigGen;
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
		TaskGenMC tg=new TaskGenMC();
		String fn=g_cfg.get_fn(i);
		return tg.loadFileTM(fn);
	}
	

	
	public int anal(TaskMng tm, Anal a) {
//		AnalEDF_AT a=new AnalEDF_AT();
		a.init(tm);
		a.prepare();
//		double dtm=a.getDtm();
//		Log.prn(2, ""+dtm);
		boolean b=a.isScheduable();
		return MUtil.btoi(b);
	}
	public double simul(TaskSimul ts){
		ts.init();
		int ret=ts.simulEnd(0,g_dur);
		if(ret==0){
			Log.prn(9, "error ExpSimul");
			System.exit(1);
		}
		return ts.getSI().getDMR();
	}

	public void setDuration(int dur ) {
		this.g_dur = dur;
		
	}
	
}
