package comp;


import basic.TaskGenMC;
import basic.TaskGenParam;
import basic.TaskMng;
import basic.TaskSetFix;
//import utill.Log;

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
			TaskSetFix tmp=new TaskSetFix(tg.getAll());
			TaskMng tm=tmp.getTM();
			c.setTM(tm);
			cm.addComp(c);
			u=cm.getMCUtil();
			if(u>=g_param.u_lb)
				break;
		}
		return cm;
	}
	



	public int check(CompMng cm){
		double u=cm.getMCUtil();
//		Log.prn(1, u+"");
		if(u>=g_param.u_lb&&u<=g_param.u_ub){
			return 1;
		}
		return 0;
	}
	



	
	
}
