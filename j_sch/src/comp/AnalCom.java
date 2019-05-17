package comp;

import anal.Anal;
import util.SLog;

public class AnalCom extends Anal {
	private double u;
	public AnalCom() {
		super();
		g_name="EDF";
	}
	@Override
	public void prepare() {
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





}
