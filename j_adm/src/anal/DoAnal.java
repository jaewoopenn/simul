package anal;

import task.DTaskVec;
import util.MCal;
import util.SLog;

public class DoAnal {
	private int g_sort;
	private double g_dtm=0;
	private Anal g_anal;
	private boolean isMC=false;
	public DoAnal(int sort) {
		g_sort=sort;

	}
	public void setMC() {
		isMC=true;
	}
	public void run(DTaskVec dt) {
		g_dtm=0;
		g_anal=AnalSel.getAnalAuto(g_sort,isMC);
		double x=-1;
		
//		g_anal.init(dt.getTM(0));
//		x=g_anal.computeX();
//		g_anal.setX(x);
//		g_dtm=g_anal.getDtm();
		
		for(int i=0;i<dt.getStageNum();i++) {
			g_anal.init(dt.getTM(i));
			if(x==-1) 
				x=g_anal.computeX();
			g_anal.setX(x);
//			g_anal.prn();
			double d=g_anal.getDtm();
//			SLog.prn(1, "dtm: "+x+","+d);
			if(i!=0&&d>1+MCal.err) {
				double mod=g_anal.getModX();
				if(mod!=-1) {
					x=mod;
					g_anal.setX(x);
					d=g_anal.getDtm();
//					SLog.prn(1, "re dtm: "+x+","+d);
				}
			}
			g_dtm= Math.max(g_dtm, d);
		}

		SLog.prn(1, "dtm: "+x+","+g_dtm);

	}
	public String getRS() {
		if(g_dtm<=1+MCal.err)
			return "1";
		else
			return "0";
	}
	public void prn() {
		SLog.prn(1, "anal: "+g_anal.getName());
		SLog.prn(1, "dtm: "+g_dtm);
		
	}

	public int getSort() {
		return g_sort;
	}

}
