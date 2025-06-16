package imc;


import task.Task;

public class TaskSimul_EDF_VD_ADM extends TaskSimul_IMC{

	public TaskSimul_EDF_VD_ADM() {
		super();
		g_name="EDF-VD-ADM";
	}
	
	@Override
	protected void modeswitch_in(Task tsk) {
		// individual ms. 
		
		g_jsm.getJM().modeswitch(tsk.tid);
		tsk.ms();
		// test fail? all degrade 
		for(Task t:g_tm.get_LC_Tasks()){
				degrade_task(t);
		}
	}


	@Override
	public void initSimul() {
		
	}




}
