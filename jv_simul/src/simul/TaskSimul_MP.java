package simul;


import part.CoreMng;
import basic.Task;
import basic.TaskMng;
import util.Log;
import util.MUtil;

public class TaskSimul_MP extends TaskSimul{
//	private boolean bMove=true;
	private static boolean bMove=false;
	private int g_core;
	public TaskSimul_MP(TaskMng m) {
		super(m);
	}

	public void setCore(int core){
		g_core=core;
	}
	@Override
	protected void initMode() {
//		Log.prn(2, "c:"+g_core);
		initMode_base_hi();
	}
	
	
	@Override
	public void modeswitch_in(Task t) {
//		Log.prn(2, "c:"+g_core);
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
			dropTaskMP(tsk);
			Log.prn(isPrnMS,1, "drop "+tsk.tid);
//			Log.prn(1, "drop "+id+","+t.getLoUtil()+","+g_tm.getReclaimUtil(id));
			ru-=g_tm.getReclaimUtil(tsk);
		}
//		Log.prn(1, ""+ru);
		
	}
	private void dropTaskMP(Task tsk){
		if(!TaskSimul_MP.bMove){
//			Log.prn(9, "MOVE: LO task "+tsk.tid+"");
//			tsk.prn();
			CoreMng cm=g_tm.get_cm();
//			cm.prn();
			cm.move(tsk,2);
			TaskSimul_MP.bMove=true;
		} 
		dropTask_base(tsk);
	}

	@Override
	protected void recover_in() {
		g_tm.get_cm().recover(g_core);
		
	}

}
