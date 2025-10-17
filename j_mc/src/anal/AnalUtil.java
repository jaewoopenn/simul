package anal;

import util.SLog;

public class AnalUtil extends Anal {
	public AnalUtil() {
		super();
		g_name="EDF(WCR)";
	}
	@Override
	public void prepare_in() {
	}
	
	@Override
	protected double getDtm_in() {
		return 0;
	}

	@Override
	public double computeX() {
		return 0;
	}
	@Override
	public void prn() {
		
	}
	
	@Override
	public void reset() {
		
		
	}
	@Override
	public void setX_in(double x) {
	}
	@Override
	public double getModX() {
		return -1;
	}
	@Override
	public void auto() {
		isDone=true;
		double u=g_tm.getInfo().get_LO_util();
		SLog.prn(2,"util:"+u);
		double u=g_tm.getInfo().get_HI_util();
		SLog.prn(2,"util:"+u);
		double u=g_tm.getInfo().getMaxUtil();
		SLog.prn(2,"util:"+u);
	}




}
