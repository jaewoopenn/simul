package simul;


import exp.AbsJob;
import basic.Task;
import basic.TaskMng;
import utilSim.Log;
import utilSim.MUtil;

public class TaskSimul_EDF_AT_S extends TaskSimul{

	public TaskSimul_EDF_AT_S() {
		super();
	}

	public TaskSimul_EDF_AT_S(TaskMng m) {
		super(m);
	}


	@Override
	protected void initMode() {
		initModeS();
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
	protected AbsJob relJob(Task tsk, int cur_t) {
		return relJobD(tsk,cur_t);
	}
}
