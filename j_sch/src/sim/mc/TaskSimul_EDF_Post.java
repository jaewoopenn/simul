package sim.mc;

// implement MC Post sch algo

import basic.Task;
import util.FLog;
import util.Log;
import util.MUtil;

public class TaskSimul_EDF_Post extends TaskSimulMC{



	@Override
	protected void initMode_in() {
		initMode_base();
	}
	
	
	@Override
	public void modeswitch_in(int tid) {
		modeswitch_in_base(tid);		
		dropDecision();
	}
	
	private void dropDecision() {
		double ru=g_tm.getRUtil();
		while(ru>=1+MUtil.err){
//			Log.prn(1, "RU"+ru);
			Task tsk=g_tm.findDropTask();
			if(tsk==null){
				Log.prn(9, "no available LO-task to drop. ru:"+ru);
//				g_tm.prnLoTasks();
				g_sm.prn();
				g_tm.prnRuntime();
				System.exit(1);
			}
			dropTask_base(tsk);
			FLog.prn("drop "+tsk.tid);
//			Log.prn(1, "drop "+id+","+t.getLoUtil()+","+g_tm.getReclaimUtil(id));
			ru-=g_tm.getReclaimUtil(tsk);
		}
//		Log.prn(1, ""+ru);
		
	}


	@Override
	protected void recover_in() {
		
	}

}
