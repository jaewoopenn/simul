package imc;


import sim.SimulInfo;
import sim.SysMng;
import sim.TaskSimul_base;
import sim.job.Job;
import sim.mc.JobSimul_MC;
import task.DTaskVec;
import task.Task;
import util.SLogF;
import util.SLog;

public abstract class TaskSimul_IMC extends TaskSimul_base {

	protected JobSimul_MC g_jsm;
	protected boolean g_ms_happen=false;
	
	@Override
	public void init_sm_dt(SysMng sm, DTaskVec dt ){
		g_sm=sm;
		g_dt=dt;
		g_tm=dt.getTM(0);
		g_tm.setX(sm.getX());
		init();
	}
	public abstract void initSimul();


	@Override
	protected void simul_end() {
		g_si.drop+=g_jsm.simul_end();
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
		int t=g_jsm.get_time();
		if(t==g_dt.getNext()) {
			SLog.prn(2, "task change "+t+" "+(g_dt.getStage()+1));
			g_dt.nextStage();
			g_tm=g_dt.getTM(g_dt.getStage());
			g_tm.setX(g_sm.getX());
//			g_tm.prn();
			
		}
		release_jobs();
		if(g_jsm.is_idle()&&g_ms_happen) {
			recover_idle();
			g_ms_happen=false;
		}
		if(g_ms_happen) {
			g_si.degraded++;
		}
		g_jsm.simul_one();
		ms_check();
	}
	
	@Override
	protected Job rel_one_job(Task tsk, int t) {
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
//			if(gscn!=null) {
//				int nxt=gscn.getNxt();
//				if(nxt<t) {
//					gscn.fetchNxt();
//					nxt=gscn.getNxt();
//				}
//				if(nxt!=-1) {
//					String s=t+":"+nxt+":"+tsk.tid+":";
//					for(int tid:gscn.getNxtA()) {
//						s+= tid+" ";
//						if(tsk.tid==tid) {
//							j.setTobeMS();
//							SLog.prn(1, t+": "+tsk.tid+" MS ");
//						}
//					}
//					
//				}
//				
//			}
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
	


	///////////////////////
	// private



	private void recover_idle(){
		SLogF.prnc( "R ");
		SLog.prn(1, "idle "+g_jsm.get_time());
		initModeAll();
	}
	

	private void ms_check(){
		Job j=g_jsm.get_ms_job(); 
		if(j==null) 
			return;
		if(j.add_exec>0) {
			if(isMS(j)) { 
				mode_switch(j.tid);
			} else {
				g_jsm.getJM().removeCur();
			}
			
		} else { // add_exec=0 : recover check
			
			g_jsm.getJM().removeCur();
		}
	}

	
	private boolean isMS(Job j) {
//		if(gscn!=null) 
//			return j.tobeMS();
		if(g_rutil.getDbl()<g_sm.getMS_Prob()) // generated prob < ms_prob
			return true;
		else
			return false;
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
		if(tsk==null) 
			return;
		SLogF.prn(g_jsm.get_time()+" "+tid);
		SLogF.prng("MS,"+g_jsm.get_time()+","+tid);
		SLog.prn(1,g_jsm.get_time()+": TID "+tid+"--> MS");
		g_si.ms++;
		modeswitch_in(tsk);
	}

	protected void modeswitch_after(Task tsk){ // function
		SLog.err_if(!tsk.isHC(),"task "+tsk.tid+" is not HI-task, cannot mode switch");

		tsk.ms();
		
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
