package simul;


import part.CoreMng;
import exp.Job;
import basic.Task;
import basic.TaskMng;
import util.Log;
import util.MUtil;

public class TaskSimul_MP extends TaskSimul{

	public TaskSimul_MP(TaskMng m) {
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
		double ru=g_tm.getRUtil();
		while(ru>=1+MUtil.err){
//			Log.prn(1, "RU"+ru);
			Task tsk=g_tm.findDropTask();
			if(tsk==null){
				Log.prnc(9, "no avaiable LO-task to drop. ru:"+ru);
				System.exit(1);
			}
			dropTaskMP(tsk);
			if(isPrnMS)
				Log.prn(1, "drop "+tsk.tid);
//			Log.prn(1, "drop "+id+","+t.getLoUtil()+","+g_tm.getReclaimUtil(id));
			ru-=g_tm.getReclaimUtil(tsk);
		}
//		Log.prn(1, ""+ru);
		
	}
	private void dropTaskMP(Task tsk){
		Log.prn(9, "MOVE: LO task "+tsk.tid+"");
		tsk.prn();
		CoreMng cm=g_tm.get_cm();
		cm.prn();
		cm.move(tsk,2);
		cm.prn();
		System.exit(0);
		dropTask(tsk);
		
	}

	@Override
	protected Job relJob(Task tsk, int t) {
		return relJobD(tsk,t);
	}
}
