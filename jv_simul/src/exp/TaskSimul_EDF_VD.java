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
		Task[] tasks=g_tm.getTasks();
		for(Task t:tasks){
			if(!t.is_HI)
				t.is_dropped=false;
			else
				t.is_HM=false;
		}

//		g_tm.prnHI();
	}
	
	
	@Override
	public void modeswitch_in(int tid) {
		Task[] tasks=g_tm.getTasks();
		for(Task t:tasks){
			if(t.is_HI){
				if(isPrnMS)
					Log.prn(1, "ms hi "+t.tid);
				g_js.getJM().modeswitch(t.tid);
				g_tm.modeswitch(t.tid);
			} else {
				if(isPrnMS)
					Log.prn(1, "drop "+t.tid);
				drop(t.tid);
			}
		}
	}

	@Override
	protected AbsJob relJob(Task tsk, int cur_t) {
		if(tsk.is_HI){
			if(tsk.is_HM){
				return new JobD(tsk.tid, 
						cur_t+tsk.period,tsk.c_h,cur_t+tsk.period,0);
			} else {
				return new JobD(tsk.tid, 
						cur_t+tsk.period,tsk.c_l,
						cur_t+(int)Math.ceil(tsk.vd),tsk.c_h-tsk.c_l);
			}
		}
		return new JobD(tsk.tid,cur_t+tsk.period,tsk.c_l);
	}
	
	
	
	// get param
}
