package anal;

import task.SysInfo;
import util.SLog;

public class AnalEDF_BV extends Anal {
	private double lotasks_loutil;
	private double hitasks_loutil;
	private double hitasks_hiutil;
	private double bv_rato=1.2;
	SysInfo g_info;
	public AnalEDF_BV() {
		super();
		g_name="EDF-BV";
	}

	@Override
	protected void prepare_in() {
		g_info=g_tm.getInfo();
		lotasks_loutil=g_info.getUtil_LC_AC();
		hitasks_loutil=g_info.getUtil_HC_LO();
		hitasks_hiutil=g_info.getUtil_HC_HI();
	}
	
	@Override
	protected double getDtm_in() {
		double d=getScore();
		
		return d;
	}
	private double getScore() {
		if (hitasks_hiutil+lotasks_loutil<=1)
			return hitasks_hiutil+lotasks_loutil;
		
		double util_max=Math.max(hitasks_hiutil, lotasks_loutil);
		if (util_max>1) 
			return util_max;
		
		double dtm=hitasks_loutil/g_x+lotasks_loutil;
		double dtm2=(hitasks_hiutil-hitasks_loutil)/(1-g_x);
//		SLog.prn(1, glo_x+","+dtm+","+dtm2);
		return Math.max(dtm, dtm2);
	}
	



	@Override
	public double computeX() {
		double x=hitasks_loutil/(1-lotasks_loutil);
		return Math.min(x, 1);
	}
	

	@Override
	public void prn() {
		SLog.prn(1, "util:"+lotasks_loutil+","+hitasks_loutil+","+hitasks_hiutil);
		SLog.prn(1, "x:"+g_x);
		SLog.prn(1, "det:"+getDtm());
		
	}


	@Override
	public void reset() {
		g_x=-1;
	}


	@Override
	public void setX_in(double x) {
		g_x=x;
	}

	@Override
	public double getModX() {
		return Math.min(0.999,g_x*bv_rato);
	}

	@Override
	public void auto() {
		isDone=true;
		double x=computeX();
		setX(x);
		double lctasks_deUtil=g_info.getUtil_LC_DE();
		if(lctasks_deUtil>0)
			SLog.err("EDF-BV for MC, deUtil:"+lctasks_deUtil);
		
	}

}
