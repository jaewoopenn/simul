package anal;

import task.DTaskVec;
import util.MCal;
import util.SLog;

public class DoAnal {
	private int g_sort;
	private Anal g_anal;
	public DoAnal(int sort) {
		g_sort=sort;

	}
	public void run(DTaskVec dt) {
		g_anal=AnalSel_IMC.getAnal(g_sort);
		g_anal.init(dt.getTM(0));
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
		double d=g_anal.getDtm();
		SLog.prn(1, "dtm: "+d);
		
	}

	public int getSort() {
		return g_sort;
	}

}
