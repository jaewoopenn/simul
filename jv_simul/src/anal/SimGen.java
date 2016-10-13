package anal;

import utilSim.MUtil;
import basic.TaskGenMC;
import basic.TaskGenParam;
import basic.TaskMng;
import basic.TaskMngPre;

public class SimGen {
	private TaskGenMC g_tg;
	private ConfigGen g_cfg;
	private boolean g_isCheck=false;
	public SimGen(ConfigGen cfg) {
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
	public void gen() {
		int num=prepare();
		int i=0;
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
	public void gen2(){
		g_isCheck=true;
		gen();
	}
	
	public int writeSys(int i)
	{
		String fn=g_cfg.get_fn(i);
		g_tg.writeFile(fn);
		return 1;
	}

	private int check() {
		TaskMngPre tm=new TaskMngPre();
		tm.setTasks(g_tg.getAll());
		TaskMng m=tm.freezeTasks();
		if(tm.g_info.getLo_size()==0) return 0;
		if(tm.g_info.getHi_size()==0) return 0;
		if(!g_isCheck)
			return 1;
		AnalEDF_VD a=new AnalEDF_VD();
		a.init(m);
		a.prepare();
		
		return MUtil.btoi(a.isScheduable());
	}
	public TaskMng genOne(){
		g_tg.generate();
		TaskMngPre tm=new TaskMngPre();
		tm.setTasks(g_tg.getAll());
		TaskMng m=tm.freezeTasks();
		return m;
	}
	
}
