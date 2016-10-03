package simul;

import basic.TaskGen;
import basic.TaskGenMC;
import basic.TaskMng;
import basic.TaskMngPre;
import utilSim.Log;

public class SimAnal {
	private TaskGenMC g_tg;
	private ConfigGen g_cfg;
	public SimAnal(ConfigGen cfg) {
		g_tg=new TaskGenMC();
		g_cfg=cfg;
	}
	
	public int size(){
		return g_cfg.readInt("num");
	}
	public int load(int anal) {
		int num=g_cfg.readInt("num");
		int sum=0;
		
		for(int i=0;i<num;i++){
			TaskMng tm=load_one(i);
			sum+=process(tm,anal);
		}
		return sum;	
	}
	public int load2(int anal) {
		int num=g_cfg.readInt("num");
		int v=0;
		
		for(int i=0;i<num;i++){
			TaskMng tm=load_one(i);
			v=process(tm,anal);
			Log.prn(2, "v:"+v);
		}
		return 0;	
	}
	
	public double load3(int anal,double p) {
		int num=g_cfg.readInt("num");
		double v=0;
//		num=100;
		for(int i=0;i<num;i++){
			TaskMng tm=load_one(i);
//			int rs=Analysis.analEDF_VD(tm);
//			if(rs==0){
//				Log.err("err");
//			}
			v+=process2(tm,anal,p);
			Log.prn(1, "v:"+v);
		}
		return v/num;	
	}

	
	
	public TaskMng load_one(int i){
		TaskGenMC tg=new TaskGenMC();
		tg.setUtil(g_cfg.readDbl("u_lb"),g_cfg.readDbl("u_ub"));
		String fn=g_cfg.get_fn(i);
		tg.loadFile(fn);
		if(tg.check()==0){
			Log.prn(2, "err "+i);
			tg.prn(2);
			return null;
		}
		TaskMngPre tm=new TaskMngPre();
		tm.setTasks(tg.getAll());
		TaskMng m=tm.freezeTasks();
		m.sort();
		return m;
	}
	
	public int process(TaskMng tm, int anal) {
//		double util=tm.getInfo().getMCUtil();
//		System.out.format("task set %d MC util: %.3f\n" ,i,util);
		switch(anal)
		{
		case 0:
			return Analysis.anal_EDF(tm);
		case 1:
			return Analysis.anal_EDF_VD(tm);
		case 2:
			return Analysis.anal_EDF_TM(tm);
		case 3:
			return Analysis.anal_ICG(tm);
		case 4:
			return Analysis.anal_EDF_TM_S(tm);

//		case 3:
//			return Analysis.getRespEDF(tm);
//		case 4:
//			return Analysis.getRespEDF_VD(tm);
//		case 5:
//			return Analysis.getRespEDF_TM(tm);
		default:
			Log.prn(2,"anal ID check");
		}
		return -1;
	}
	public double process2(TaskMng tm, int anal,double p) {
		switch(anal)
		{
		case 6:
			return Analysis.getDrop_EDF_VD(tm,p);
		case 7:
			return Analysis.getDrop_EDF_TM_E(tm,p);
		default:
			Log.prn(2,"anal ID check");
		}
		return -1;
	}

	
}
