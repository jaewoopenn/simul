package sim.mc;

import util.SLog;
import util.SLogF;
//import util.SLogF;
import task.Task;
import util.MCal;

// for minJobDrop

public class TaskSimul_MC_RUN2 extends TaskSimulMC{

	public TaskSimul_MC_RUN2() {
		super();
		g_name="MC-RUN-E2";
	}

	
	@Override
	public void initSimul() {
		g_tm.sortMinJobDrop();
		
	}
	
	@Override
	protected void modeswitch_in(Task tsk) {
		modeswitch_after(tsk);		
		drop_algo(tsk.x);
	}
	
	private void drop_algo(double x) {
		double ru=g_tm.getRUN_Util(x);
		while(ru>=1+MCal.err){
			Task tsk=g_tm.findDropTask();
			if(tsk==null){
				break;
//				g_tm.prnRuntime();
////				g_tm.prnPara();
//				g_tm.prnRun(x);
//				SLog.err("no available LO-task to drop. ru:"+ru);
			}
			drop_task(tsk);
			ru=g_tm.getRUN_Util(x);
		}
		
	}
	
	@Override
	protected void recover_in(int tid) {
//		SLogF.prn( "t:"+g_jsm.get_time()+" recover in ");
//		switchback_tid(tid);		
//		resume_algo();
		
	}


	
	private void resume_algo() {
		
		double ru=g_tm.getIV_Util();
//		SLogF.prn( "ru:"+ru);
		while(true){
			Task tsk=g_tm.findResumeTask();
			if(tsk==null)
				break;
//			SLogF.prn("ru:"+(ru+tsk.getLoUtil()));
			if(ru+tsk.getLoUtil()>1+MCal.err) {
				break;
			}
			resume_task(tsk);
			ru=g_tm.getIV_Util();
		}
//		SLogF.prn( "t:"+g_jsm.get_time()+" resume end ");
		
	}


	@Override
	protected void vir_check() {

		
		
	}










}
