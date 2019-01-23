package sim;

import util.Log;

public class SysMng {
	private double g_prob;
	private double g_x;

	//--- Get
	public double getMS_Prob() {
		return g_prob;
	}
	public double getX() {
		return g_x;
	}

	
	//--- Set
	public void setMS_Prob(double p) {
		g_prob=p;
	}

	public void setX(double d) {
		g_x=d;
	}
	public void prn() {
//		Log.prn(2, "prob:"+g_prob);
		Log.prn(2, "x:"+g_x);
		
	}
	
}
