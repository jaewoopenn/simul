package Simul;

import Util.Log;

public class AnalEDF_VD extends Anal {
	private double lu;
	private double hu_l;
	private double hu_h;

	@Override
	public void prepare() {
		lu=tm.getLoUtil();
		hu_l=tm.getHiUtil_l();
		hu_h=tm.getHiUtil_h();
		Log.prn(1, "util:"+lu+","+hu_l+","+hu_h);
	}
	
	@Override
	public boolean isScheduable() {
		return false;
	}




}
