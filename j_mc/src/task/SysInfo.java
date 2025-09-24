package task;

/*

*/

import util.SLog;
import util.MCal;


public class SysInfo {
	private double lc_ac_util;
	private double lc_de_util;
	private double hc_lo_util;
	private double hc_hi_util;
	private double x_para;
	public double getX() {
		return x_para;
	}
	public void setX(double x_para) {
		this.x_para = x_para;
	}
//	public double computeRU(Task t) {
//		if(t.isHC()){
//			if(t.isHM())
//				return t.getHiUtil();
//			else
//				return t.getLoVdUtil();
//		} 
//		if(t.isDrop())
//			return x_para*t.getLoUtil();
//		else
//			return t.getLoUtil();
//		
//	}
	public double computeVU(Task t) { 
		if(t.isHC()){
			if(t.isHI_Preferred() ||t.isHM())
				return t.getHiUtil();
			else
				return t.getLoVdUtil();
		} 
		// LC task
		if(t.isDrop())
			return x_para*t.getLoUtil()+(1-x_para)*t.getHiUtil(); 
		else
			return t.getLoUtil();
		
	}
	
	


	
	public double getMaxUtil() {
		return hc_hi_util+lc_ac_util;
	}
	public double get_LO_util() {
		return lc_ac_util+hc_lo_util;
	}
	public double get_HI_util() {
		return hc_hi_util+lc_de_util;
	}
	
	public double getUtil_LC_AC() {
		return lc_ac_util;
	}
	public double getUtil_LC_DE() {
		return lc_de_util;
	}
	

	
	public double getUtil_HC_LO() {
		return hc_lo_util;
	}
	public double getUtil_HC_HI() {
		return hc_hi_util;
	}
	
	public void setLo_de_util(double u) {
		this.lc_de_util = u;
	}
	public void setLo_util(double u) {
		this.lc_ac_util = u;
	}
	
	public void setUtil_HC_LO(double u) {
		this.hc_lo_util = u;
	}
	public void setUtil_HC_HI(double u) {
		this.hc_hi_util = u;
	}
	
	public void prn() {
		double hl=getUtil_HC_LO();
		double ll=getUtil_LC_AC();
		SLog.prnc(2, "lo_mode_util:"+MCal.getStr(hl+ll));
		SLog.prnc(2, " hc_lo:"+MCal.getStr(hl));
		SLog.prn(2, " lc_ac:"+MCal.getStr(ll));
		double hh=getUtil_HC_HI();
		double lh=getUtil_LC_DE();
		SLog.prnc(2, "hi_mode_util:"+MCal.getStr(hh+lh));
		SLog.prnc(2, " hc_hi:"+MCal.getStr(hh));
		SLog.prn(2, " lc_de:"+MCal.getStr(lh));
		SLog.prn(2, "WCR:"+MCal.getStr(hh+ll));
//		SLog.prn(2, "x:"+MCal.getStr(getX()));
	}
	public void prnUtil() {
		double hl=getUtil_HC_LO();
		double ll=getUtil_LC_AC();
		double hh=getUtil_HC_HI();
		double lh=getUtil_LC_DE();
		SLog.prn(2, "HCH:"+MCal.getStr(hh));
		SLog.prn(2, "HCL:"+MCal.getStr(hl));
		SLog.prn(2, "LCA:"+MCal.getStr(ll));
		SLog.prn(2, "LCD:"+MCal.getStr(lh));
//		SLog.prn(2, "WCR:"+MCal.getStr(hh+ll));
		
	}
	
}
