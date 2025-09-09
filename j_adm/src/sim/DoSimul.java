package sim;

import anal.Anal;
import task.DTUtil;
import task.DTaskVec;
import util.SLog;
import util.SLogF;

public class DoSimul {
	private TaskSimul g_ts;
	private int g_sort;
	private double g_prob=-1;
	private int g_dur=-1;
	public DoSimul(int sort) {
		g_sort=sort;

	}
	public void run(DTaskVec dt) {
		Anal anal=getAnalSim(g_sort);
		g_ts=getSim(g_sort);
		anal.init(DTUtil.getTM(dt,0));
		double x=anal.computeX();
		SLog.prn("x:"+x);
		SysMng sm=new SysMng();
		sm.setMS_Prob(g_prob);
		sm.setX(x);
		
		g_ts.init_sm_dt(sm,dt);

		g_ts.simul(g_dur);
		if(SLogF.isON())
			SLogF.save();
		
	}
	public String getRS() {
		SimulInfo si=g_ts.getStat();
		return si.getDegraded()+"";
	}
	public void prn() {
		SimulInfo si=g_ts.getStat();
		si.prn2();
		SLog.prn(2, si.getDegraded()+"");
		
	}

	public Anal getAnalSim(int sort) {
		return SimulSel_IMC.getAnal(sort);
	}
	
	public TaskSimul getSim(int sort) {
		return SimulSel_IMC.getSim(sort);
	}
	
	public int getSort() {
		return g_sort;
	}
	public void setProb(double d) {
		g_prob=d;
	}
	public void setDur(int i) {
		g_dur=i;
	}
	public void setTrace(String out) {
		SLogF.init(out);
	}
	
}
