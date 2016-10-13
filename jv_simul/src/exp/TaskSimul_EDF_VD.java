package exp;


import utilSim.Log;
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
		Task[] tasks=g_tm.getTasks();
		for(Task t:tasks){
			if(t.is_HI){
				if(isPrnMS)
					Log.prn(1, "ms hi "+t.tid);
				g_js.getJM().modeswitch(t.tid);
				g_tm.getTask(tid).ms();
			} else {
				if(isPrnMS)
					Log.prn(1, "drop "+t.tid);
				dropTask(t);
			}
		}
	}

	@Override
	protected AbsJob relJob(Task tsk, int cur_t) {
		return relJobD(tsk,cur_t);
	}
}
