package gen;

import anal.Anal;
import basic.TaskMng;
import basic.TaskSetFix;
import util.FUtil;
import util.Log;

public abstract class SimGen {
	protected TaskGenMC g_tg;
	protected Anal g_anal;
	private ConfigGen g_cfg;
	protected boolean g_isCheck=false;

	public SimGen(ConfigGen cfg,Anal an) {
		g_cfg=cfg;
		g_anal=an;
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
	public void gen() {
		int num=prepare();
		int i=0;
		String fn=g_cfg.get_dir();
		Log.prn(2, fn);
		FUtil.makeDir(fn);
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
	
	public int writeSys(int i)
	{
		String fn=g_cfg.get_fn(i);
		g_tg.writeFile(fn);
		return 1;
	}

	protected abstract int check() ;
	public TaskMng genOne(){
		g_tg.generate();
		TaskSetFix tm=new TaskSetFix(g_tg.getAll());
		TaskMng m=tm.getTM();
		return m;
	}
	public void setCheck(boolean b){
		g_isCheck=b;
	}
	
}
