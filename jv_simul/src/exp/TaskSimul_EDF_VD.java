package exp;


import utilSim.Log;
import basic.Task;
import basic.TaskMng;

public class TaskSimul_EDF_VD extends TaskSimul{
	public TaskSimul_EDF_VD(TaskMng m) {
		super(m);
	}

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
	
	
	
	// get param
}
