package simul;


import basic.Task;
import basic.TaskMng;
import util.Log;
import util.MUtil;

public class TaskSimul_MP_nomig extends TaskSimul_MP{
	public TaskSimul_MP_nomig(TaskMng m) {
		super(m);
	}

	
	
	@Override
	protected void recover_in() {
		Log.prn(isPrnMS, 1, "recover "+g_core);
		
	}

	
	@Override
	protected void dropDecision() {
		double ru=g_tm.getRUtil();
		while(ru>=1+MUtil.err){
//			Log.prn(1, "RU"+ru);
			Task tsk=g_tm.findDropTask();
			if(tsk==null){
				g_tm.getTaskSet().prnRuntime();
				Log.prnc(9, "no avaiable LO-task to drop. ru:"+ru);
				System.exit(1);
			}
			dropTask_base(tsk);
			Log.prn(isPrnMS,1, "drop "+tsk.tid+","+tsk.getLoUtil()+","+g_tm.getReclaimUtil(tsk));
			ru-=g_tm.getReclaimUtil(tsk);
		}
//		Log.prn(1, ""+ru);
		
	}
}
