package gen;

import basic.TaskMng;
import basic.TaskSet;
import basic.TaskSetFile;
import util.FUtil;
import util.Log;

public abstract class SysGen {
	protected TaskGenMC g_tg;
	private ConfigGen g_cfg;
	protected boolean g_isCheck=false;

	public SysGen(ConfigGen cfg) {
		g_cfg=cfg;
	}
	public int prepare(){
		TaskGenParam tgp=new TaskGenParam();
		tgp.setUtil(g_cfg.readDbl("u_lb"),g_cfg.readDbl("u_ub"));
		tgp.setPeriod(g_cfg.readInt("p_lb"),g_cfg.readInt("p_ub"));
		tgp.setTUtil(g_cfg.readDbl("tu_lb"),g_cfg.readDbl("tu_ub"));
		tgp.setRatioLH(g_cfg.readDbl("r_lb"),g_cfg.readDbl("r_ub"));
		tgp.setProbHI(g_cfg.readDbl("prob_hi"));
		g_tg=new TaskGenMC(tgp);
		return g_cfg.readInt("num");
	}
	public String get_fn() {
		return g_cfg.get_fn();
		
	}
	public String get_mod() {
		return g_cfg.get_mod();
	}
	public void gen(String fn) {
		int num=prepare();
		int i=0;
//		String fn=g_cfg.get_dir();
//		Log.prn(2, fn);
//		FUtil.makeDir(fn);
		FUtil fu=new FUtil(fn);
		while(i<num){
//			Log.prn(2, i+"");
			g_tg.generate();
			int rs=check();
			if(rs==0)
				continue;
			writeSys(fu);
			i++;
		}
		fu.save();
		
	}
	
	public int writeSys(FUtil fu)
	{
		
		TaskSet ts=new TaskSet(g_tg.getAll());
		ts.transform_Array();
		TaskSetFile.writeTS(fu, ts);
		
		return 1;
	}

	protected abstract int check() ;
	public TaskMng genOne(){
		g_tg.generate();
		TaskSetFile tm=new TaskSetFile(g_tg.getAll());
		TaskMng m=tm.getTM();
		return m;
	}
	public void setCheck(boolean b){
		g_isCheck=b;
	}
	
}
