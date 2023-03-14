package sim.mc;
//..


import task.Task;

public class TaskSimul_EDF_VD extends TaskSimulMC{

	public TaskSimul_EDF_VD() {
		super();
		g_name="EDF-VD";
	}
	
	@Override
	protected void modeswitch_in(Task tsk) {
		for(Task t:g_tm.getTasks()){
			if(t.isHC()){
				g_jsm.getJM().modeswitch(tsk.tid);
				t.ms();
			} else {
				drop_task(t);
			}
		}
	}

	@Override
	protected void recover_in(int tid) {
	}


	@Override
	public void initSimul() {
		
	}

	@Override
	protected void recover_idle_in() {
		
	}



}
