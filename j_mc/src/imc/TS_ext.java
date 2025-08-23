package imc;

import job.Job;
import job.JobSimul;
import sim.SimulInfo;
import sim.SysMng;
import task.Task;
import task.TaskMng;
import util.MRand;
import util.SLog;
import util.SLogF;

public class TS_ext {
	private TaskMng g_tm;
	private SysMng g_sm;
	private JobSimul g_jsm;
	private SimulInfo g_si;
	private boolean g_ms_happen=false;
	private MRand g_rutil=new MRand();
	public TS_ext(SysMng s) {
		g_sm=s;
	}
	
	public void simul_end() {
		g_si.drop+=g_jsm.simul_end();
	}
	
	/////////////
	/// util
	
	public boolean happen_MS(Job j) {
		if(g_rutil.getDbl()<g_sm.getMS_Prob()) // generated prob < ms_prob
			return true;
		else
			return false;
	}
	
	//////////////////
	/// action
	
	public void initModeAll() {
		for(Task t:g_tm.getTasks()){
			t.initMode();
		}
	}

	public void variousAfterWork() {
		if(g_jsm.is_idle()&&g_ms_happen) {
			recover_idle();
			g_ms_happen=false;
		}
		if(g_ms_happen) {
			g_si.degraded++;
		}
	}

	public void recover_idle(){
		SLogF.prnc( "R ");
		SLog.prn(1, "idle "+g_jsm.get_time());
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

	public void modeswitch_after(Task tsk){ // function
		SLog.err_if(!tsk.isHC(),"task "+tsk.tid+" is not HI-task, cannot mode switch");

		tsk.ms();
		
		g_jsm.getJM().modeswitch(tsk.tid);
	}


	
	public void degrade_task(Task tsk) { // for IMC
		SLog.err_if(tsk.isHC(),"task "+tsk.tid+" is not LO-task, cannot drop");
		
		if(tsk.isDrop())
			return;
		
		g_si.drop+=g_jsm.getJM().degrade(tsk.tid);
		tsk.drop();
	}
	
	//////////
	/// set

	public void setTM(TaskMng t) {
		g_tm=t;
	}	
	public void setJSM(JobSimul j) {
		g_jsm=j;
		
	}
	public void setSI(SimulInfo s) {
		g_si=s;
	}

	public boolean isMS() {
		return g_ms_happen;
	}

	public void setMS() {
		g_ms_happen=true;
	}

	
}