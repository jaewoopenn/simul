package sim.mc;

/*
 * implement MC Post sch algo
 * 
 * TODO recover implement
 * 
 * 
 * 
 */
import basic.Task;
import util.S_Log;
import util.MUtil;

public class TaskSimul_EDF_Post extends TaskSimulMC{



	
	
	@Override
	protected void modeswitch_in(int tid) {
		modeswitch_tid(tid);		
		task_drop_algo();
	}
	
	private void task_drop_algo() {
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
//			Log.prn(1, "drop "+id+","+t.getLoUtil()+","+g_tm.getReclaimUtil(id));
			ru-=g_tm.getReclaimUtil(tsk);
		}
//		Log.prn(1, ""+ru);
		
	}



}
