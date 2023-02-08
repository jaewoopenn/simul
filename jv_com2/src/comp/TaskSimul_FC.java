package comp;


import simul.TaskSimul;
import basic.Task;
import basic.TaskMng;

public class TaskSimul_FC extends TaskSimul{
	protected CompMng g_cm;

	public TaskSimul_FC(TaskMng m) {
		super(m);
	}


	public void set_cm(CompMng cm) {
		this.g_cm = cm;
	}

	@Override
	protected void initMode() {
	}
	
	
	@Override
	public void modeswitch_in(Task t) {
	}


	@Override
	protected void recover_in() {
		// TODO Auto-generated method stub
		
	}	
	
}
