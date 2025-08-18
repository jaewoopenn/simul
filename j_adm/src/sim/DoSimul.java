package sim;

import anal.Anal;
import anal.AnalSel_IMC;
import imc.SimulSel_IMC;
import imc.TaskSimul_IMC;
import task.DTaskVec;
import util.MList;
import util.SLog;

public class DoSimul {
	private DTaskVec g_dt;
	private Anal g_anal;
	TaskSimul_IMC g_ts;
	public DoSimul(DTaskVec dt,int sort) {
		g_dt=dt;
		g_anal=getAnalSim(sort);
		g_ts=getSim(sort);

	}
	public void run(MList fu) {
		int dur=10000;
		g_anal.init(g_dt.getTM(0));
		double x=g_anal.computeX();
//		SLog.prn(2, x);

		SysMng sm=new SysMng();
		sm.setMS_Prob(0.5);
		sm.setX(x);
		
//		TaskSimul_IMC ts=new TaskSimul_EDF_VD_ADM();
		g_ts.init_sm_dt(sm,g_dt);

		g_ts.simul(dur);
		SimulInfo si=g_ts.getSI();
//		si.prn2();
//		SLog.prn(2, si.getDegraded()+"");
		if(fu!=null)
			fu.add(si.getDegraded()+"");
		
	}

	public Anal getAnalSim(int sort) {
		return AnalSel_IMC.getAnalSim(sort);
	}
	public TaskSimul_IMC getSim(int sort) {
		return SimulSel_IMC.getSim(sort);
	}
	
}
