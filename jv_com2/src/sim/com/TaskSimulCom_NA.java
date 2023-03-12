package sim.com;
//..


import comp.Comp;
import task.Task;

public class TaskSimulCom_NA extends TaskSimulCom{
	private boolean isExtMS=false;


	public TaskSimulCom_NA() {
		super();
		g_name="Com_NA";
	}
	@Override
	public void initSimul() {
		super.initSimul();
		isExtMS=false;
		
	}

	
	
	@Override
	protected void modeswitch_in(Task tsk) {
		modeswitch_after(tsk);		
		int cid=tsk.getComp();
		dropDecision(cid);
		resManager(cid);
//		modeswitch_after(tsk);		
//		drop_algo();
	}
	private void resManager(int ex_id) {
		if(isExtMS) return;
		for(Comp c:g_cm.getComps()){
			if(c.getID()==ex_id)
				continue;
			dropAll(c,false); // only shared
		}
		isExtMS=true;
	}
	private void dropDecision(int cid) {
		Comp c=g_cm.getComp(cid);
		dropAll(c,true);
	}
	public void dropAll(Comp c,boolean isAll) {
		for(Task t:c.getTM().getLoTasks()){
//			Log.prn(1, isAll+" "+t.is_isol());
			if(isAll||!t.is_isol())
				drop_task(t);
		}
		
	}
	
//	private void drop_algo() {
//		double ru=g_tm.getRUtil();
//		while(ru>=1+MCal.err){
//			Task tsk=g_tm.findDropTask();
//			if(tsk==null){
//				g_sm.prn();
//				g_tm.prnRuntime();
//				SLog.err("no available LO-task to drop. ru:"+ru);
//			}
//			drop_task(tsk);
//			ru=g_tm.getRUtil();
//		}
//		
//	}


	@Override
	protected void recover_in(int tid) {
		
	}



	@Override
	protected void vir_check() {
		
	}






}
