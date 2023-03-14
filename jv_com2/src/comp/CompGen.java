package comp;


import gen.TaskGenMC;
import gen.TaskGenParam;
import util.SLog;

public class CompGen {
	protected CompGenParam g_param;
	protected TaskGenParam g_t_param;
	public CompGen(CompGenParam c, TaskGenParam p) {
		g_param=c;
		g_t_param=p;
	}

	public CompMng generate() {
		TaskGenMC tg=new TaskGenMC(g_t_param);
		CompMng cm=new CompMng();
		double u=0;
		while(true){
			if(u+g_param.c_ub>g_param.u_ub)
				g_t_param.setUtil(0,g_param.u_ub-u);
			else
				g_t_param.setUtil(g_param.c_lb,g_param.c_ub);
			tg.generate();
			Comp c=new Comp(g_param.getAlpha());
			c.setTM(tg.getTS().getTM());
			cm.addComp(c);
			u=cm.getMCUtil();
			if(u>=g_param.u_lb)
				break;
		}
		return cm;
	}
	



	public int check(CompMng cm){
		double u=cm.getMCUtil();
		SLog.prn(1, u+"");
		if(u<g_param.u_lb||u>g_param.u_ub){
			return 0;
		}
		if(cm.getLoUtil()==0)
			return 0;
//		if(cm.getHcUtil()==0)
//			return 0;
		return 1;
	}
	



	
	
}
