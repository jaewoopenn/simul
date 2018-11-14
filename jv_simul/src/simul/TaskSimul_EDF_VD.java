package simul;


import basic.Task;
import basic.TaskMng;

public class TaskSimul_EDF_VD extends TaskSimul{
	public TaskSimul_EDF_VD(TaskMng m) {
		super(m);
	}


	@Override
	protected void initMode_in() {
		initMode_base();
	}
	
	
	@Override
	public void modeswitch_in(Task t) {
		t=null;
		for(Task tsk:g_tm.getTasks()){
			if(tsk.is_HI){
				g_js.getJM().modeswitch(tsk);
				tsk.ms();
			} else {
				dropTask_base(tsk);
			}
		}
	}


	@Override
	protected void recover_in() {
		
	}

}
