package anal;

import util.SLog;

public class AnalEDF extends Anal {
	private double u;
	public AnalEDF() {
		super();
		g_name="EDF(WCR)";
	}
	@Override
	protected void prepare() {
		u=g_tm.getInfo().getUtil();
	}
	
	@Override
	public double getDtm() {
		return u;
	}

	@Override
	public double computeX() {
		return 0;
	}
	@Override
	public void prn() {
		SLog.prn(1, "det:"+u);
		
	}
	@Override
	public double getExtra(int i) {
		return 0;
	}
	@Override
	public void reset() {
		
	}





}
