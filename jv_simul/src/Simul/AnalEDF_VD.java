package Simul;

import Util.Log;

public class AnalEDF_VD extends Anal {
	private double lu;
	private double hu_l;
	private double hu_h;

	@Override
	public void prepare() {
		lu=tm.getLOUtil();
		Log.prn(1, "util:"+lu);
	}
	
	@Override
	public boolean isScheduable() {
		if(lu<=1)
			return true;
		return false;
	}




}
