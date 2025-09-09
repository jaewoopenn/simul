package gen;

import anal.Anal;
import task.Task;
import task.TaskMng;
import task.TaskSet;
import task.TaskSetUtil;
import util.MList;
import util.MRand;
import util.SLog;

@SuppressWarnings("unused")
public class SysGen {
	private MRand g_rand=new MRand();
	private TaskGen g_tg;
	private ConfigGen g_cfg;
	private boolean g_isSch=false;
	private boolean g_isOnlyMC=false;
	private int g_stage=1;
	private int g_num=0;
	public void load_in(ConfigGen cfg) {
		g_cfg=cfg;
		TaskGenParam tgp=getTgp();
		g_tg=new TaskGen(tgp);
		g_num=g_cfg.readInt("num");
		
	}
	public int getNum() {
		return g_num;
	}
	public void setSch() {
		g_isSch=true;
	}
	public void setOnlyMC() {
		g_isOnlyMC=true;
	}
	public void setStage(int n) {
		g_stage=n;
	}
	
	private TaskGenParam getTgp() {
		TaskGenParam tgp=new TaskGenParam();
		tgp.setUtil(g_cfg);
		tgp.setPeriod(g_cfg);
		tgp.setTUtil(g_cfg);
		tgp.setRatioLH(g_cfg);
		tgp.setMoLH(g_cfg);
		tgp.setProbHI(g_cfg.readDbl("prob_hi"));
		return tgp;
	}
	public void gen(String fn,Anal a,int num) {
		int i=0;
		MList ml=MList.new_list();
		ml.add(num+"");
		while(i<num){
			g_tg.genTS();
			if(!checkOnlyMC())
				continue;
			if(!isSch(a)) 
				continue;
//			SLog.prnc(1, i+" ");
			writeSys(ml);
//			SLog.prn(2,i+"");
			i++;
		}
		ml.saveTo(fn);
//		SLog.prn(2,fn+"");
	}

	
	public int writeSys(MList ml)
	{
		
		TaskSet ts=g_tg.getTS();

//		TaskMng tm=ts.getTM();
//		tm.prnInfo();

		TaskSetUtil.initStage(ml, g_stage);
		Task[] tss=ts.getArr();
		for(Task t:tss) {
			TaskSetUtil.writeTask(ml, t);
		}
		int num=tss.length;
		for(int i=1;i<g_stage;i++) {
			TaskSetUtil.nextStage(ml,i);
			boolean isAdd=g_rand.getBool();
			if(isAdd) { //add
				Task t=g_tg.genTaskOne();
				TaskSetUtil.writeTask(ml, t);
				num++;
			} else {
				int remove_n=g_rand.getInt(num);
				TaskSetUtil.remove(ml,remove_n);
				num--;
			}
		}
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
		if(!g_isSch)
			return true;
		TaskSet tsf=g_tg.getTS();
		TaskMng tm=tsf.getTM();
		a.init(tm);
		return a.is_sch(); 
	}

	public static SysGen load(ConfigGen cfg) {
		SysGen s=new SysGen();
		s.load_in(cfg);
		return s;
	}

	
}
