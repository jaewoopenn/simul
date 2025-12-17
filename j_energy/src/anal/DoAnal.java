package anal;

import task.TaskMng;
import task.TaskUtil;
import task.TaskVec;
import util.MCal;
import util.SLog;

public class DoAnal {
	private int g_sort;
	private String g_rs;
	private Anal g_anal;
	private boolean isMC=false;
	public DoAnal(int sort) {
		g_sort=sort;

	}
	public void setMC() {
		isMC=true;
	}
	public void run(TaskVec dt) {
		double dtm=0;
		g_anal=AnalSel.get(g_sort);
		
		TaskMng tm=null;
		tm=dt.getTM();
//			TaskUtil.prn(tm);
		g_anal.init(tm);
		g_anal.auto();
		double x=g_anal.computeX();
		if(x<=0||x>1) {
			g_rs="0";
			return;
		} 
		g_anal.setX(x);
		dtm=g_anal.getDtm();
		if(dtm<=1+MCal.err)
			g_rs="1";
		else
			g_rs="0";
	}
	

	
	public String getRS() {
		String s=g_rs;
		g_rs="";
		return s;
	}

	public int getSort() {
		return g_sort;
	}
	public void prn() {
		Anal a=AnalSel.get(g_sort);
		SLog.prn(2,a.getName());
		
	}

}
