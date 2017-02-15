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
		for(Task t:g_tm.getTasks().getArr()){
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
	protected Job relJob(Task tsk, int cur_t) {
		return relJobD(tsk,cur_t);
	}
}
