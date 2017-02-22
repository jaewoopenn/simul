package simul;


import basic.Task;
import basic.TaskMng;

public class TaskSimul_EDF_VD extends TaskSimul{
	public TaskSimul_EDF_VD(TaskMng m) {
		super(m);
	}


	@Override
	protected void initMode() {
		initMode_base();
	}
	
	
	@Override
	public void modeswitch_in(Task t) {
		for(Task tsk:g_tm.getTasks()){
			if(tsk.is_HI){
				g_js.getJM().modeswitch(t);
				t.ms();
			} else {
				dropTask_base(t);
			}
		}
	}


	@Override
	protected void recover_in() {
		// TODO Auto-generated method stub
		
	}

}
