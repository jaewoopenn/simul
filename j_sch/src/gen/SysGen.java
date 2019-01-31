package gen;

import basic.TaskSet;
import basic.TaskSetFile;
import util.FOut;
import util.Log;

public abstract class SysGen {
	protected TaskGenMC g_tg;
	private ConfigGen g_cfg;
	protected boolean g_isCheck=false;

	public SysGen(ConfigGen cfg) {
		g_cfg=cfg;
	}
	public void setCheck() {
		g_isCheck=true;
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
	public void gen(String fn) {
		int num=prepare();
		int i=0;
//		String fn=g_cfg.get_dir();
//		Log.prn(2, fn);
//		FUtil.makeDir(fn);
		FOut fu=new FOut(fn);
		while(i<num){
//			Log.prn(2, i+"");
			g_tg.generate();
			int rs=check();
			if(rs==0)
				continue;
			writeSys(fu);
			Log.prn(1, "write");
			
			i++;
		}
		fu.save();
	}
	
	public int writeSys(FOut fu)
	{
		
		TaskSet ts=new TaskSet(g_tg.getAll());
		ts.transform_Array();
		TaskSetFile.writeTS(fu, ts);
		
		return 1;
	}

	protected abstract int check() ;
	
}
