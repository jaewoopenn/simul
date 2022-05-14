package anal_imc;

import anal.Anal;
import util.SLog;

public class AnalEDF_IMC extends Anal {
	private double u;
	public AnalEDF_IMC() {
		super();
		g_name="EDF-IMC";
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
