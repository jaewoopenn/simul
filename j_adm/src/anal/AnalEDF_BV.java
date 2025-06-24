package anal;

import task.SysInfo;
import task.TaskMng;
import util.SLog;

public class AnalEDF_BV extends Anal {
	private double lotasks_loutil;
	private double hitasks_loutil;
	private double hitasks_hiutil;
	private double glo_x;
	SysInfo g_info;
	public AnalEDF_BV() {
		super();
		g_name="EDF-BV";
	}

	@Override
	protected void prepare() {
		g_info=g_tm.getInfo();
		lotasks_loutil=g_info.getUtil_LC();
		hitasks_loutil=g_info.getUtil_HC_LO();
		hitasks_hiutil=g_info.getUtil_HC_HI();
		glo_x=hitasks_loutil/(1-lotasks_loutil);
		double lctasks_deUtil=g_info.getUtil_DeLC();
		if(lctasks_deUtil>0)
			SLog.err("EDF-BV for MC, deUtil:"+lctasks_deUtil);

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
//		double dtm=glo_x*lotasks_loutil+hitasks_hiutil;
//		return dtm;
		
		double dtm;
		dtm=hitasks_loutil/glo_x+lotasks_loutil;
		if(dtm>1)
			return dtm;
		dtm=(hitasks_hiutil-hitasks_loutil)/(1-glo_x);
		return dtm;
	}
	



	@Override
	public double computeX() {
		return glo_x;
	}
	
	public static double computeX(TaskMng tm) {
		AnalEDF_BV a=new AnalEDF_BV();
		a.init(tm);
		a.prepare();
		return a.computeX();
	}

	@Override
	public void prn() {
		SLog.prn(1, "util:"+lotasks_loutil+","+hitasks_loutil+","+hitasks_hiutil);
		SLog.prn(1, "x:"+glo_x);
		SLog.prn(1, "det:"+getDtm());
		
	}

	@Override
	public double getExtra(int i) {
		return 0;
	}

	@Override
	public void reset() {
		
	}

}
