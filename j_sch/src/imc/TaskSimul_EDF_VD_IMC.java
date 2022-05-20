package imc;


import task.Task;

public class TaskSimul_EDF_VD_IMC extends TaskSimul_IMC{

	public TaskSimul_EDF_VD_IMC() {
		super();
		g_name="EDF-VD-IMC";
	}
	
	@Override
	protected void modeswitch_in(Task tsk) {
		for(Task t:g_tm.getTasks()){
			if(t.isHC()){
				g_jsm.getJM().modeswitch(tsk.tid);
				t.ms();
			} else {
				degrade_task(t);
			}
		}
	}


	@Override
	public void initSimul() {
		
	}




}
