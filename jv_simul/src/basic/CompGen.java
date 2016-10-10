package basic;


import comp.Comp;
import comp.CompMng;

public class CompGen {
	protected CompGenParam g_param;
	protected TaskGenParam g_t_param;
	public CompGen(CompGenParam c, TaskGenParam p) {
		g_param=c;
		g_t_param=p;
	}

	public CompMng generate() {
		g_t_param.setUtil(g_param.c_lb,g_param.c_ub);
		TaskGenMC tg=new TaskGenMC(g_t_param);
		CompMng cm=new CompMng();
		while(true){
			tg.generate();
			Comp c=new Comp(g_param.alpha);
			TaskMngPre tmp=new TaskMngPre(tg.getAll());
			TaskMng tm=tmp.freezeTasks();
			c.setTM(tm);
			cm.addComp(c);
			double u=cm.getMCUtil();
			if(u>=g_param.u_lb)
				break;
		}
		return cm;
	}
	



	public int check(CompMng cm){
		double u=cm.getMCUtil();
		if(u>=g_param.u_lb&&u<=g_param.u_ub){
			return 1;
		}
		return 0;
	}
	



	
	
}
