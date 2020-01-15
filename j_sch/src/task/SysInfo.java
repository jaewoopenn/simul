package task;

/*

*/

import util.SLog;
import util.MCal;

public class SysInfo {
	private double lo_util;
	private double lo_util_max;
	private double hi_util_lm;
	private double hi_util_hm;
	private double x_para;
	public double getX() {
		return x_para;
	}
	public void setX(double x_para) {
		this.x_para = x_para;
	}
	public double computeRU(Task t) {
		if(t.isHC()){
			if(t.isHM())
				return t.getHiUtil();
			else
				return t.getLoVdUtil();
		} 
		if(t.isDrop())
			return x_para*t.getLoUtil();
		else
			return t.getLoUtil();
		
	}
	public double computeVU(Task t) {
		if(t.isHC()){
			if(t.isHM()||t.sb_tm!=-1)
				return t.getHiUtil();
			else
				return t.getLoVdUtil();
		} 
		if(t.isDrop())
			return x_para*t.getLoUtil();
		else
			return t.getLoUtil();
		
	}
	
	public double getUtil() {
		return lo_util+hi_util_hm;
	}
	public double getCritUtil() {
		return 	Math.max(lo_util+hi_util_lm, hi_util_hm);	
	}
	public double get_lm_util() {
		return lo_util+hi_util_lm;
	}
	public double get_hm_util() {
		return hi_util_hm;
	}
	
	public double getLo_util() {
		return lo_util;
	}
	public double getLo_max() {
		return lo_util_max;
	}
	
	public double getHi_util_lm() {
		return hi_util_lm;
	}
	public double getHi_util_hm() {
		return hi_util_hm;
	}
	
	public void setLo_max(double u) {
		this.lo_util_max = u;
	}
	public void setLo_util(double u) {
		this.lo_util = u;
	}
	
	public void setHi_util_lm(double u) {
		this.hi_util_lm = u;
	}
	public void setHi_util_hm(double u) {
		this.hi_util_hm = u;
	}
	
	public void prn() {
		SLog.prnc(2, "lo_mode_util:"+MCal.getStr(getLo_util()+getHi_util_lm()));
		SLog.prnc(2, " ll_util:"+MCal.getStr(getLo_util()));
		SLog.prn(2, " hl_util:"+MCal.getStr(getHi_util_lm()));
		SLog.prn(2, "hi_mode_util:"+MCal.getStr(getHi_util_hm()));
		SLog.prn(2, "x:"+MCal.getStr(getX()));
	}
	public void prnUtil() {
		SLog.prn(2, " ll_util:"+MCal.getStr(getLo_util()));
		
	}
	
}
