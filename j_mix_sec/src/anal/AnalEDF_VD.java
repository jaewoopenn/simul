package anal;

import task.TaskMng;
import util.SLog;

public class AnalEDF_VD extends Anal {
	private double lotasks_loutil;
	private double hitasks_loutil;
	private double hitasks_hiutil;
	private double glo_x;
	public AnalEDF_VD() {
		super();
		g_name="EDF-VD";
	}

	@Override
	public void prepare() {
		lotasks_loutil=g_tm.getLC_LoUtil();
		hitasks_loutil=g_tm.getHC_LoUtil();
		hitasks_hiutil=g_tm.getHC_HiUtil();
		glo_x=hitasks_loutil/(1-lotasks_loutil);
	}
	
	@Override
	public double getDtm() {
		return getScore();
	}

	public double getScore() {
		if (hitasks_hiutil>1) return hitasks_hiutil;
		if (lotasks_loutil>1) return lotasks_loutil;
		if (hitasks_hiutil+lotasks_loutil<=1)
			return hitasks_hiutil+lotasks_loutil;
		double dtm=glo_x*lotasks_loutil+hitasks_hiutil;
		return dtm;
	}
	



	

	@Override
	public void prn() {
		SLog.prn(1, "util:"+lotasks_loutil+","+hitasks_loutil+","+hitasks_hiutil);
		SLog.prn(1, "x:"+glo_x);
		SLog.prn(1, "det:"+getDtm());
		
	}


}
