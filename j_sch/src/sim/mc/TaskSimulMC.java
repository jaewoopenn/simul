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

	protected boolean g_recover_need=false;
	public boolean g_recover_on=true;
	
	protected JobSimulMC g_jsm;

	@Override
	public void init_sm_tm(SysMng sm,TaskMng tm ){
		if(sm!=null) {
			tm.setX(sm.getX());
			g_sm=sm;
		}
		g_tm=tm;
		init();
		check_err();
	}


	@Override
	public void simul_end() {
		g_jsm.simul_end();
	}
	
	

	@Override
	protected void init() {
		g_jsm=new JobSimulMC(g_tm.size());
		g_si=new SimulInfo();
		g_recover_need=false;
//		Log.prn(1, "num:"+g_tm.size());
	}

	@Override
	protected void simul_one(){
		// TODO simul one (recover implement)
		
		release_jobs();
		g_jsm.simul_one();
		ms_check();
		if(g_jsm.is_idle()&&g_recover_need&&g_recover_on) {
			recover();
		}
		//Log.prn(isSchTab,1, " "+t);
	}
	
	@Override
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
	
	
	@Override
	protected void release_jobs(){
		int t=g_jsm.get_time();
		String s="";
		for(Task tsk:g_tm.getTasks()){
			if (t%tsk.period!=0){
				s+="-";
				continue;
			}
			if(tsk.is_HI) {
				s+="+";
				g_jsm.add(rel_one_job(tsk,t));
				continue;
			}
			g_si.rel++;
			if(tsk.isDrop()){
				g_si.drop++;
				s+="X";
				continue;
			}
			s+="+";
			g_jsm.add(rel_one_job(tsk,t));
		}
		s+=" ";
		S_FLog.prnc(s);
	}	
	

	///////////////////////
	// private



	private void recover(){
		S_FLog.prn( "t:"+g_jsm.get_time()+" recover ");
		g_recover_need=false;
		initMode();
//		System.exit(0);
		
	}
	
	

	private void ms_check(){
		boolean isMS=false;
		Job j=g_jsm.get_ms_job();
		if(j==null) 
			return;
		double prob=g_rutil.getDbl();
		if(prob<g_sm.getMS_Prob())
			isMS=true;
		if(isMS){
			S_FLog.prn("t:"+g_jsm.get_time()+" mode-switch "+j.tid);
			g_recover_need=true;
			mode_switch(j.tid);
		} else {
			g_jsm.getJM().removeCur();
		}
		
	}

	
	private void initMode() {
		for(Task t:g_tm.getTasks()){
			t.initMode();
		}

	}	

	
	


	
	
	// abstract method
	protected abstract void modeswitch_in(int tid);
	
	
	
	// MC specific 
	protected void mode_switch(int tid){
		g_si.ms++;
		modeswitch_in(tid);
	}
	

	protected void modeswitch_tid(int tid){
		Task tsk=g_tm.getTask(tid);
		if(!tsk.is_HI)	{
			S_Log.err("task "+tid+" is not HI-task, cannot mode switch");
		}
		tsk.ms();
		g_jsm.getJM().modeswitch(tid);
	}
	
	protected void drop_task(Task tsk) {
		if(tsk.is_HI)	{
			S_Log.err("task "+tsk.tid+" is not LO-task, cannot drop");
		}
		if(tsk.isDrop())
			return;
		S_FLog.prn("drop "+tsk.tid);
		
		int n=g_jsm.getJM().drop(tsk.tid);
		g_si.drop+=n;
		tsk.drop();
	}
	
	
}
