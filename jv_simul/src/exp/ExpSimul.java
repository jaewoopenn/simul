package exp;


import basic.TaskGen;
import basic.TaskMng;
import simul.ConfigGen;
import utilSim.Log;

public class ExpSimul {
	private ConfigGen g_cfg;
	
	public ExpSimul(ConfigGen cfg) {
		g_cfg=cfg;
	}
	public int size(){
		return g_cfg.readInt("num");
	}
	public int load(int dur) {
		int num=g_cfg.readInt("num");
		int sum=0;
		
		for(int i=0;i<num;i++){
			TaskGen tg=new TaskGen();
			String fn=g_cfg.get_fn(i);
			TaskMng tm=tg.loadFileTM(fn);
			double util=tg.getUtil();
			TaskSimul ts=new TaskSimul(tm);
			int ret=ts.exec(dur);
			sum+=ret;
			if(Log.isPrn(2))
				System.out.format("task %d util: %.3f ret: %d\n" ,i,util,ret);
			if(util>1 && ret==1)
				Log.prn(2,"util>1 but sch");
			if(util<=1 && ret==0)
				Log.prn(2,"util<=1 but not sch");
//			Log.prn(2, "task "+i+" util:"+util+" ret:"+ret);
		}
		return sum;
		
	}

}
