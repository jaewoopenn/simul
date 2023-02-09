package comp;

import sim.mc.TaskSimulMC;
import task.Task;
import task.TaskMng;

public class TaskSimul_FC_Naive extends TaskSimulMC{
	private boolean isExtMS=false;


	@Override
	protected void init() {
		super.init();
		isExtMS=false;
	}

	
	@Override
	public void modeswitch_in(Task t) {
		modeswitch_after(t);		
		int cid=t.getComp();
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
				drop_task(t);
		}
		
	}


	@Override
	public void initSimul() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void recover_in(int tid) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void vir_check() {
		// TODO Auto-generated method stub
		
	}
	

}
