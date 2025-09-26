package sim;

import anal.Anal;
import anal.AnalEDF_VD_ADM;
import task.Task;
import task.TaskMng;
//import task.TaskUtil;
import util.MCal;
import util.SLog;

public class TaskSimul_EDF_VD_ADM extends TaskSimul{

	public TaskSimul_EDF_VD_ADM() {
		super();
		g_name="EDF-VD-ADM";
	}
	
	@Override
	protected void initSimul() {
		
	}
	
	@Override
	protected void modeswitch_in(Task tsk) {
		sys_ms();
//		task_ms(tsk);
	}





	private void sys_ms() {
		for(Task t:g_tm.get_HC_Tasks()) {
			g_jsm.getJM().modeswitch(t.tid);
			t.ms();
			
		}
		if(g_ext.isMS())
			return;
		g_ext.setMS();
//		SLog.prn(2, "de");
		// test fail? all degrade 
		for(Task t:g_tm.get_LC_Tasks()){
			g_ext.degrade_task(t);
		}
//		TaskUtil.prn(g_tm);
		
	}

	@SuppressWarnings("unused")
	private void task_ms(Task tsk) {
//		SLog.prn(2, "vu:"+ru);
		
		g_jsm.getJM().modeswitch(tsk.tid);
		tsk.ms();
		// check schedulability test
		double ru;
		ru=g_tm.getVUtil();
		ru=g_tm.getVUtil();
		SLog.prn(2, "vu:"+ru);
		if(ru<1+MCal.err) {
			g_ext.setMS();
		}
		if(g_ext.isMS())
			return;
		g_ext.setMS();
//		SLog.prn(2, "de");
		// test fail? all degrade 
		for(Task t:g_tm.get_LC_Tasks()){
			g_ext.degrade_task(t);
		}
//		TaskUtil.prn(g_tm);
		
	}
	
	// get dynamic;
	@Override
	protected void setDelay() {
		int t=g_jsm.get_time();
		int add=0;
		g_delayed_t=t+add;				
		
	}
	

	@Override
	protected int changeVD_nextSt(TaskMng tm) {
		Anal a=new AnalEDF_VD_ADM();
		double old_x=g_tm.getX();
		double d;
//		g_tm.prnInfo();
		a.init(g_tm);
		a.setX(old_x);
		d=a.getDtm();
//		TaskUtil.prnUtil(g_tm);
//		SLog.prn(1, "old x, dtm: "+MCal.getStr(old_x)+","+MCal.getStr(d));

//		TaskUtil.prnUtil(tm);
		a.init(tm);
		a.setX(old_x);
		d=a.getDtm();
//		SLog.prn(1, "x, dtm: "+MCal.getStr(old_x)+","+MCal.getStr(d));
//		a.prn();
		if(d<=1) {
			tm.setX(old_x);
			return 1;
		}
		if(d>1) { // need to change x
			SLog.prn(1, "x need to be changed");
			double x=a.getModX();
			if(x<=0||x>1) {
				SLog.prn(1, "re x: "+MCal.getStr(x));
				return 0; // reject
			}
			a.setX(x);
			d=a.getDtm();
			SLog.prn(1, "re x, dtm: "+MCal.getStr(x)+","+MCal.getStr(d));
			if(d>1)
				return 0; // reject
			if(!g_jsm.is_idle()&&x<old_x) {
				SLog.prn(1, "not idle, x<old_x: "+MCal.getStr(x)+","+MCal.getStr(old_x));
				return 2; // idle and change
			}
//			a.prn();
			tm.setX(x);
		}
		return 1; // OK
	}




}
