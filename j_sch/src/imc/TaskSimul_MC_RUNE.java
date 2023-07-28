package imc;

import util.SLog;
import util.SLogF;
import sim.mc.TaskSimulMC;
//import util.SLogF;
import task.Task;
import util.MCal;

// for minJobDrop
// for MC RUN ÈÄ¼Ó 

public class TaskSimul_MC_RUNE extends TaskSimul_IMC{

	public TaskSimul_MC_RUNE() {
		super();
		g_name="MC-RUN-E22";
	}

	
	@Override
	public void initSimul() {
		g_tm.sortMinJobDrop(); //????
		
	}
	
	@Override
	protected void modeswitch_in(Task tsk) {
		modeswitch_after(tsk);		
		drop_algo(tsk.x);
	}
	
	private void drop_algo(double x) {
		double ru=g_tm.getRUN_Util2(x);
		while(ru>=1+MCal.err){
			Task tsk=g_tm.findDropTask();
			if(tsk==null){
				g_tm.prnRuntime();
//				g_tm.prnPara();
				g_tm.prnRun(x);
				SLog.err("no available LO-task to drop. ru:"+ru);
			}
			degrade_task(tsk);
			ru=g_tm.getRUN_Util2(x);
		}
		
	}
	

	
//	private void resume_algo() {
//		
//		double ru=g_tm.getRUN_Util();
////		SLogF.prn( "ru:"+ru);
//		while(true){
//			Task tsk=g_tm.findResumeTask();
//			if(tsk==null)
//				break;
////			SLogF.prn("ru:"+(ru+tsk.getLoUtil()));
//			if(ru+tsk.getLoUtil()>1+MCal.err) {
//				break;
//			}
//			resume_task(tsk);
//			ru=g_tm.getRUN_Util();
//		}
////		SLogF.prn( "t:"+g_jsm.get_time()+" resume end ");
//		
//	}
//










}
