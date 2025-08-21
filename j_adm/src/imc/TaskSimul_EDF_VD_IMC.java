package imc;


import task.Task;

public class TaskSimul_EDF_VD_IMC extends TaskSimul{

	public TaskSimul_EDF_VD_IMC() {
		super();
		g_name="EDF-VD-IMC";
	}
	
	@Override
	protected void modeswitch_in(Task tsk) {
		// all ms , all degrade 
		
		for(Task t:g_tm.get_HC_Tasks()){
			g_jsm.getJM().modeswitch(t.tid);
			t.ms();
		}
		g_ts.setMS();
		for(Task t:g_tm.get_LC_Tasks()){
			g_ts.degrade_task(t);
		}
	}


	@Override
	public void initSimul() {
		
	}


	@Override
	protected void changeVD_nextSt() {
		g_tm.setX(g_sm.getX());
		
	}

	@Override
	protected void setDelay() {
		
	}


}
