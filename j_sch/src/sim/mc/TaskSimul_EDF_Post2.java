package sim.mc;

import util.SLog;
import util.SLogF;
//import util.SLogF;
import task.Task;
import util.MCal;

// for minJobDrop

public class TaskSimul_EDF_Post2 extends TaskSimulMC{

	public TaskSimul_EDF_Post2() {
		super();
		g_name="MC-FLEX-C2";
	}

	
	@Override
	public void initSimul() {
		g_tm.sortMinJobDrop();
		
	}
	
	@Override
	protected void modeswitch_in(int tid) {
		modeswitch_tid(tid);		
		drop_algo();
	}
	
	private void drop_algo() {
		double ru=g_tm.getVUtil();
		while(ru>=1+MCal.err){
			Task tsk=g_tm.findDropTask();
			if(tsk==null){
				g_sm.prn();
				g_tm.prnRuntime();
				SLog.err("no available LO-task to drop. ru:"+ru);
			}
			drop_task(tsk);
			ru-=g_tm.getReclaimUtil(tsk);
		}
		
	}
	
	@Override
	protected void recover_in(int tid) {
//		SLogF.prn( "t:"+g_jsm.get_time()+" recover in ");
		switchback_tid(tid);		
		resume_algo();
		
	}


	
	private void resume_algo() {
		
		double ru=g_tm.getVUtil();
//		SLogF.prn( "ru:"+ru);
		while(true){
			Task tsk=g_tm.findResumeTask();
			if(tsk==null)
				return;
			ru+=g_tm.getReclaimUtil(tsk); 
//			SLogF.prn( "resume tsk "+tsk.tid+" ru:"+ru);
			if(ru<=1+MCal.err) {
				resume_task(tsk);
			} else {  // ru>1
				break;
			}
		}
//		SLogF.prn( "t:"+g_jsm.get_time()+" resume end ");
		
	}


	@Override
	protected void vir_check() {
		int tm=g_jsm.get_time();
		int d=g_sm.getDelay();
		for(Task t:g_tm.getTasks()){
			if(!t.isHC()||t.sb_tm==-1)
				continue;
//			SLogF.prn("resume"+tm+" "+t.sb_tm);
			
			if(tm==t.sb_tm+d) {
				SLogF.prn("t:"+tm+" ************* vir sw back "+t.tid);
				t.sb_tm=-1;
				resume_algo();
			}
		}
		
		
	}










}
