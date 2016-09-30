package Simul;

import Basic.TaskGen;
import Basic.TaskGenMC;
import Basic.TaskMng;
import Util.Log;

public class SimGen {
	private TaskGenMC g_tg;
	private ConfigGen g_cfg;
	public SimGen(ConfigGen cfg) {
		g_tg=new TaskGenMC();
		g_cfg=cfg;
	}
	public int prepare(){
		g_tg=new TaskGenMC();
		g_tg.setUtil(g_cfg.readDbl("u_lb"),g_cfg.readDbl("u_ub"));
		g_tg.setPeriod(g_cfg.readInt("p_lb"),g_cfg.readInt("p_ub"));
		g_tg.setTUtil(g_cfg.readDbl("tu_lb"),g_cfg.readDbl("tu_ub"));
		g_tg.setRatioLH(g_cfg.readDbl("r_lb"),g_cfg.readDbl("r_ub"));
		g_tg.setProbHI(g_cfg.readDbl("prob_hi"));
		return g_cfg.readInt("num");
	}
	public int genSet(int i,boolean b)
	{
		g_tg.generate();
//		Log.prn(2, "util:"+tg.getMCUtil());
		if(b){
			TaskMng tm=new TaskMng();
			tm.setTasks(g_tg.getAll());
			tm.freezeTasks();
			int rs=Analysis.anal_EDF_VD(tm);
			if(rs==0)
				return 0;
		}
		String fn=g_cfg.get_fn(i);
		g_tg.writeFile(fn);
		return 1;
	}
	public void gen() {
		int num=prepare();
//		Log.prn(2, num+"");
		for(int i=0;i<num;i++){
//			Log.prn(2, i+"");
			genSet(i,false);
		}
		
	}
	public void gen2() {
		int num=prepare();
		int i=0;
		while(i<num){
//			Log.prn(2, i+"");
			int rs=genSet(i,true);
			if(rs==1)
				i++;
		}
		
	}
	
	public TaskMng genOne(){
		g_tg.generate();
		TaskMng tm=new TaskMng();
		tm.setTasks(g_tg.getAll());
		tm.freezeTasks();
		return tm;	
	}
	
}
