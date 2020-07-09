package gen;

import util.MList;
import task.TaskSet;
import task.TaskSetUtil;
import util.CRange;

public  class HSysGen {
	protected ComGen g_cg;
	private ConfigGen g_cfg;
	protected boolean g_isCheck=false;

	public HSysGen(ConfigGen cfg) {
		g_cfg=cfg;
	}
	public void setCheck() {
		g_isCheck=true;
	}
	
	public int prepare(){
		TaskGenParam tgp=new TaskGenParam();
		CRange r=CRange.gen(g_cfg.readDbl("cu_lb"),g_cfg.readDbl("cu_ub"));
		tgp.setUtil(r);
		r=CRange.gen(g_cfg.readInt("p_lb"),g_cfg.readInt("p_ub"));
		tgp.setPeriod(r);
		r=CRange.gen(g_cfg.readDbl("tu_lb"),g_cfg.readDbl("tu_ub"));
		tgp.setTUtil(r);
		TaskGen tg=new TaskGen(tgp);
		ComGenParam cgp=new ComGenParam();
		r=CRange.gen(g_cfg.readDbl("u_lb"),g_cfg.readDbl("u_ub"));
		cgp.setUtil(r);
		g_cg=new ComGen(cgp,tg);
		tgp.prn();
		cgp.prn();
		
		return g_cfg.readInt("num");
	}
	public String gen() {
		int num=prepare();
		int i=0;
		MList fu=new MList();
		fu.add(num+"");
		while(i<num){
//			Log.prn(2, i+"");
			g_cg.generate();
			int rs=check();
			if(rs==0)
				continue;
			writeSys(fu);
//			Log.prn(1, "write");
			
			i++;
		}
		fu.save(g_cfg.get_fn());
		return g_cfg.get_fn();
	}
	
	public int writeSys(MList fu)
	{
		for(int i=0;i<g_cg.getCNum();i++) {
			TaskSet ts=g_cg.getTS(i);
			TaskSetUtil.writeCom(fu, ts.getArr());
		}
		fu.add("------");
		return 1;
	}

	protected  int check() {
		return 1;
	}
	
}
