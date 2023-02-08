package anal;

import util.Log;

public class AnalEDF extends Anal {
	private double u;
	public AnalEDF() {
		super();
		g_name="EDF";
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
	public void prn() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public double computeX() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public double getExtra(int i) {
		// TODO Auto-generated method stub
		return 0;
	}





}
