package imc;


import task.Task;

public class TaskSimul_EDF_VD_IMC extends TaskSimulIMC{

	public TaskSimul_EDF_VD_IMC() {
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
	public void initSimul() {
		
	}



}
