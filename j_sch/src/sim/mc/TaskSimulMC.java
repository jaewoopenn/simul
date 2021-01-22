package sim.mc;


import sim.SimulInfo;
import sim.SysMng;
import sim.TaskSimul;
import sim.job.Job;
import task.Task;
import task.TaskMng;
import util.SLogF;
import util.SLog;

public abstract class TaskSimulMC extends TaskSimul {

	protected boolean g_recover_need=false;
	private  boolean g_recover_idle_on=true;
	protected boolean g_best_effort=false;
	
	protected JobSimulMC g_jsm;

	public void setBE() {
		g_best_effort=true;
	}
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
	public abstract void initSimul();


	@Override
	public void simul_end() {
		g_si.drop+=g_jsm.simul_end();
	}
	
	

	@Override
	protected void init() {
		g_jsm=new JobSimulMC(g_tm.size());
		g_si=new SimulInfo();
		g_recover_need=false;
//		Log.prn(1, "num:"+g_tm.size());
		initSimul();
	}

	@Override
	protected void simul_one(){
		
		release_jobs();
		if(g_jsm.is_idle()&&g_recover_need&&g_recover_idle_on) {
			recover_idle();
		}
		g_si.drop+=g_jsm.simul_one();
		ms_check();
		vir_check();
	}
	
	@Override
	protected Job rel_one_job(Task tsk, int t) {
//		SLog.prn(1, "t:"+t+" R:"+tsk.tid+" "+(t+tsk.period)+" "+tsk.c_l+" "+tsk.isHC());
		if(tsk.isHC()){
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
			if(tsk.isHC()) {
				s+="+";
				g_jsm.add(rel_one_job(tsk,t));
				continue;
			}
			g_si.rel++;
			if(tsk.isDrop()){
				if(this.g_best_effort) {
					s+="X";
					Job j=rel_one_job(tsk,t);
					j.drop();
					g_jsm.add(j);
				} else {
					g_si.nrel++;
				}
				continue;
			}
			s+="+";
			g_jsm.add(rel_one_job(tsk,t));
		}
		s+=" ";
		SLogF.prnc(s);
	}	
	
	public void setRecoverIdle(boolean b) {
		g_recover_idle_on=b;
	}

	///////////////////////
	// private



	private void recover_idle(){
		SLogF.prnc( "R ");
		initModeAll();
		g_recover_need=false;
	}
	

	private void ms_check(){
		boolean isMS=false;
		Job j=g_jsm.get_ms_job();
		if(j==null) 
			return;
		if(j.add_exec>0) {
			double prob=g_rutil.getDbl();
			if(prob<g_sm.getMS_Prob())
				isMS=true;
			if(isMS){
				g_recover_need=true;
				mode_switch(j.tid);
			} else {
				g_jsm.getJM().removeCur();
			}
			
		} else { // add_exec=0 : recover check
			
			recover_in(j.tid);
			g_jsm.getJM().removeCur();
		}
	}

	
	private void initModeAll() {
		for(Task t:g_tm.getTasks()){
			t.initMode();
		}

	}	

	
	


	
	
	// abstract method
	protected abstract void modeswitch_in(int tid);
	protected abstract void recover_in(int tid);
	protected abstract void vir_check();
	
	
	
	// MC specific 
	protected void mode_switch(int tid){ // connect to each algo's MS
		SLogF.prn("t:"+g_jsm.get_time()+" mode-switch "+tid);
		g_si.ms++;
		modeswitch_in(tid);
	}
	

	protected void modeswitch_tid(int tid){ // function
		Task tsk=g_tm.getTask(tid);
		SLog.err_if(!tsk.isHC(),"task "+tid+" is not HI-task, cannot mode switch");
		tsk.ms();
		tsk.sb_tm=g_jsm.get_time();
		g_jsm.getJM().modeswitch(tid);
	}

	protected void switchback_tid(int tid){
		Task tsk=g_tm.getTask(tid);
		SLog.err_if(!tsk.isHC(),"task "+tid+" is not HI-task, cannot switch back");
		if(!tsk.isHI_Preferred()) {
			SLogF.prn("t:"+g_jsm.get_time()+" switch back "+tid);
			tsk.initMode();
			tsk.sb_tm=g_jsm.get_time();
		}
//		SLogF.prn("t:"+g_jsm.get_time()+" isHI "+tsk.isHM());
	}
	
	
	protected void drop_task(Task tsk) {
		SLog.err_if(tsk.isHC(),"task "+tsk.tid+" is not LO-task, cannot drop");
		
		if(tsk.isDrop())
			return;
		
//		g_si.drop+=g_jsm.getJM().drop(tsk.tid);
		g_jsm.getJM().drop(tsk.tid);
		tsk.drop();
	}
	
	protected void resume_task(Task tsk) {
		SLog.err_if(tsk.isHC(),"task "+tsk.tid+" is not LO-task, cannot resume");
		if(!tsk.isDrop())
			return;
		tsk.resume();
		
	}

	
}
