package sim.mc;


import util.SLog;
import task.Task;
import util.MCal;

public class TaskSimul_FMC extends TaskSimulMC{


	public TaskSimul_FMC() {
		super();
		g_name="FMC";
	}
	@Override
	public void initSimul() {
		
	}

	
	
	@Override
	protected void modeswitch_in(int tid) {
		modeswitch_tid(tid);		
		drop_algo();
	}
	
	private void drop_algo() {
		double ru=g_tm.getRUtilFMC();
//		SLog.prn(1,"ru:"+ru);
		while(ru>=0+MCal.err){
			Task tsk=g_tm.findDropTask();
			if(tsk==null){
				g_sm.prn();
				g_tm.prnRuntime();
				SLog.err("no available LO-task to drop. ru:"+ru);
			}
			drop_task(tsk);
			// LC task util minus
			ru-=tsk.getLoUtil();
//			ru-=g_tm.getReclaimUtil(tsk);
		}
		
	}


	@Override
	protected void recover_in(int tid) {
		
	}



	@Override
	protected void vir_check() {
		
	}






}
