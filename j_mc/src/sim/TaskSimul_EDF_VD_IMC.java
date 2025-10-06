package sim;


import anal.Anal;
import anal.AnalEDF_VD_IMC;
import task.Task;
import task.TaskMng;
import util.SLog;

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
		Anal a=new AnalEDF_VD_IMC();
//		g_tm.prnInfo();
		a.init(tm);
		a.setX(g_tm.getX());
		double d=a.getDtm();
		SLog.prn("d:"+d);
		if(d<=1) {
			return 1;
		}
		return 0;
	}


}
