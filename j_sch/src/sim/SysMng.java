package sim;

import util.SLog;

public class SysMng {
	private double g_prob=-1;
	private double g_x=-1;
	private int g_delay;
	private int g_et;
	//--- Get
	public double getMS_Prob() {
		if(g_prob==-1) {
			SLog.err("SysMng:prob is not set");
		}
		return g_prob;
	}
	public double getX() {
		if(g_x==-1) {
			SLog.err("SysMng:x is not set");
		}
		return g_x;
	}
	public int getDelay() {
		return g_delay;
	}
	public int getEnd() {
		return g_et;
	}

	
	//--- Set
	public void setMS_Prob(double p) {
		g_prob=p;
	}

	public void setX(double d) {
		g_x=d;
	}
	public void prn() {
		SLog.prn(2, "prob:"+g_prob);
		SLog.prn(2, "x:"+g_x);
		SLog.prn(2, "delay:"+g_delay);
		
	}
	public void setDelay(double t) {
		g_delay=(Double.valueOf(t+1.0)).intValue();
//		SLog.prn(2, "delay:"+g_delay);
		
	}
	public void setEnd(int i) {
		g_et=i;
		
	}
	
}
