package Simul;

import Basic.Task;
import Util.Log;

public class AnalEDF extends Anal {
	private double u;
	@Override
	public void prepare() {
		u=tm.getUtil();
		Log.prn(1, "det:"+u);
	}
	
	@Override
	public boolean isScheduable() {
		if(u<=1)
			return true;
		return false;
	}

	@Override
	public double getDropRate(double p) {
		return 0;
	}





}
