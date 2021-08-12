package gen;

import task.Comp;
import task.CompSet;
import task.TaskSet;
import util.SLog;

public class ComGen {
	protected ComGenParam gsys_param;
	protected TaskGen g_tg;
	private CompSet g_cs;
	public ComGen(ComGenParam cgp, TaskGen tg) {
		gsys_param=cgp;
		g_tg=tg;
//		tg.prn();
	}
	public void prn() {
		SLog.prn(1, "OKOK");
	}

	public void generate() {
		while(true){
			g_cs=new CompSet();
			genComs();
			if(check()==1) break;
		}
	}
	private void genComs()
	{
		int cid=0;
		double u=0;
		Comp c;
//		SLog.prn(2, "===");
		while(u<gsys_param.u_ub){
			c=genCom(cid,gsys_param.u_ub-u);
			g_cs.add(c);
			cid++;
			u=g_cs.getUtil();
//			SLog.prn(2, ""+u);
			if(u>gsys_param.u_lb) break;
		}
	}
	private Comp genCom(int cid,double ub) {
		Comp c=new Comp(cid,0,0);
		g_tg.setUB(ub);
		g_tg.generate();
		c.setTV(g_tg.getTV());
		return c;
	}
	public int check(){
		return gsys_param.check(g_cs.getUtil());
	}
	
	


	public TaskSet getTS(int i) {
		return g_cs.getTS(i);
	}
	public int getCNum() {
		return g_cs.size();
	}
	
	
	
	
}
