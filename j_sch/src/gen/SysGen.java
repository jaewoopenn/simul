package gen;

import anal.Anal;
import task.TaskSet;
import task.TaskSetUtil;
import util.MList;

public abstract class SysGen {
	protected TaskGen g_tg;
	private ConfigGen g_cfg;
	protected boolean g_isCheck=false;

	public SysGen(ConfigGen cfg) {
		g_cfg=cfg;
	}
	public void setCheck() {
		g_isCheck=true;
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
	public int prepareIMC(){
		TaskGenParam tgp=prepare_in();
		g_tg=new TaskGenIMC(tgp);
		return g_cfg.readInt("num");
	}	
	public void gen(String fn,Anal a,int num) {
		int i=0;
//		String fn=g_cfg.get_dir();
//		Log.prn(2, fn);
//		FUtil.makeDir(fn);
		MList fu=new MList();
		fu.add(num+"");
		while(i<num){
//			Log.prn(2, i+"");
			g_tg.generate();
			int rs=check(a);
			if(rs==0)
				continue;
			writeSys(fu);
//			Log.prn(1, "write");
			
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

	protected abstract int check(Anal a) ;
	
}
