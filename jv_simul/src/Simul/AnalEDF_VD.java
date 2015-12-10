package Simul;

import Util.Log;

public class AnalEDF_VD extends Anal {
	private double lotasks_loutil;
	private double hitasks_loutil;
	private double hitasks_hiutil;
	private double glo_x;

	@Override
	public void prepare() {
		lotasks_loutil=tm.getLoUtil();
		lotasks_loutil=tm.getHiUtil_l();
		hitasks_hiutil=tm.getHiUtil_h();
		glo_x=(1-hitasks_hiutil)/lotasks_loutil;
		Log.prn(1, "util:"+lotasks_loutil+","+lotasks_loutil+","+hitasks_hiutil);
		Log.prn(1, "x:"+glo_x);
	}
	
	@Override
	public boolean isScheduable() {
		if (glo_x*lotasks_loutil+hitasks_hiutil <=1) {
			Log.prn(1, "Sch OK");
			return true;
		}
		return false;
	}




}
