package simul;


import exp.Job;
import util.Log;
import basic.Task;
import basic.TaskMng;

public class TaskSimul_EDF_VD extends TaskSimul{
	public TaskSimul_EDF_VD(TaskMng m) {
		super(m);
	}

	public TaskSimul_EDF_VD() {
		super();
	}

	@Override
	protected void initMode() {
		initModeN();
	}
	
	
	@Override
	public void modeswitch_in(int tid) {
		for(Task tsk:g_tm.getTasks().getArr()){
			if(tsk.is_HI){
				g_js.getJM().modeswitch(tsk.tid);
				g_tm.getTask(tid).ms();
			} else {
				dropTask(tsk);
			}
		}
	}

	@Override
	protected Job relJob(Task tsk, int t) {
		return relJobD(tsk,t);
	}
}
