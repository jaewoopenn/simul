package task;

/*

*/

import util.SLog;
import util.MCal;

public class SysInfo {
	private double lo_util;
	private double lo_de_util;
	private double hi_util_lm;
	private double hi_util_hm;
	private double x_para=2;
	public double getX() {
		if(x_para>1) {
			SLog.err("x:"+x_para);
		}
		return x_para;
	}
	public void setX(double x_para) {
		this.x_para = x_para;
	}
	public double computeRU(Task t) {  //EDF-VD
		if(t.isHC()){
			if(t.isHM())
				return t.getHiUtil();
			else
				return t.getLoVdUtil();
		} 
		if(t.isDrop())
			return getX()*t.getLoUtil();
		else
			return t.getLoUtil();
		
	}
	public double computeVU(Task t) {  //MC-ADAPT
		if(t.isHC()){
			if(t.isHO())
				return t.getHiUtil();
			if(t.isHM())
				return t.getHiUtil();
			else
				return t.getLoVdUtil();
		} 
		if(t.isDrop())
			return getX()*t.getLoUtil();
		else
			return t.getLoUtil();
		
	}
	

	
	public double getUtil() {
		return lo_util+hi_util_hm;
	}
	public double getCritUtil() {
		return 	Math.max(lo_util+hi_util_lm, hi_util_hm);	
	}
	public double getMaxUtil() {
		return hi_util_hm+lo_util;
	}
	public double get_lm_util() {
		return lo_util+hi_util_lm;
	}
	public double get_hm_util() {
		return hi_util_hm;
	}
	
	public double getUtil_LC() {
		return lo_util;
	}
	public double getUtil_DeLC() {
		return lo_de_util;
	}
	

	
	public double getUtil_HC_LO() {
		return hi_util_lm;
	}
	public double getUtil_HC_HI() {
		return hi_util_hm;
	}
	
	public void setLo_de_util(double u) {
		this.lo_de_util = u;
	}
	public void setLo_util(double u) {
		this.lo_util = u;
	}
	
	public void setUtil_HC_LO(double u) {
		this.hi_util_lm = u;
	}
	public void setUtil_HC_HI(double u) {
		this.hi_util_hm = u;
	}
	
	public void prn() {
		double hl=getUtil_HC_LO();
		double ll=getUtil_LC();
		SLog.prnc(2, "lo_mode_util:"+MCal.getStr(hl+ll));
		SLog.prnc(2, " hc_l_util:"+MCal.getStr(hl));
		SLog.prn(2, " lc_l_util:"+MCal.getStr(ll));
		double hh=getUtil_HC_HI();
		double lh=getUtil_DeLC();
		SLog.prnc(2, "hi_mode_util:"+MCal.getStr(hh+lh));
		SLog.prnc(2, " hc_h_util:"+MCal.getStr(hh));
		SLog.prn(2, " lc_h_util:"+MCal.getStr(lh));
		SLog.prn(2, "hc_h+lc_l:"+MCal.getStr(hh+ll));
		SLog.prn(2, "x:"+MCal.getStr(getX()));
	}
	public void prnUtil() {
		SLog.prn(2, " ll_util:"+MCal.getStr(getUtil_LC()));
		
	}
	
}
