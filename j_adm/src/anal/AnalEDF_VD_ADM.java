package anal;

import task.SysInfo;
import task.Task;
import util.MCal;
import util.SLog;

// todo EDF로 스케줄 가능할때, 전부 HI-only로 맞추기



public class AnalEDF_VD_ADM extends Anal {
	private double lc_ac;
	private double lc_de;
	private double hc_lo;
	private double hc_hi;
	private boolean isWCR;
	SysInfo g_info;
	public AnalEDF_VD_ADM() {
		super();
		g_name="EDF-VD-ADM";
	}

	@Override
	public void prepare() {
		isWCR=false;
		g_tm.updateInfo();
		g_info=g_tm.getInfo();
		lc_ac=g_info.getUtil_LC_AC();
		lc_de=g_info.getUtil_LC_DE();
		hc_lo=g_info.getUtil_HC_LO();
		hc_hi=g_info.getUtil_HC_HI();
		if(g_info.getMaxUtil()<=1) {
			setWCR();
		}
	}
	private void setWCR() {
//		SLog.prn("WCR");
		g_x=1;
		for(Task t:g_tm.get_HC_Tasks()){
			t.setHI_only();
		}
		isWCR=true;
		
	}
	private void comp_hi_prefer() {
//		int n=0;
		for(Task t:g_tm.get_HC_Tasks()){
			double v_util=t.getLoUtil()/g_x;
			double h_util=t.getHiUtil();
//			SLog.prn(1, MCal.getStr(h_util)+","+MCal.getStr(v_util));
			if(v_util>=h_util){
				t.setHI_only();
//				n++;
			}
		}
//		SLog.prn(2, "no:"+n);
	}

		
	@Override
	protected double getDtm_in(){
		double dtm=g_info.getMaxUtil();
		if (dtm<=1)
			return dtm;
		dtm=getScore();
		return dtm;
	}
	private double getScore() {
		double dtm_lo;
		double dtm_hi;
		
		double util_max=Math.max(hc_hi, lc_ac);
		if (util_max>1) 
			return util_max;
		
		dtm_hi=lc_de;
		dtm_lo=lc_ac;
//		double dtm_lo2=lc_ac;
		for(Task t:g_tm.get_HC_Tasks()){
			if(t.removed())
				continue;
			double l_util=t.getLoUtil();
			double v_util=l_util/g_x;
			double h_util=t.getHiUtil();
			if(v_util>=h_util) {// HI-only
				dtm_lo+=h_util;
				dtm_hi+= h_util;
			} else { // v_util < h_util
				dtm_lo+=v_util;
				dtm_hi+=(h_util-l_util)/(1-g_x);
			}
//			dtm_lo2+=v_util;
		}
//		SLog.prn(2, "!! dtm:"+MCal.getStr(dtm_lo)+", "+MCal.getStr(dtm_hi));
//		SLog.prn(2, "!! dtm3:"+MCal.getStr(dtm_lo2));
		double max=Math.max(dtm_lo, dtm_hi);
		if(max>1+MCal.err)
			return max;
		
		return Math.min(dtm_lo, dtm_hi);
	}
	



	@Override
	public double computeX() {
		if(isWCR)
			return 1;
		double nr_lo=0;
		double r_hi=0;
		for(Task t:g_tm.get_HC_Tasks()){
			if(t.removed())
				continue;
			if(t.isHI_Preferred()) {
				r_hi+=t.getHiUtil();
			} else {
//				nr_hi+=t.getHiUtil();
				nr_lo+=t.getLoUtil();
			}
		}	
		double x=(nr_lo)/(1-lc_ac-r_hi);
//		SLog.prn(2, nr_lo+","+lc_ac+","+r_hi);
		return x;
	}
	

	@Override
	public void prn() {
		SLog.prn(1, "LC task util:"+hc_lo+","+hc_hi);
		SLog.prn(1, "HC task util:"+lc_de+","+lc_ac);
		SLog.prn(1, "x:"+g_x);
//		double dtm=g_x*lc_ac+(1-g_x)*lc_de+hc_hi;
//		double dtm2=hc_lo/g_x+lc_ac; // LO mode 
//		SLog.prn(1, "det:"+dtm2+","+dtm);
		
	}

	

	@Override
	public void reset() {
		g_x=-1;
	}
	@Override
	public void setX(double x) {
		isDone=true;
		if(x<=0||x>1) {
			SLog.err("anal... x:"+x);
		}

		g_x=x;
		comp_hi_prefer();
	}
	
	// dynamic 

	@Override
	public double getModX() {
		prepare();
		auto();
		return computeX();
	}

	@Override
	public void auto() {
		double x=-1;
		double old_x=-2;
		while(old_x!=x) {
			old_x=x;
			x=computeX();
			if(x<=0||x>1)
				return;
			setX(x);
			SLog.prn("x: "+x);
		}
		
	}

	
	

}
