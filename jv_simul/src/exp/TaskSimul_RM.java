package exp;


import basic.Task;
import basic.TaskMng;
import utilSim.Log;

public class TaskSimul_RM extends TaskSimul{
	public TaskSimul_RM(TaskMng m) {
		super(m);
	}

	protected void initMode() {
		initModeN();
	}
	
	
	public void modeswitch_in(int tid) {
		if(isPrnMS)
			Log.prn(1, "ms hi "+tid);
	}
	
	@Override
	protected AbsJob relJob(Task tsk, int cur_t) {
//		if(tsk.is_HI){
//			return new JobF(tsk.tid, 
//					cur_t+tsk.period,tsk.c_h,cur_t+tsk.period,0);
//		}
//		return new JobF(tsk.tid,cur_t+tsk.period,tsk.c_l);
		return null;
	}
		
	
	
	// get param
}
