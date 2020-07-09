package gen;

import java.util.Vector;

import task.Comp;
import task.TaskSet;
import task.TaskVec;
import util.SLog;

public class ComGen {
	protected ComGenParam g_param;
	protected TaskGen g_tg;
	private Vector<Comp> g_com;
	public ComGen(ComGenParam cgp, TaskGen tg) {
		g_param=cgp;
		g_tg=tg;
//		tg.prn();
	}
	public void prn() {
		SLog.prn(1, "OKOK");
	}

	public void generate() {
		while(true){
			g_com=new Vector<Comp>();
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
		while(u<g_param.u_ub){
			c=genCom(cid,g_param.u_ub-u);
			g_com.add(c);
			cid++;
			u=getUtil();
			SLog.prn(2, ""+u);
			if(u>g_param.u_lb) break;
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
		return g_param.check(getUtil());
	}
	
	


	protected  double getUtil() {
		double util=0;
		for(Comp c:g_com){
			util+=c.getUtil();
		}
//		SLog.prn(1, util+"");
		return util;
	}
	public TaskSet getTS(int i) {
		return g_com.get(i).getTS();
	}
	public int getCNum() {
		return g_com.size();
	}
	
	
	
	
}
