package sim.mc;


import basic.Task;
import util.S_FLog;
import util.S_Log;
import util.MUtil;

public class TaskSimul_EDF_AD_E extends TaskSimulMC{



	
	
	@Override
	protected void modeswitch_in(int tid) {
		modeswitch_tid(tid);		
		dropDecision();
	}
	
	private void dropDecision() {
		double ru=g_tm.getRUtil();
		while(ru>=1+MUtil.err){
//			Log.prn(1, "RU"+ru);
			Task tsk=g_tm.findDropTask();
			if(tsk==null){
//				g_tm.prnLoTasks();
				g_sm.prn();
				g_tm.prnRuntime();
				S_Log.err( "no available LO-task to drop. ru:"+ru);
			}
			drop_task(tsk);
			S_FLog.prn("drop "+tsk.tid);
			ru-=g_tm.getReclaimUtil(tsk);
		}
//		Log.prn(1, ""+ru);
		
	}

	@Override
	protected void recover_in() {
		
	}



}
