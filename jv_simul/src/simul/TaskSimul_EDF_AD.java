package simul;


import basic.Task;
import basic.TaskMng;
import util.Log;
import util.MUtil;

public class TaskSimul_EDF_AD extends TaskSimul{
	public TaskSimul_EDF_AD(TaskMng m) {
		super(m);
	}

	@Override
	protected void initMode_in() {
		initMode_base();
	}
	
	
	@Override
	public void modeswitch_in(Task t) {
		modeswitch_in_base(t);		
		dropDecision();
	}
	
	private void dropDecision() {
		double ru=g_tm.getRUtil();
		while(ru>=1+MUtil.err){
//			Log.prn(1, "RU"+ru);
			Task tsk=g_tm.findDropTask();
			if(tsk==null){
				Log.prnc(9, "no avaiable LO-task to drop. ru:"+ru);
				System.exit(1);
			}
			dropTask_base(tsk);
			if(isPrnMS)
				Log.prn(1, "drop "+tsk.tid);
//			Log.prn(1, "drop "+id+","+t.getLoUtil()+","+g_tm.getReclaimUtil(id));
			ru-=g_tm.getReclaimUtil(tsk);
		}
//		Log.prn(1, ""+ru);
		
	}

	@Override
	protected void recover_in() {
		
	}

}
