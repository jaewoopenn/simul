package anal;

import task.DTUtil;
import task.DTaskVec;
import task.TaskMng;
import task.TaskUtil;
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
	public void run(DTaskVec dt) {
		double dtm=0;
		g_anal=AnalSel.getAuto(g_sort,isMC);
		double x=-1;
		
		
		for(int i=0;i<dt.getStageNum();i++) {
			SLog.prn("Stage: "+i);
			TaskMng tm=DTUtil.getCurTM(dt);
			TaskUtil.prn(tm);
			g_anal.init(tm);
			if(x==-1) 
				x=g_anal.computeX();
			if(x<=0||x>1) {
				SLog.prn(1, "x: "+x);
				dtm=2;
				break;
			}
			g_anal.setX(x);
//			g_anal.prn();
			double d=g_anal.getDtm();
			SLog.prn(1, "x, dtm: "+x+","+d);
			if(i!=0&&d>1+MCal.err) {
				double mod=g_anal.getModX();
				if(mod!=-1) {
					x=mod;
					if(x<=0||x>1) {
						SLog.prn(1, "re x: "+x);
						d=2;
//						break;
					} else {
						g_anal.setX(x);
						d=g_anal.getDtm();
						SLog.prn(1, "re x, dtm: "+x+","+d);
					}
				}
			}
			dtm= Math.max(dtm, d);
			dt.nextStage();
		}

//		SLog.prn("x, dtm: "+x+","+dtm);
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

}
