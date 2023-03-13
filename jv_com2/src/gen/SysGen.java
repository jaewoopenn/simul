package gen;

import anal.Anal;
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
		tgp.setProbHI(g_cfg.readDbl("prob_hi"));
		return tgp;
	}
	public int prepare(){
		TaskGenParam tgp=prepare_in();
		g_tg=new TaskGenMC(tgp);
		return g_cfg.readInt("num");
	}
	public void gen(String fn,Anal a,int num) {
		int i=0;
		MList fu=new MList();
		fu.add(num+"");
		while(i<num){
			g_tg.generate();
			if(!isOK())
				continue;
			if(!isSch(a)) 
				continue;
			writeSys(fu);
//			SLog.prn(2,i+"");
			i++;
		}
		fu.save(fn);
	}
	
	public int writeSys(MList fu)
	{
		
		TaskSet ts=g_tg.getTS();
		TaskSetUtil.writeTS(fu, ts.getArr());
		
		return 1;
	}
	protected boolean isOK() {
		if(!g_isOnlyMC)
			return true;
		TaskMng tm=g_tg.getTS().getTM();
//		tm.prnInfo();
		if(tm.getMaxUtil()<=1) 
			return false;
//		SLog.prn(2,"OK");
//		tm.prnInfo();
		return true;
	}

	protected boolean isSch(Anal a) {
		if(!g_isCheck)
			return true;
		TaskMng tm=g_tg.getTS().getTM();
		a.init(tm);
//		tm.prnInfo();
		if(!a.is_sch()) {		
//			SLog.prn(2,"Not OK");
			return false;
		}
//		SLog.prn(2,"OK");
		return true;
	}

	
}
