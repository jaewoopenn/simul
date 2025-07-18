package imc;


import task.Task;

public class TaskSimul_EDF_VD_IMC extends TaskSimul_IMC{

	public TaskSimul_EDF_VD_IMC() {
		super();
		g_name="EDF-VD-IMC";
	}
	
	@Override
	protected void modeswitch_in(Task tsk) {
		// all ms , all degrade 
		for(Task t:g_tm.get_HC_Tasks()){
			g_jsm.getJM().modeswitch(tsk.tid);
			t.ms();
		}
		for(Task t:g_tm.get_LC_Tasks()){
				degrade_task(t);
		}
	}


	@Override
	public void initSimul() {
		
	}




}
