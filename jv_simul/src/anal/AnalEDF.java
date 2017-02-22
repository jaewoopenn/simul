package anal;

import util.Log;

public class AnalEDF extends Anal {
	private double u;
	public AnalEDF() {
		super();
		name="EDF";
	}
	@Override
	public void prepare() {
		u=g_tm.getInfo().getUtil();
		Log.prn(1, "det:"+u);
	}
	
	@Override
	public double getDtm() {
		return u;
	}

	@Override
	public double getX() {
		return 0;
	}





}
