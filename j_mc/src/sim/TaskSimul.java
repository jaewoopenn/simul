package sim;


import job.Job;
import job.JobSimul;
import task.DTUtil;
import task.DTaskVec;
import task.Task;
import task.TaskMng;
import util.SLogF;
import util.SLog;

public abstract class TaskSimul  {
	protected String g_name="";
	protected SysMng g_sm;
	protected DTaskVec g_dt;
	protected TaskMng g_tm;
	protected SimulInfo g_si;
	protected JobSimul g_jsm;
	protected int g_delayed_t=-1;
	protected TS_ext g_ext;
	public String getName() {
		return g_name;
	}

	/////////////
	/// init 
	
	
	private void init() {
		g_jsm=new JobSimul(g_tm.size());
		g_si=new SimulInfo();
//		Log.prn(1, "num:"+g_tm.size());
		g_ext.setSI(g_si);
		g_ext.setJSM(g_jsm);
		initSimul();
		g_ext.initModeAll();
	}
	

	public void init_sm_dt(SysMng sm, DTaskVec dt ){
		g_sm=sm;
		g_dt=dt;
		g_tm=DTUtil.getTM(dt,0);
		g_tm.setX(sm.getX());
		g_ext=new TS_ext(g_sm);
		g_ext.setTM(g_tm);
		init();
	}

	/////////////
	/// Overide in child class
	protected abstract void initSimul();
	protected abstract void setDelay();
	protected abstract void changeVD_nextSt();
	protected abstract void modeswitch_in(Task tsk);
	
	// could overide
	protected  Job rel_one_job(Task tsk, int t) {
		return TS_ext.rel_job(tsk,t);
	}

	
	
	
	
	/////////////
	/// simul... starting point 
	
	public void simul(int et){
		int t=0;
		g_si.total=et;
		SLogF.prn("rel  / exec / t");
		while(t<et){
			simul_one();
			t++;
		}
		g_ext.simul_end();
	}

	


	private void simul_one(){
		dynamicTask();
		release_jobs();
		g_ext.variousAfterWork();
		g_jsm.simul_one();
		ms_check();
	}
	
	
	// changeVD --> algo 
	private void dynamicTask() {
		int t=g_jsm.get_time();
		if(t==g_dt.getNextTime()) {
			int st=g_dt.getStage();
			SLog.prn(2,t+": stage change "+(st+1));
			g_dt.nextStage();
			if(g_dt.getClass(st)==0) { // add
				g_si.start_delay=t;
				g_si.add_task++;
				setDelay();
			} else { // remove
				SLog.prn(2, t+": remove.");
				setTM_nextSt();
			}
//			g_tm.prn();
		}
		if(g_delayed_t!=-1) {
			if(t==g_delayed_t||g_jsm.is_idle()) {
				g_si.delayed+=t-g_si.start_delay;
				SLog.prn(2, t+": add.");
				setTM_nextSt();
				changeVD_nextSt();
				g_delayed_t=-1;
			}
		}
		
	}

	private void setTM_nextSt() {
		TaskMng tm=DTUtil.getCurTM(g_dt);
		g_tm=tm;
		tm.prn();
		g_ext.setTM(tm);
	}

	// rel_one_job --> algo 
	private void release_jobs(){
		int t=g_jsm.get_time();
		String s="";
		for(Task tsk:g_tm.getTasks()){
			if (t%tsk.period!=0){
				s+="-";
				continue;
			}
			if(tsk.isHC()) {
				s+="*";
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
	



	

	// MC specific 
	private void ms_check(){
		Job j=g_jsm.get_ms_job(); 
		if(j==null) 
			return;
		if(j.add_exec>0) {
			if(g_ext.happen_MS(j)) { 
				mode_switch(j.tid);
			} else { // LO complete 
				g_jsm.getJM().removeCur();
			}
			
		} else { // add_exec=0 : recover check
			g_jsm.getJM().removeCur();
		}
	}

	protected void mode_switch(int tid){ // connect to each algo's MS
		Task tsk=g_tm.getTask(tid);
		if(tsk==null) 
			return;
		SLogF.prn(g_jsm.get_time()+" "+tid);
		SLogF.prng("MS,"+g_jsm.get_time()+","+tid);
		SLog.prn(1,g_jsm.get_time()+": TID "+tid+"--> MS");
		g_si.ms++;
		modeswitch_in(tsk);
	}
	

	
	


	
	

	
	// get statics after simulation
	public SimulInfo getStat(){
		return g_si;
	}

	
}
