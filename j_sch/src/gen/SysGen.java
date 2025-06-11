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
	
	private TaskGenParam getTGP() {
		TaskGenParam tgp=new TaskGenParam();
		tgp.setUtil(g_cfg);
		tgp.setPeriod(g_cfg);
		tgp.setTUtil(g_cfg);
		tgp.setRatioLH(g_cfg);
		tgp.setMoLH(g_cfg);
		tgp.setProbHI(g_cfg.readDbl("prob_hi"));
		return tgp;
	}
	public int prepare_MC(){
		TaskGenParam tgp=getTGP();
		g_tg=new TaskGenMC(tgp);
		return g_cfg.readInt("num");
	}
	public int prepare_IMC(){
		TaskGenParam tgp=getTGP();
		g_tg=new TaskGenIMC(tgp);
		return g_cfg.readInt("num");
	}	
	public void gen(String fn,Anal a,int num) {
		int i=0;
		MList fu=new MList();
		fu.add(num+"");
		while(i<num){
			g_tg.genTS();
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
