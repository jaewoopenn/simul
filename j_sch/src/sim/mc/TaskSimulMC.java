package sim.mc;


import basic.Task;
import basic.TaskMng;
import sim.SimulInfo;
import sim.SysMng;
import sim.TaskSimul;
import sim.job.Job;
import util.S_FLog;
import util.S_Log;

public abstract class TaskSimulMC extends TaskSimul {

	protected boolean g_needRecover=false;
	public boolean g_recoverOn=true;
	protected JobSimulMC g_jsm;

	// @override
	public void init_sm_tm(SysMng sm,TaskMng tm ){
		if(sm!=null) {
			tm.setX(sm.getX());
			g_sm=sm;
		}
		g_tm=tm;
		init();
		check_err();
	}


	// @override
	public void simul_end() {
		g_jsm.simul_end();
	}
	
	///////////////////
	// protected

	// @override
	protected void init() {
		g_jsm=new JobSimulMC(g_tm.size());
		g_si=new SimulInfo();
		g_needRecover=false;
//		Log.prn(1, "num:"+g_tm.size());
	}

	// @override
	protected void simul_one(){
		ms_check();
		rel_check();
		g_jsm.simul_one();
		if(g_jsm.is_idle()&&g_needRecover&&g_recoverOn) {
			recover();
		}
		//Log.prn(isSchTab,1, " "+t);
	}
	
	// @override
	protected Job rel_one_job(Task tsk, int t) {
		if(tsk.is_HI){
//			tsk.prnStat();
			if(tsk.isHM()){
				return new Job(tsk.tid, 
						t+tsk.period,tsk.c_h,t+tsk.period,0);
			} else {
				return new Job(tsk.tid, 
						t+tsk.period,tsk.c_l,
						t+(int)Math.ceil(tsk.vd),tsk.c_h-tsk.c_l);
			}
		}
		return new Job(tsk.tid,t+tsk.period,tsk.c_l);
	}
	
	
	///////////////////////
	// private



	private void recover(){
		S_FLog.prn( "t:"+g_jsm.get_time()+" recover ");
		g_needRecover=false;
		initMode_base();
//		System.exit(0);
		
	}
	
	

	private void ms_check(){
		boolean isMS=false;
		int tid=g_jsm.ms_check();
		if(tid==-1) 
			return;
		double prob=g_rutil.getDbl();
		if(prob<g_sm.getMS_Prob())
			isMS=true;
		if(isMS){
			S_FLog.prn("t:"+g_jsm.get_time()+" mode-switch "+tid);
			g_needRecover=true;
			mode_switch(tid);
		} else {
			g_jsm.getJM().removeCur();
		}
		
	}
	
	private void rel_check(){
		int t=g_jsm.get_time();
		String s="";
		for(Task tsk:g_tm.getTasks()){
			if (t%tsk.period!=0){
				s+="-";
				continue;
			}
//			Log.prn(2, "rc:"+t);
			if(!tsk.is_HI){
				g_si.rel++;
//				Log.prn(2, "rel:"+t);
				if(tsk.is_dropped){
					g_si.drop++;
//					Log.prn(1, "drop plus:"+tsk.tid+" "+g_si.drop);
					s+="-";
					continue;
				}
			}
			s+="+";
			g_jsm.add(rel_one_job(tsk,t));
		}
		s+=" ";
		S_FLog.prnc(s);
	}
	private void initMode_base() {
		for(Task t:g_tm.getTasks()){
			t.initMode();
		}

	}	

	
	


	
	
	// abstract method
	protected abstract void modeswitch_in(int tid);
	
	
	
	// MC specific 
	protected void mode_switch(int tid){
		g_si.ms++;
//		Log.prn(9, "a"+g_si.ms);
		modeswitch_in(tid);
	}
	

	protected void modeswitch_tid(int tid){
		Task tsk=g_tm.getTask(tid);
		if(!tsk.is_HI)	{
			S_Log.prn(9, "ERROR: task "+tid+" is not HI-task, cannot mode switch");
			System.exit(0);
		}
		tsk.ms();
		g_jsm.getJM().modeswitch(tid);
	}
	
	protected void drop_task(Task tsk) {
		if(tsk.is_HI)	{
			S_Log.prn(9, "ERROR: task "+tsk.tid+" is not LO-task, cannot drop");
			System.exit(0);
		}
		if(tsk.is_dropped)
			return;
		int n=g_jsm.getJM().drop(tsk.tid);
		if(n>1){
			S_Log.prn(9, "drop num>1");
			System.exit(1);
		}
		g_si.drop+=n;
		tsk.drop();
	}
	
	
}
