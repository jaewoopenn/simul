package imc;


import task.Task;
import util.MCal;
import util.SLog;

public class TaskSimul_EDF_VD_ADM extends TaskSimul_IMC{

	public TaskSimul_EDF_VD_ADM() {
		super();
		g_name="EDF-VD-ADM";
	}
	
	@Override
	protected void modeswitch_in(Task tsk) {
		// individual ms. 
		double ru;
		ru=g_tm.getVUtil();
//		SLog.prn(2, "vu:"+ru);
		
		g_jsm.getJM().modeswitch(tsk.tid);
		tsk.ms();
		// check schedulability test
		ru=g_tm.getVUtil();
//		SLog.prn(2, "vu:"+ru);
		if(ru<1+MCal.err)
			return;
		if(g_ms_happen)
			return;
		g_ms_happen=true;
		SLog.prn(2, "de");
		// test fail? all degrade 
		for(Task t:g_tm.get_LC_Tasks()){
				degrade_task(t);
		}
	}


	@Override
	public void initSimul() {
		
	}




}
