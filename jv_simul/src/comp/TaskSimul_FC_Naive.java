package comp;


import basic.Task;
import basic.TaskMng;

public class TaskSimul_FC_Naive extends TaskSimul_FC{
	private boolean isExtMS=false;

	public TaskSimul_FC_Naive(TaskMng m) {
		super(m);
	}

	@Override
	protected void initMode() {
		initMode_base_hi();
		isExtMS=false;
	}

	
	@Override
	public void modeswitch_in(Task t) {
		modeswitch_in_base(t);		
		int cid=g_tm.get_comp(t.tid);
		dropDecision(cid);
		resManager(cid);
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
				dropTask_base(t);
		}
		
	}
	

}
