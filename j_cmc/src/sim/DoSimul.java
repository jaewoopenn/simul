package sim;

import anal.Anal;
import anal.AnalSel;
import imc.SimulSel_IMC;
import imc.TaskSimul;
import task.DTaskVec;
import util.SLog;

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
		anal.init(dt.getTM(0));
		double x=anal.computeX();

		SysMng sm=new SysMng();
		sm.setMS_Prob(g_prob);
		sm.setX(x);
		
		g_ts.init_sm_dt(sm,dt);

		g_ts.simul(g_dur);
		
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
		return AnalSel.getAnalSim(sort);
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
	
}
