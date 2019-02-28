package sim.mc;

/*
 * implement MC Post sch algo
 * 
 * TODO implement recover 
 * 
 * 
 * 
 */
import basic.Task;
import util.S_Log;
import util.MUtil;
import util.S_FLog;

public class TaskSimul_EDF_Post extends TaskSimulMC{



	
	
	@Override
	protected void modeswitch_in(int tid) {
		modeswitch_tid(tid);		
		task_drop_algo();
	}
	
	private void task_drop_algo() {
		double ru=g_tm.getRUtil();
		while(ru>=1+MUtil.err){
			Task tsk=g_tm.findDropTask();
			if(tsk==null){
				S_Log.prn(9, "no available LO-task to drop. ru:"+ru);
				g_sm.prn();
				g_tm.prnRuntime();
				System.exit(1);
			}
			drop_task(tsk);
			ru-=g_tm.getReclaimUtil(tsk);
		}
		
	}

	@Override
	protected void recover_in() {
		// TODO Auto-generated method stub
		S_FLog.prn( "t:"+g_jsm.get_time()+" recover in ");
		
	}



}
