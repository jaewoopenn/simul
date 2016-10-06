package exp;


import basic.TaskGen;
import basic.TaskMng;
import simul.ConfigGen;
import utilSim.Log;

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
		TaskGen tg=new TaskGen();
		String fn=g_cfg.get_fn(i);
		return tg.loadFileTM(fn);
	}
	public int anal() {
		int num=g_cfg.readInt("num");
		int sum=0;
		
		for(int i=0;i<num;i++){
			TaskMng tm=loadTM(i);
			sum+=anal(tm);
		}
		return sum;
		
	}	
	private int anal(TaskMng tm) {
		// TODO Auto-generated method stub
		return 0;
	}
	public int simul() {
		int num=g_cfg.readInt("num");
		int sum=0;
		
		for(int i=0;i<num;i++){
			TaskMng tm=loadTM(i);
			if(Log.isPrn(2))
				System.out.format("task %d " ,i);
			sum+=simul(tm);
		}
		return sum;
		
	}
	public int simul(TaskMng tm){
		double util=tm.getUtil();
		TaskSimul_EDF_AT_S ts=new TaskSimul_EDF_AT_S(tm);
		int ret=ts.simulEnd(0,g_dur);
		if(Log.isPrn(2))
			System.out.format("util: %.3f ret: %d\n" ,util,ret);
		if(util>1 && ret==1)
			Log.prn(2,"util>1 but sch");
		if(util<=1 && ret==0)
			Log.prn(2,"util<=1 but not sch");
//		Log.prn(2, "task "+i+" util:"+util+" ret:"+ret);
		return ret;
	}

	public void setDuration(int dur ) {
		this.g_dur = dur;
		
	}
	
}
