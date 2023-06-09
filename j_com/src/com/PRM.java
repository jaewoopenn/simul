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
//		double s=k*exec+MCal.err+Math.max(0, t-2*l-k*period);
		double kp=Math.max(0, k);
		double sp=kp*exec+MCal.err+Math.max(0, t-2*l-kp*period);
//		SLog.prn(1, k+","+sp+","+MCal.err);
		if(sp<=MCal.err)
			sp=0;
		return sp;
//		return Math.max(0, sp); 
	}

	public double sbf_i(double t) {
		double init_d=period-Math.floor(exec+0.5);  // round
		double tprime=t-init_d+MCal.err;
		double k=Math.floor(tprime/period);
		double ef=Math.floor((k+1)*exec)-Math.floor(k*exec);
		double cur_d=period-ef;
//		SLog.prn(1, "k:"+k);
//		SLog.prn(1, "cur_d:"+cur_d);
		double s=Math.floor(k*exec+MCal.err)+Math.max(0, tprime-k*period-cur_d);
		s=Math.round(s*1000)*1.0/1000;
		return Math.max(0, s); 
	}
	
	public double sbf_d(double t) {
		double s=sbf(t);
		s=Math.round(s*1000)*1.0/1000;
		return s;
	}
	public double sbf_n(double t) {
		double s=sbf(t);
		return Math.floor(s);
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
	public double getUtil() {
		return exec/period;
	}
	
}
