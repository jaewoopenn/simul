package sim.mc;


import basic.Task;
import util.S_FLog;
import util.S_Log;
import util.MUtil;

public class TaskSimul_EDF_AD_E extends TaskSimulMC{



	
	
	@Override
	public void modeswitch_in(int tid) {
		modeswitch_tid(tid);		
		dropDecision();
	}
	
	private void dropDecision() {
		double ru=g_tm.getRUtil();
		while(ru>=1+MUtil.err){
//			Log.prn(1, "RU"+ru);
			Task tsk=g_tm.findDropTask();
			if(tsk==null){
				S_Log.prn(9, "no available LO-task to drop. ru:"+ru);
//				g_tm.prnLoTasks();
				g_sm.prn();
				g_tm.prnRuntime();
				System.exit(1);
			}
			drop_task(tsk);
			S_FLog.prn("drop "+tsk.tid);
//			Log.prn(1, "drop "+id+","+t.getLoUtil()+","+g_tm.getReclaimUtil(id));
			ru-=g_tm.getReclaimUtil(tsk);
		}
//		Log.prn(1, ""+ru);
		
	}



}
