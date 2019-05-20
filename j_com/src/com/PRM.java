package com;

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
	public double sbf(int t) {
		double l=period-exec;
		double k=Math.floor((t-l)/period);
		double s=k*exec+Math.max(0, t-2*l-k*period);
		return Math.max(0, s); 
	}
	public void prn() {
		SLog.prn(2, period+","+exec);
		
	}
	
}
