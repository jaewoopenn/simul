package simul;

import basic.TaskSetInfo;
import utilSim.Log;

public class AnalEDF_VD extends Anal {
	private double lotasks_loutil;
	private double hitasks_loutil;
	private double hitasks_hiutil;
	private double glo_x;
	TaskSetInfo g_info;
	@Override
	public void prepare() {
		g_info=tm.getInfo();
		lotasks_loutil=g_info.getLo_util();
		hitasks_loutil=g_info.getHi_util_lm();
		hitasks_hiutil=g_info.getHi_util_hm();
		glo_x=hitasks_loutil/(1-lotasks_loutil);
		Log.prn(1, "util:"+lotasks_loutil+","+hitasks_loutil+","+hitasks_hiutil);
		Log.prn(1, "x:"+glo_x);
	}
	
	@Override
	public double getDtm() {
		return getScore();
	}

	public double getScore() {
		if (hitasks_hiutil>1) return 2;
		if (lotasks_loutil>1) return 2;
		
		double dtm=glo_x*lotasks_loutil+hitasks_hiutil;
//		Log.prnc(2," x:");
//		Log.prnDblc(2, glo_x);
//		Log.prnc(2," lt_lu:");
//		Log.prnDblc(2, lotasks_loutil);
//		Log.prnc(2," ht_lu:");
//		Log.prnDbl(2, hitasks_hiutil);
		return dtm;
	}
	


	// no ratio / nl 
	@Override
	public double getDropRate(double p) {
		int size=g_info.getHi_size();
		double v=1-Math.pow(1-p,size);
		Log.prn(1, v+" "+Math.pow(1-p,size)+" "+size);
		return v;
	}

	public double getX() {
		return glo_x;
	}

}
