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
	}
	private void resManager(int ex_id) {
		if(isExtMS) return;
		for(Comp c:g_cm.getComps()){
			if(c.getID()==ex_id)
				continue;
			dropShared(c);
		}
		isExtMS=true;
	}
	private void dropDecision(int cid) {
		Comp c=g_cm.getComp(cid);
		dropAll(c);
	}
	public void dropShared(Comp c) {
		for(Task t:c.getTM().getLoTasks()){
			if(!t.is_isol())
				drop_task(t);
		}
	}
	public void dropAll(Comp c) {
		for(Task t:c.getTM().getLoTasks()){
			drop_task(t);
		}
		
	}
	


	@Override
	protected void recover_in(int tid) {
		
	}



	@Override
	protected void recover_idle_in() {
		isExtMS=false;
		
	}






}
