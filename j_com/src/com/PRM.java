package com;

import util.MCal;
import util.SLog;

public class PRM {
	private int period;
	private double exec;
	public PRM(int p, double d) {
		period=p;
		exec=d;
	}
	public int getP() {
		return period;
	}
	public double getE() {
		return exec;
	}
	public double sbf(double t) {
		double l=period-exec;
		double tprime=t-l+MCal.err;
		double k=Math.floor(tprime/period);
		double s=k*exec+Math.max(0, t-2*l-k*period);
		return Math.max(0, s); 
	}

	public double sbf_d(double t) {
		double s=sbf(t);
		s=Math.round(s*1000)*1.0/1000;
		return s;
	}

	public void prn() {
		SLog.prn(2, period+","+exec);
		
	}
	public double nextPt(double t) {
		String st="";
		double l=period-exec;
		double tprime=t-l+MCal.err;
		st+="tprime:"+tprime;
		double k=Math.floor(tprime/period);
		st+="k:"+k;
		double alpha=tprime-k*period;
		st+="alpha:"+alpha;
		SLog.prn(1, st);
		if(t<2*l) {
			return 2*l;
		} else if(alpha<l ){
			return k*period+2*l;
		} else {
			return (k+1)*period+l;
		}
	}
	
}
