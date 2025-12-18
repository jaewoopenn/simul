package anal;

import task.TaskMng;
import util.MCal;
import util.SLog;

public class DoAnal {
	private int g_sort;
	private Anal g_anal;
	private boolean isMC=false;
	public DoAnal(int sort) {
		g_sort=sort;

	}
	public void setMC() {
		isMC=true;
	}
	public void run(TaskMng tm) {
		g_anal=AnalSel.getAnalAuto(g_sort,isMC);
		g_anal.init(tm);
		double x=g_anal.computeX();
		g_anal.setX(x);

		
	}
	public String getRS() {
		double d=g_anal.getDtm();
//		SLog.prn(1, "dtm: "+d);
		if(d<=1+MCal.err)
			return "1";
		else
			return "0";
	}
	public void prn() {
		SLog.prn(1, "anal: "+g_anal.getName());
		double d=g_anal.getDtm();
		SLog.prn(1, "dtm: "+d);
		
	}

	public int getSort() {
		return g_sort;
	}

}
