package sim.com;
//..


import comp.CompMng;
import sim.mc.TaskSimulMC;
import task.Task;

public class TaskSimulCom extends TaskSimulMC{

	protected CompMng g_cm;
	public void set_cm(CompMng cm) {
		g_cm=cm;
	}
	
	@Override
	public void initSimul() {
		
	}

	@Override
	protected void modeswitch_in(Task tsk) {
		
	}

	@Override
	protected void recover_in(int tid) {
		
	}

	@Override
	protected void recover_idle_in() {
		
	}




}
