package imc;

import util.SLog;
import util.SLogF;
import sim.mc.TaskSimulMC;
//import util.SLogF;
import task.Task;
import util.MCal;

// for minJobDrop

public class TaskSimul_MC_RUN extends TaskSimulIMC{

	public TaskSimul_MC_RUN() {
		super();
		g_name="MC-RUN";
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
		double ru=g_tm.getRUN_Util();
		while(ru>=1+MCal.err){
			Task tsk=g_tm.findDropTask();
			if(tsk==null){
				g_tm.prnRuntime();
//				g_tm.prnPara();
				g_tm.prnRun(x);
				SLog.err("no available LO-task to drop. ru:"+ru);
			}
			drop_task(tsk);
			ru=g_tm.getRUN_Util();
		}
		
	}
	













}
