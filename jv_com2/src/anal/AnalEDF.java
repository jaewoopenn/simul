package anal;

import util.SLog;

public class AnalEDF extends Anal {
	private double u;
	public AnalEDF() {
		super();
		g_name="EDF";
	}
	@Override
	protected void prepare() {
		u=g_tm.getInfo().getUtil();
		SLog.prn(1, "det:"+u);
		
	}
	
	@Override
	public double getDtm() {
		return u;
	}


	@Override
	public void prn() {
		
	}
	@Override
	public double computeX() {
		return 0;
	}
	@Override
	public double getExtra(int i) {
		return 0;
	}






}
