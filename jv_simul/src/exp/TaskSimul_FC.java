package exp;


import comp.CompMng;
import basic.Task;
import basic.TaskMng;

public class TaskSimul_FC extends TaskSimul{
	protected CompMng g_cm;
	public TaskSimul_FC() {
		super();
	}

	public TaskSimul_FC(TaskMng m) {
		super(m);
	}


	public void set_cm(CompMng cm) {
		this.g_cm = cm;
	}

	@Override
	protected void initMode() {
		initModeS();
	}
	
	
	@Override
	public void modeswitch_in(int tid) {
	}
	
	

	@Override
	protected AbsJob relJob(Task tsk, int cur_t) {
		return relJobD(tsk,cur_t);
	}
}
