package simul;


import part.CoreMng;
import basic.Task;
import basic.TaskMng;
import util.Log;
import util.MUtil;

public class TaskSimul_MP_mig extends TaskSimul_MP{
	public TaskSimul_MP_mig(TaskMng m) {
		super(m);
	}

	
	
	@Override
	protected void recover_in() {
		Log.prn(isPrnMS, 1, "recover "+g_core);
		g_tm.get_cm().recover(g_core);
		
	}

	
	@Override
	protected void dropDecision() {
		double ru=g_tm.getRUtil();
		while(ru>=1+MUtil.err){
//			Log.prn(1, "RU"+ru);
			Task tsk=g_tm.findDropTask();
			if(tsk==null){
				Log.prn(9, "no avaiable LO-task to drop. ru:"+ru);
				g_tm.prnRuntime();
				System.exit(1);
			}
			dropTaskMP(tsk);
			Log.prn(isPrnMS,1, "drop "+tsk.tid+","+tsk.getLoUtil()+","+g_tm.getReclaimUtil(tsk));
			ru-=g_tm.getReclaimUtil(tsk);
		}
//		Log.prn(1, ""+ru);
		
	}
	private void dropTaskMP(Task tsk){
		migrate(tsk);
		dropTask_base(tsk);
	}

	private void migrate(Task tsk) {
		int core=-1;
		CoreMng cm=g_tm.get_cm();
		for(int i:MUtil.loop(cm.size())){
			if(i==g_core)
				continue;
			if(dtm_mig(i,tsk)){
				core=i;
//				Log.prn(2, "mig OK:"+i);
//				System.exit(1);
				break;
			}
//			System.exit(1);
		}
		if(core==-1)
			return;
		cm.move(tsk,core);
		
	}

	private boolean dtm_mig(int core,Task tsk) {
		CoreMng cm=g_tm.get_cm();
		TaskMng tm=cm.getTM(core);
		double lo=tm.getLoUtil();
		double add=tsk.getLoUtil();
//		Log.prn(2, "lo: "+lo+" tsk:"+add+" lo_max:"+g_tm.getInfo().getLo_max());
		if(lo+add>g_tm.getInfo().getLo_max()+MUtil.err) 
			return false;
		double ru=tm.getRUtil();
//		Log.prn(2, "ru: "+ru+" tsk:"+add);
		if(ru+add>1+MUtil.err)
			return false;
		return true;
	}

}
