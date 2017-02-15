package simul;


import exp.Job;
import basic.Task;
import basic.TaskMng;
import util.Log;
import util.MUtil;

public class TaskSimul_EDF_AT extends TaskSimul{
	public TaskSimul_EDF_AT(TaskMng m) {
		super(m);
	}

	@Override
	protected void initMode() {
		initModeN();
	}
	
	
	@Override
	public void modeswitch_in(int tid) {
		modeswitch_in_pre(tid);		
		dropDecision();
	}
	
	private void dropDecision() {
		double ru=g_tm.getRU();
		while(ru>=1+MUtil.err){
//			Log.prn(1, "RU"+ru);
			Task t=g_tm.findDropTask();
			if(t==null){
				Log.prnc(9, "no avaiable LO-task to drop. ru:"+ru);
				System.exit(1);
			}
			dropTask(t);
			if(isPrnMS)
				Log.prn(1, "drop "+t.tid);
//			Log.prn(1, "drop "+id+","+t.getLoUtil()+","+g_tm.getReclaimUtil(id));
			ru-=g_tm.getReclaimUtil(t);
		}
//		Log.prn(1, ""+ru);
		
	}

	@Override
	protected Job relJob(Task tsk, int cur_t) {
		return relJobD(tsk,cur_t);
	}
}
