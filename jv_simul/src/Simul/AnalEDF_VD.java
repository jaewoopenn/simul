package Simul;

import Basic.Task;
import Util.Log;

public class AnalEDF_VD extends Anal {
	private double lotasks_loutil;
	private double hitasks_loutil;
	private double hitasks_hiutil;
	private double glo_x;

	@Override
	public void prepare() {
		lotasks_loutil=tm.getLoUtil();
		hitasks_loutil=tm.getHiUtil_l();
		hitasks_hiutil=tm.getHiUtil_h();
		glo_x=hitasks_loutil/(1-lotasks_loutil);
		Log.prn(1, "util:"+lotasks_loutil+","+hitasks_loutil+","+hitasks_hiutil);
		Log.prn(1, "x:"+glo_x);
	}
	
	@Override
	public boolean isScheduable() {
		if (getScore()<=1) 
			return true;
		return false;
	}

	public double getScore() {
		if (hitasks_hiutil>1) return 2;
		double dtm=glo_x*lotasks_loutil+hitasks_hiutil;
		Log.prn(1,"det:"+dtm);
		return dtm;
	}
	


	// no ratio / nl 
	@Override
	public double getDropRate(double p) {
		int size=tm.hi_size();
		double v=1-Math.pow(1-p,size);
		Log.prn(1, v+" "+Math.pow(1-p,size)+" "+size);
		return v;
	}


}
