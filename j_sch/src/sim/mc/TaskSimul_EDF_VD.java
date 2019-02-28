package sim.mc;


import basic.Task;

public class TaskSimul_EDF_VD extends TaskSimulMC{
	
	@Override
	protected void modeswitch_in(int tid) {
		for(Task tsk:g_tm.getTasks()){
			if(tsk.is_HI){
				g_jsm.getJM().modeswitch(tsk.tid);
				tsk.ms();
			} else {
				drop_task(tsk);
			}
		}
	}

	@Override
	protected void recover_in() {
	}



}
