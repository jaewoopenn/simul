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
			migrate(tsk);
		} 
		dropTask_base(tsk);
	}

	private void migrate(Task tsk) {
		TaskSimul_MP.bMove=true;
		int core=-1;
		CoreMng cm=g_tm.get_cm();
		for(int i:MUtil.loop(cm.size())){
			if(i==g_core)
				continue;
			if(dtm_mig(i,tsk)){
				core=i;
				Log.prn(2, "mig OK:"+i);
				System.exit(1);
				break;
			}
			System.exit(1);
		}
		if(core==-1)
			return;
		cm.move(tsk,core);
		
	}

	private boolean dtm_mig(int core,Task tsk) {
		CoreMng cm=g_tm.get_cm();
		TaskMng tm=cm.getTM(core);
		double ru=tm.getRUtil();
		double add=tsk.getLoUtil();
		Log.prn(2, "ru: "+ru+" tsk:"+add);
		if(ru+add>1) 
			return false;
		ru=tm.getWCUtil();
		add=tm.getDroppedUtil(tsk);
		Log.prn(2, "ru: "+ru+" tsk:"+add);
		if(ru+add>1) 
			return false;
		return true;
	}

	@Override
	protected void recover_in() {
		g_tm.get_cm().recover(g_core);
		
	}

}
