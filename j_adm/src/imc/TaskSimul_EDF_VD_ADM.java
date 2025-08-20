package imc;

import anal.Anal;
import anal.AnalEDF_VD_ADM;
import task.Task;
import util.MCal;
import util.SLog;

public class TaskSimul_EDF_VD_ADM extends TaskSimul{

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
//		SLog.prn(2, "de");
		// test fail? all degrade 
		for(Task t:g_tm.get_LC_Tasks()){
			g_ts.degrade_task(t);
		}
	}


	@Override
	public void initSimul() {
		
	}

	@Override
	protected void setVD() {
		Anal a=new AnalEDF_VD_ADM();
		a.init(g_tm);
		double x=g_sm.getX();
		a.setX(x);
		double d=a.getDtm();
		SLog.prn(2, "dtm:"+d);
		a.prn();
		if(d>1) {
			SLog.prn(2, "x need to be changed");
			x=a.computeX();
			d=a.getDtm();
			a.prn();
			if(d<=1) {
				g_tm.setX(x);
				g_sm.setX(x);
			}
		}
		
	}

	@Override
	protected void setDelay() {
		int t=g_jsm.get_time();
		int add=50;
		// VD는 먼저 바꾸자 
		// max Delay
//		int add=g_tm.getMaxPeriod();
		g_delayed_t=t+add;				
		
	}




}
