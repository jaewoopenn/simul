package simul;

import basic.TaskGenMC;
import basic.TaskMng;
import basic.TaskMngPre;

public class SimGen {
	private TaskGenMC g_tg;
	private ConfigGen g_cfg;
	private boolean g_isCheck=false;
	public SimGen(ConfigGen cfg) {
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
	public void setCheck(boolean g_isCheck) {
		this.g_isCheck = g_isCheck;
	}
	public void gen() {
		int num=prepare();
		int i=0;
		while(i<num){
//			Log.prn(2, i+"");
			g_tg.generate();
			int rs=check();
			if(rs==0)
				continue;
			writeSys(i);
			i++;
		}
		
	}
	public void gen2(){
		setCheck(true);
		gen();
	}
	
	public int writeSys(int i)
	{
		String fn=g_cfg.get_fn(i);
		g_tg.writeFile(fn);
		return 1;
	}

	private int check() {
		TaskMngPre tm=new TaskMngPre();
		tm.setTasks(g_tg.getAll());
		TaskMng m=tm.freezeTasks();
//		m.prn();
		if(tm.g_info.getLo_size()==0) return 0;
		if(tm.g_info.getHi_size()==0) return 0;
		if(!g_isCheck)
			return 1;
		return Analysis.anal_EDF_VD(m);
	}
	public TaskMng genOne(){
		g_tg.generate();
		TaskMngPre tm=new TaskMngPre();
		tm.setTasks(g_tg.getAll());
		TaskMng m=tm.freezeTasks();
		return m;
	}
	
}
