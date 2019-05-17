package gen;

import basic.TaskSet;
import basic.TaskSetEx;
import util.MList;
import util.CRange;

public  class SysGen {
	protected TaskGen g_tg;
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
		CRange r=CRange.gen(g_cfg.readDbl("u_lb"),g_cfg.readDbl("u_ub"));
		tgp.setUtil(r);
		r=CRange.gen(g_cfg.readInt("p_lb"),g_cfg.readInt("p_ub"));
		tgp.setPeriod(r);
		r=CRange.gen(g_cfg.readDbl("tu_lb"),g_cfg.readDbl("tu_ub"));
		tgp.setTUtil(r);
		g_tg=new TaskGen(tgp);
		return g_cfg.readInt("num");
	}
	public void gen(String fn) {
		int num=prepare();
		int i=0;
//		String fn=g_cfg.get_dir();
//		Log.prn(2, fn);
//		FUtil.makeDir(fn);
		MList fu=new MList();
		fu.add(num+"");
		while(i<num){
//			Log.prn(2, i+"");
			g_tg.generate();
			int rs=check();
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
		TaskSetEx.writeTS(fu, ts.getArr());
		
		return 1;
	}

	protected  int check() {
		return 1;
	}
	
}
