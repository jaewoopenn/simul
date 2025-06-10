package gen;

import anal.Anal;
import task.Task;
import task.TaskMng;
import task.TaskSet;
import task.TaskSetUtil;
import util.MList;

public class SysGen {
	protected TaskGen g_tg;
	private ConfigGen g_cfg;
	protected boolean g_isCheck=false;
	protected boolean g_isOnlyMC=false;

	public SysGen(ConfigGen cfg) {
		g_cfg=cfg;
	}
	public void setCheck() {
		g_isCheck=true;
	}
	public void setOnlyMC() {
		g_isOnlyMC=true;
	}
	
	private TaskGenParam prepare_in() {
		TaskGenParam tgp=new TaskGenParam();
		tgp.setUtil(g_cfg.readDbl("u_lb"),g_cfg.readDbl("u_ub"));
		tgp.setPeriod(g_cfg.readInt("p_lb"),g_cfg.readInt("p_ub"));
		tgp.setTUtil(g_cfg.readDbl("tu_lb"),g_cfg.readDbl("tu_ub"));
		tgp.setRatioLH(g_cfg.readDbl("r_lb"),g_cfg.readDbl("r_ub"));
		tgp.setMoLH(g_cfg.readDbl("mo_lb"),g_cfg.readDbl("mo_ub"));
		tgp.setProbHI(g_cfg.readDbl("prob_hi"));
		return tgp;
	}
	public int prepare(){
		TaskGenParam tgp=prepare_in();
		g_tg=new TaskGenMC(tgp);
		return g_cfg.readInt("num");
	}
	public int prepareIMC(){
		TaskGenParam tgp=prepare_in();
		g_tg=new TaskGenIMC(tgp);
		return g_cfg.readInt("num");
	}	
	public void gen(String fn,Anal a,int num) {
		int i=0;
		MList ml=new MList();
		ml.add(num+"");
		while(i<num){
			g_tg.genTS();
			if(!checkOnlyMC())
				continue;
			if(!isSch(a)) 
				continue;
			writeSys(ml);
//			SLog.prn(2,i+"");
			i++;
		}
		ml.saveTo(fn);
	}
	public void gen2(String fn,Anal a,int num) {
		int i=0;
		MList ml=new MList();
		ml.add(num+"");
		while(i<num){
			g_tg.genTS();
			if(!checkOnlyMC())
				continue;
			if(!isSch(a)) 
				continue;
			writeSys2(ml);
//			SLog.prn(2,i+"");
			i++;
		}
		ml.saveTo(fn);
	}

	
	public int writeSys(MList ml)
	{
		
		TaskSet ts=g_tg.getTS();
		TaskSetUtil.writeTS(ml, ts.getArr());
		
		return 1;
	}
	public int writeSys2(MList ml)
	{
		
		TaskSet ts=g_tg.getTS();
		TaskSetUtil.initStage(ml, 2);
		for(Task t:ts.getArr()) {
			TaskSetUtil.writeTask(ml, t);
		}
		TaskSetUtil.nextStage(ml);
		TaskSetUtil.remove(ml,0);
		TaskSetUtil.writeTask(ml, new Task(40,1,2));
		ml.add("------");
		return 1;
	}
	
	protected boolean checkOnlyMC() {
		if(!g_isOnlyMC)
			return true;
		TaskSet tsf=g_tg.getTS();
		TaskMng tm=tsf.getTM();
		if(tm.getMaxUtil()<=1) 
			return false;
		return true;
	}

	protected boolean isSch(Anal a) {
		if(!g_isCheck)
			return true;
		TaskSet tsf=g_tg.getTS();
		TaskMng tm=tsf.getTM();
		a.init(tm);
		return a.is_sch(); 
	}

	
}
