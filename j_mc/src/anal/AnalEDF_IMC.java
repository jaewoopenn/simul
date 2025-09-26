package anal;

import util.SLog;

public class AnalEDF_IMC extends Anal {
	private double u;
	public AnalEDF_IMC() {
		super();
		g_name="EDF(WCR)";
	}
	@Override
	public void prepare() {
		u=g_tm.getInfo().getMaxUtil();
	}
	
	@Override
	protected double getDtm_in() {
		return u;
	}

	@Override
	public double computeX() {
		return 1;
	}
	@Override
	public void prn() {
		SLog.prn(1, "det:"+u);
		
	}
	
	@Override
	public void reset() {
		
		
	}
	@Override
	public void setX(double x) {
	}
	@Override
	public double getModX() {
		return -1;
	}
	@Override
	public void auto() {
		isDone=true;
		
	}




}
