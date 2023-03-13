package sim.com;
//..


import comp.Comp;
import task.Task;

public class TaskSimulCom_NA2 extends TaskSimulCom{


	public TaskSimulCom_NA2() {
		super();
		g_name="Com_NA";
	}
	@Override
	public void initSimul() {
		super.initSimul();
		
	}

	
	
	@Override
	protected void modeswitch_in(Task tsk) {
		modeswitch_after(tsk);		
		for(Comp c:g_cm.getComps()){
			dropAll(c);
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
		
	}






}
