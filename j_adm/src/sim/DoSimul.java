package sim;

import anal.Anal;
import task.DTUtil;
import task.DTaskVec;
import task.TaskMng;
import task.TaskUtil;
import util.MCal;
import util.SLog;
import util.SLogF;

public class DoSimul {
	private TaskSimul g_ts;
	private int g_sort;
	private double g_prob=-1;
	private int g_dur=-1;
	public DoSimul(int sort, int dur,double prob) {
		g_sort=sort;
		g_dur=dur;
		g_prob=prob;

	}
	
	public void run(DTaskVec dt) {
		Anal anal=getAnalSim(g_sort);
		TaskMng tm=DTUtil.getCurTM(dt);
		anal.init(tm);
		anal.auto();
		double dtm=anal.getDtm();
		double x=anal.getX();

		if(x<=0||x>1) {
			TaskUtil.prn(tm);
			SLog.prn("x: "+x);
			SLog.err("not sch X");
		}
		if(dtm>1+MCal.err) {
			TaskUtil.prn(tm);
//			TaskUtil.prnUtil(tm);
			SLog.prn(2,"x: "+x);
			SLog.prn(2,"dtm: "+dtm);
			SLog.err("not sch DTM");
		}
		SLog.prnc(1,"x: "+x);
		SLog.prn(", dtm: "+dtm);
		SysMng sm=new SysMng();
		sm.setMS_Prob(g_prob);
		
		g_ts=getTaskSim(g_sort);
		tm.setX(x);
		g_ts.init_sm_dt(sm,x,dt);
//		TaskUtil.prnUtil(tm);
//		TaskUtil.prnDetail(tm);
		g_ts.simul(g_dur);
		
		if(SLogF.isON())
			SLogF.save();
		
	}
	public void run(DTaskVec dt, double x) {
		TaskMng tm=DTUtil.getCurTM(dt);
		SLog.prn(1,"x: "+x);
		SysMng sm=new SysMng();
		sm.setMS_Prob(g_prob);
		
		g_ts=getTaskSim(g_sort);
		tm.setX(x);
		g_ts.init_sm_dt(sm,x,dt);
//		TaskUtil.prnUtil(tm);
		TaskUtil.prnDetail(tm);
		g_ts.simul(g_dur);
		
		if(SLogF.isON())
			SLogF.save();
		
	}
	
	
	//////////
	/// get
	
	public int getSort() {
		return g_sort;
	}
	public SimulInfo getSI() {
		return g_ts.getStat();
	}
	public String getRS() {
		SimulInfo si=g_ts.getStat();
		return si.getDegraded()+"";
	}
	
	//////////
	/// set
	
	public void setTrace(String out) {
		SLogF.init(out);
	}


	//////////
	/// private
	
	private Anal getAnalSim(int sort) {
		return SimulSel_IMC.getAnal(sort);
	}
	
	private TaskSimul getTaskSim(int sort) {
		return SimulSel_IMC.getSim(sort);
	}

	
}
