package simul;

import utilSim.Log;

public class AnalEDF extends Anal {
	private double u;
	@Override
	public void prepare() {
		u=tm.getInfo().getUtil();
		Log.prn(1, "det:"+u);
	}
	
	@Override
	public double getDtm() {
		return u;
	}

	@Override
	public double getDropRate(double p) {
		return 0;
	}

	@Override
	public double getX() {
		// TODO Auto-generated method stub
		return 0;
	}





}
