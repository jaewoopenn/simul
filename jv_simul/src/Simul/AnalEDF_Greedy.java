package Simul;

import Basic.TaskSetInfo;
import Util.Log;

public class AnalEDF_Greedy extends Anal {
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
		double cal_x=(1-hitasks_hiutil)/lotasks_loutil;
		glo_x=Math.min(1, cal_x);
		Log.prn(1, "util:"+lotasks_loutil+","+hitasks_loutil+","+hitasks_hiutil);
		Log.prn(1, "x:"+glo_x);
	}
	
	@Override
	public boolean isScheduable() {
		if (hitasks_hiutil>1) return false;
		double dtm=lotasks_loutil+hitasks_loutil/glo_x;
		Log.prn(1,"det:"+dtm);
		if (dtm <=1) {
			//Log.prn(1, "Sch OK");
			return true;
		}
		return false;
	}


	@Override
	public double getDropRate(double prob_hi) {
		int size=tm.getInfo().getHi_size();
		double v=1-Math.pow(1-prob_hi,size);
		//Log.prn(1, v+" "+Math.pow(1-prob_hi,size)+" "+size);
		return v;
	}


}
