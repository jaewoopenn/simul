package imc;

import sim.SysMng;
import sim.job.Job;
import task.Task;
import task.TaskMng;
import util.MRand;
import util.SLog;
import util.SLogF;

public class TSUtil {
	protected TaskMng g_tm;
	protected SysMng g_sm;
	private MRand g_rutil=new MRand();
	public TSUtil(SysMng s) {
		g_sm=s;
	}
	
	public boolean isMS(Job j) {
		if(g_rutil.getDbl()<g_sm.getMS_Prob()) // generated prob < ms_prob
			return true;
		else
			return false;
	}
	public void initModeAll() {
		for(Task t:g_tm.getTasks()){
			t.initMode();
		}

	}

	public void setTM(TaskMng t) {
		g_tm=t;
	}	
	public void recover_idle(int t){
		SLogF.prnc( "R ");
		SLog.prn(1, "idle "+t);
		initModeAll();
	}

	public static Job rel_job(Task tsk, int t) {
//		SLog.prn(1, "t:"+t+" R:"+tsk.tid+" "+(t+tsk.vd)+" "+tsk.c_l+" "+tsk.isHC());
		int dl=t+tsk.period;
		if(!tsk.isHC()) { // LC task
			return new Job(tsk.tid,dl,tsk.c_l,tsk.c_l-tsk.c_h);
		}

		// HC task 
		Job j;
//		SLog.prn(1,"tsk "+tsk.tid+" HM:"+tsk.isHM());
		if(tsk.isHM()){ // HI-mode
			j= new Job(tsk.tid, dl, tsk.c_h,dl,0);
		} else { // LO-mode
			j= new Job(tsk.tid, dl,tsk.c_l,t+(int)Math.ceil(tsk.vd),tsk.c_h-tsk.c_l);
//			j.prn();
		}
		return j;
	}
	
}