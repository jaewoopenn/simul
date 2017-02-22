package simul;


import exp.Job;
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
	public void modeswitch_in(int tid) {
		for(Task tsk:g_tm.getTasks().getArr()){
			if(tsk.is_HI){
				g_js.getJM().modeswitch(tsk.tid);
				g_tm.getTask(tid).ms();
			} else {
				dropTask_base(tsk);
			}
		}
	}

	@Override
	protected Job relJob(Task tsk, int t) {
		return relJob_base(tsk,t);
	}
}
