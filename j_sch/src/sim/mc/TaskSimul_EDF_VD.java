package sim.mc;


import task.Task;

public class TaskSimul_EDF_VD extends TaskSimulMC{
	
	@Override
	protected void modeswitch_in(int tid) {
		for(Task tsk:g_tm.getTasks()){
			if(tsk.isHC()){
				g_jsm.getJM().modeswitch(tsk.tid);
				tsk.ms();
			} else {
				drop_task(tsk);
			}
		}
	}

	@Override
	protected void recover_in(int tid) {
	}

	@Override
	protected void vir_check() {
		
	}



}
