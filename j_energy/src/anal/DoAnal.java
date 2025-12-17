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
		g_anal=AnalSel.get(g_sort);
		double x=-1;
		
		TaskMng tm=null;
		for(int i=0;i<dt.getStageNum();i++) {
			SLog.prn("Stage: "+i);
			tm=DTUtil.getCurTM(dt);
//			TaskUtil.prn(tm);
			g_anal.init(tm);
			g_anal.auto();
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
//			SLog.prn(1, "x, dtm: "+x+","+d);
			if(i==0) {
				if(d>1+MCal.err) {
					dtm=d;
					SLog.prn("Not sch");
					break;
				} else {
					dtm=d;
					dt.nextStage();
					continue;
				}
			} 
			if(d>1+MCal.err) {
				double mod=g_anal.getModX();
				if(mod!=-1) {
					x=mod;
					if(x<=0||x>1) {
						SLog.prn(1, "re x: "+x);
						dtm=2;
						break;
					} 
					g_anal.setX(x);
					d=g_anal.getDtm();
					SLog.prn(1, "re x, dtm: "+x+","+d);
				}
			}
			dtm= Math.max(dtm, d);
			dt.nextStage();
		}
		SLog.prn("x, dtm: "+x+","+dtm);
		if(dtm<=1+MCal.err)
			g_rs="1";
		else
			g_rs="0";
	}
	
	public void run_simul(DTaskVec dt) {
		g_anal=AnalSel.get(g_sort);
		double x=-1;
		int tot=0;
		TaskMng tm=null;
		for(int i=0;i<dt.getStageNum();i++) {
			SLog.prn("Stage: "+i+","+tot);
			tm=DTUtil.getCurTM(dt);
			if(dt.getCurSt()!=0&&dt.getClass(dt.getCurSt())==0) {
				tot++;
			}
//			TaskUtil.prn(tm);
			g_anal.init(tm);
			g_anal.auto();
			if(x==-1) 
				x=g_anal.computeX();
			if(x<=0||x>1) {
				SLog.prn(1, "x11: "+x);
				dt.reject();
				break;
			}
			g_anal.setX(x);
			double d=g_anal.getDtm();
			SLog.prn(1, "x, dtm: "+x+","+d);
			if(i==0) {
				if(d>1+MCal.err) {
					SLog.prn("Not sch");
					break;
				} else {
					dt.nextStage();
					continue;
				}
			} 
			if(d>1+MCal.err) {
//				TaskUtil.prnUtil(tm);
				double mod=g_anal.getModX();
				if(mod!=-1) {
					if(mod<=0||mod>1) {
						SLog.prn(1, "re x: "+mod);
						d=2;
//						break;
					} else {
						x=mod;
						g_anal.setX(x);
						d=g_anal.getDtm();
						SLog.prn(1, "re x, dtm: "+x+","+d);
					}
				}
				if(d>1)
					dt.reject();
			}
			dt.nextStage();
		}
//		TaskUtil.prnUtil(tm);
//		TaskUtil.prn(tm);
		Double per=(double)dt.getR()/tot;
		SLog.prn("r, tot, per: "+dt.getR()+","+tot+","+per);
//		SLog.prn("x, dtm: "+x+","+dtm);
		g_rs= per+"";
	}

	public String run_one(DTaskVec dt) {
		g_anal=AnalSel.get(g_sort);
		TaskMng tm=null;
		tm=DTUtil.getCurTM(dt);
		TaskUtil.prn(tm);
		g_anal.init(tm);
		g_anal.auto();
		
		double d=g_anal.getDtm();
		SLog.prn("dtm: "+d);
//		tm.setX(x);
//		TaskUtil.prnDetail(tm);
		if(d<=1+MCal.err)
			return "1";
		else
			return "0";
		
	}
	
	
	public String getRS() {
		String s=g_rs;
		g_rs="";
		return s;
	}

	public int getSort() {
		return g_sort;
	}
	public void run_one(DTaskVec dt, double x) {
		g_anal=AnalSel.get(g_sort);
		TaskMng tm=DTUtil.getCurTM(dt);
		TaskUtil.prn(tm);
		g_anal.init(tm);
		g_anal.setX(x);
		double d=g_anal.getDtm();
		SLog.prn("x, dtm: "+x+","+d);
		tm.setX(x);
		TaskUtil.prnDetail(tm);
		
	}
	public void prn() {
		Anal a=AnalSel.get(g_sort);
		SLog.prn(2,a.getName());
		
	}

}
