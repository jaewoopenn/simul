package sim.mc;

import util.SLog;
//import util.SLogF;
import task.Task;
import util.MCal;

public class TaskSimul_EDF_Post extends TaskSimulMC{

	public TaskSimul_EDF_Post() {
		super();
		g_name="MC-FLEX-C1";
	}

	
	
	@Override
	protected void modeswitch_in(int tid) {
		modeswitch_tid(tid);		
		drop_algo();
	}
	
	// TODO need to consider sw_tm
	private void drop_algo() {
		double ru=g_tm.getRUtil();
//		double ru=g_tm.getVUtil();
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
	
	//TODO implement switch back algo (longest task VD)
	@Override
	protected void recover_in(int tid) {
//		SLogF.prn( "t:"+g_jsm.get_time()+" recover in ");
		switchback_tid(tid);		
		resume_algo();
		
	}


	// TODO recover algo check  ..... change HI task to LO ...
	// 1st check OK.. 2nd check OK. but keep checking 
	
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


	// vir mode�� �����ؾ� �ϴµ�.. ������ real mode�� �ϰ� �ִ�. 
	@Override
	protected void vir_check() {
		int tm=g_jsm.get_time();
		int d=g_sm.getDelay();
		for(Task t:g_tm.getTasks()){
			if(!t.isHC()||t.sb_tm==-1)
				continue;
//			SLogF.prn("resume"+tm+" "+t.sb_tm);
			
			if(tm==t.sb_tm+d) {
//				SLogF.prn("resume");
				t.sb_tm=-1;
//				resume_algo();
			}
		}
		
		
	}



	@Override
	public void initSimul() {
		
	}




}
