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
	private double g_x;
	private boolean isWCR;
	SysInfo g_info;
	public AnalEDF_VD_ADM() {
		super();
		g_name="EDF-VD-ADM";
	}

	@Override
	public void prepare() {
		isWCR=false;
		g_info=g_tm.getInfo();
		lc_ac=g_info.getUtil_LC_AC();
		lc_de=g_info.getUtil_LC_DE();
		hc_lo=g_info.getUtil_HC_LO();
		hc_hi=g_info.getUtil_HC_HI();
		if(g_info.getMaxUtil()<=1) {
			setWCR();
			return;
		}
		for(Task t:g_tm.get_HC_Tasks()) {
			t.setNormal();
		}
		g_x=computeX();
//		g_info.prn();
//		SLog.prn(1,g_x);
		comp_hi_prefer();
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
//			SLog.prn(1, v_util+","+h_util);
			if(v_util>=h_util){
				t.setHI_only();
//				n++;
			}
		}
//		SLog.prn(2, "no:"+n);
	}

		
	@Override
	public double getDtm() {
		double dtm=g_info.getMaxUtil();
		if (dtm<=1)
			return dtm;
		dtm=getDtm2();
		return dtm;
	}
	public double getDtm2() {
		double dtm;
		double dtm2;
		
		double util_max=Math.max(hc_hi, lc_ac);
		if (util_max>1) 
			return util_max;
		
		dtm2=lc_de+(hc_hi-hc_lo)/(1-g_x);
		dtm=lc_ac;
		for(Task t:g_tm.get_HC_Tasks()){
			if(t.removed())
				continue;
			double v_util=t.getLoUtil()/g_x;
			double h_util=t.getHiUtil();
			dtm+=Math.min(v_util,h_util);
		}
//		SLog.prn(2, "dtm:"+MCal.getStr(dtm)+", ÷"+MCal.getStr(dtm2));
		double max=Math.max(dtm, dtm2);
		if(max>1+MCal.err)
			return max;
		
		return Math.min(dtm, dtm2);
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
		
	}
	@Override
	public void setX(double x) {
		if(x<=0||x>1) {
			SLog.err("anal... x:"+x);
		}

		g_x=x;
		comp_hi_prefer();
	}
	
	// dynamic 

	@Override
	public double getModX() {
		return computeX();
	}

	
	

}
