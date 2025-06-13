package imc;


import sim.SimulInfo;
import sim.SysMng;
import sim.TaskSimul_base;
import sim.job.Job;
import sim.mc.JobSimul_MC;
import task.Task;
import task.TaskMng;
import util.SLogF;
import util.SLog;

public abstract class TaskSimul_IMC extends TaskSimul_base {

	private int g_life=0;
	protected JobSimul_MC g_jsm;
	protected boolean g_ms_happen=false;
	

	
	@Override
	public void init_sm_tm(SysMng sm,TaskMng tm ){
		if(sm!=null) {
			double x=sm.getX();
			g_life=sm.getLife();
			if(x>0) {
				tm.setX(sm.getX());
			}
//			tm.prnTxt();
//			SLog.err("test");
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
	public void setBE() {
		
	}
	

	@Override
	protected void init() {
		g_jsm=new JobSimul_MC(g_tm.size());
		g_si=new SimulInfo();
//		Log.prn(1, "num:"+g_tm.size());
		initSimul();
		initModeAll();
	}

	@Override
	protected void simul_one(){
		
		release_jobs();
		if(g_jsm.is_idle()&&g_ms_happen) {
			if(g_tm.isZeroLife()) {
				recover_idle();
				g_ms_happen=false;
			}
		}
		g_jsm.simul_one();
		ms_check();
	}
	
	@Override
	protected Job rel_one_job(Task tsk, int t) {
//		SLog.prn(1, "t:"+t+" R:"+tsk.tid+" "+(t+tsk.vd)+" "+tsk.c_l+" "+tsk.isHC());
		int dl=t+tsk.period;
		if(!tsk.isHC()) {
			return new Job(tsk.tid,dl,tsk.c_l,tsk.c_l-tsk.c_h);
		}

		// HC task 
		Job j;
		if(tsk.isHM()){ // HI-mode
			j= new Job(tsk.tid, dl, tsk.c_h,dl,0);
			if(tsk.isMS()) {
				SLogF.prn("t:"+g_jsm.get_time()+" HI-mode "+tsk.tid);				
				tsk.ms_end();
			}
			if(tsk.life>0)
				tsk.life--;
		} else { // LO-mode
			j= new Job(tsk.tid, dl,tsk.c_l,t+(int)Math.ceil(tsk.vd),tsk.c_h-tsk.c_l);
			tsk.life=(int)Math.ceil(g_life/tsk.period);
		}
		return j;
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
				s+="X";
				Job j=rel_one_job(tsk,t);
				j.degrade();
				g_si.nrel++;
				if(j.exec>0)
					g_jsm.add(j);
				continue;
			}
			s+="+";
			g_jsm.add(rel_one_job(tsk,t));
		}
		s+=" ";
		SLogF.prnc(s);
	}
	
	protected void check_err() {
		if(g_tm==null){
			SLog.err("ERROR: TaskMng is not set");
		}
	}
	
	public void setRecoverIdle(boolean b) {
		g_recover_idle_on=b;
	}

	///////////////////////
	// private



	private void recover_idle(){
		SLogF.prnc( "R ");
		initModeAll();
	}
	

	private void ms_check(){
		Job j=g_jsm.get_ms_job();
		if(j==null) 
			return;
		if(j.add_exec>0) {
			if(g_rutil.getDbl()<g_sm.getMS_Prob()) { // generated prob < ms_prob
				g_ms_happen=true;
				mode_switch(j.tid);
			} else {
				g_jsm.getJM().removeCur();
			}
			
		} else { // add_exec=0 : recover check
			
			g_jsm.getJM().removeCur();
		}
	}

	
	private void initModeAll() {
		for(Task t:g_tm.getTasks()){
			t.initMode();
		}

	}	

	
	


	
	
	// abstract method
	protected abstract void modeswitch_in(Task tsk);
	
	
	
	// MC specific 
	protected void mode_switch(int tid){ // connect to each algo's MS
		Task tsk=g_tm.getTask(tid);
		SLogF.prn("t:"+g_jsm.get_time()+" mode-switch "+tid);
		g_si.ms++;
		modeswitch_in(tsk);
	}

	protected void modeswitch_after(Task tsk){ // function
		SLog.err_if(!tsk.isHC(),"task "+tsk.tid+" is not HI-task, cannot mode switch");

		tsk.ms();
		int t=g_jsm.get_time();
		tsk.sb_tm=t;
		
		g_jsm.getJM().modeswitch(tsk.tid);
	}


	
	protected void degrade_task(Task tsk) { // for IMC
		SLog.err_if(tsk.isHC(),"task "+tsk.tid+" is not LO-task, cannot drop");
		
		if(tsk.isDrop())
			return;
		
		g_si.drop+=g_jsm.getJM().degrade(tsk.tid);
		tsk.drop();
	}
	

	
}
