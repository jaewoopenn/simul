package sim.mc;


import basic.Task;
import util.S_Log;
import util.MUtil;

public class TaskSimul_EDF_AD_E extends TaskSimulMC{


	public TaskSimul_EDF_AD_E() {
		super();
		g_name="EDF-AD";
	}

	
	
	@Override
	protected void modeswitch_in(int tid) {
		modeswitch_tid(tid);		
		drop_algo();
	}
	
	private void drop_algo() {
		double ru=g_tm.getRUtil();
		while(ru>=1+MUtil.err){
			Task tsk=g_tm.findDropTask();
			if(tsk==null){
				g_sm.prn();
				g_tm.prnRuntime();
				S_Log.err("no available LO-task to drop. ru:"+ru);
			}
			drop_task(tsk);
			ru-=g_tm.getReclaimUtil(tsk);
		}
		
	}


	@Override
	protected void recover_in(int tid) {
		
	}



}
