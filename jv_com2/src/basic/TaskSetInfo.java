package basic;

import util.Log;
import util.MUtil;

public class TaskSetInfo {
	private double lo_util;
	private double hi_util_lm;
	private double hi_util_hm;
	private double x_para;
	private double prob_ms=0.5;
	public double getProb_ms() {
		return prob_ms;
	}
	public void setProb_ms(double prob_ms) {
		this.prob_ms = prob_ms;
	}
	public double getX() {
		return x_para;
	}
	public void setX(double x_para) {
		this.x_para = x_para;
	}
	public double computeRU(Task t) {
		if(t.is_HI){
			if(t.is_HM)
				return t.getHiUtil();
			else
				return t.getLoRUtil();
		} 
		if(t.is_dropped)
			return x_para*t.getLoUtil();
		else
			return t.getLoUtil();
		
	}
	
	public double getUtil() {
		return lo_util+hi_util_hm;
	}
	public double getMCUtil() {
		return 	Math.max(lo_util+hi_util_lm, hi_util_hm);	
	}
	public double getLo_util() {
		return lo_util;
	}
	public double getHi_util_lm() {
		return hi_util_lm;
	}
	public double getHi_util_hm() {
		return hi_util_hm;
	}
	public void setLo_util(double lo_util) {
		this.lo_util = lo_util;
	}
	public void setHi_util_lm(double hi_util_lm) {
		this.hi_util_lm = hi_util_lm;
	}
	public void setHi_util_hm(double hi_util_hm) {
		this.hi_util_hm = hi_util_hm;
	}
	public void prn() {
		Log.prnc(2, "lo_mode_util:"+MUtil.getStr(getLo_util()+getHi_util_lm()));
		Log.prnc(2, " ll_util:"+MUtil.getStr(getLo_util()));
		Log.prn(2, " hl_util:"+MUtil.getStr(getHi_util_lm()));
		Log.prn(2, "hi_mode_util:"+MUtil.getStr(getHi_util_hm()));
	}
	public void prnUtil() {
		Log.prn(2, " ll_util:"+MUtil.getStr(getLo_util()));
		
	}
	
}
