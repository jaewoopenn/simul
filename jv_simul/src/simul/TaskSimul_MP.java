package simul;


import basic.Task;
import basic.TaskMng;

public abstract class TaskSimul_MP extends TaskSimul{
	protected int g_core;
	public TaskSimul_MP(TaskMng m) {
		super(m);
	}

	public void setCore(int core){
		g_core=core;
	}
	// abstract method
	@Override
	protected void initMode_in() {
//		Log.prn(2, "c:"+g_core);
		initMode_base_hi();
	}
	@Override
	public void modeswitch_in(Task t) {
//		Log.prn(2, "c:"+g_core);
		modeswitch_in_base(t);		
		dropDecision();
	}
	protected abstract void recover_in();
	protected abstract void dropDecision();
	

}
