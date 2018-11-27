package sim.mc;


import basic.Task;
import basic.TaskMng;

public class TaskSimul_EDF_VD extends TaskSimulMC{
	public TaskSimul_EDF_VD(TaskMng m) {
		super(m);
	}


	@Override
	protected void initMode_in() {
		initMode_base();
	}
	
	
	@Override
	public void modeswitch_in(int tid) {
		for(Task tsk:g_tm.getTasks()){
			if(tsk.is_HI){
				g_jsm.getJM().modeswitch(tsk.tid);
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
