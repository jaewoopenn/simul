package exp;


import basic.TaskGenMC;
import basic.TaskMng;
import basic.TaskMngPre;
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
		TaskMngPre tmp=new TaskMngPre();
		String fn=g_cfg.get_fn(i);
		tmp.loadFile(fn);
		return tmp.freezeTasks();
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
	
}
