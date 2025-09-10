package sim;


import task.Task;
import task.TaskMng;

public class TaskSimul_EDF_VD_IMC extends TaskSimul{

	public TaskSimul_EDF_VD_IMC() {
		super();
		g_name="EDF-VD-IMC";
	}
	@Override
	protected void initSimul() {
		
	}
	
	@Override
	protected void modeswitch_in(Task tsk) {
		// all ms , all degrade 
		
		for(Task t:g_tm.get_HC_Tasks()){
			g_jsm.getJM().modeswitch(t.tid);
			t.ms();
		}
		g_ext.setMS();
		for(Task t:g_tm.get_LC_Tasks()){
			g_ext.degrade_task(t);
		}
	}





	@Override
	protected void setDelay() {
		int t=g_jsm.get_time();
		int add=0;
		g_delayed_t=t+add;				
	}
	@Override
	protected int changeVD_nextSt(TaskMng tm) {
		return 0;
	}


}
