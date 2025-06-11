package anal;

import task.SysInfo;
import task.TaskMng;
import util.SLog;

public class AnalEDF_VD_IMC extends Anal {
	private double lotasks_loutil;
	private double lotasks_hiutil;
	private double hitasks_loutil;
	private double hitasks_hiutil;
	private double glo_x=-1;
	SysInfo g_info;
	public AnalEDF_VD_IMC() {
		super();
		g_name="EDF-VD-IMC";
	}

	@Override
	public void prepare() {
		g_info=g_tm.getInfo();
		lotasks_loutil=g_info.getUtil_LC();
		lotasks_hiutil=g_info.getUtil_DeLC();
		hitasks_loutil=g_info.getUtil_HC_LO();
		hitasks_hiutil=g_info.getUtil_HC_HI();
		if(glo_x==-1)
			glo_x=hitasks_loutil/(1-lotasks_loutil);
//		SLog.prn(1, glo_x);
		
	}
	
	@Override
	public double getDtm() {
		return getScore();
	}

	public double getScore() {
		if (hitasks_hiutil>1) return hitasks_hiutil;
		if (lotasks_loutil>1) return lotasks_loutil;
		double dtm=hitasks_hiutil+lotasks_loutil;
		if (dtm<=1)
			return dtm;
		dtm=glo_x*lotasks_loutil+(1-glo_x)*lotasks_hiutil+hitasks_hiutil;
		return dtm;
	}
	



	@Override
	public double computeX() {
		return glo_x;
	}
	

	@Override
	public void prn() {
//		SLog.prn(1, "lotask util:"+lotasks_loutil+","+lotasks_hiutil);
//		SLog.prn(1, "hitask util:"+hitasks_loutil+","+hitasks_hiutil);
		SLog.prn(1, "x:"+glo_x);
//		SLog.prn(1, "det:"+getDtm());
		
	}

	@Override
	public double getExtra(int i) {
		return 0;
	}
	
	public static double computeX(TaskMng tm) {
		AnalEDF_VD_IMC a=new AnalEDF_VD_IMC();
		a.init(tm);
		a.prepare();
		return a.computeX();
	}

	@Override
	public void reset() {
		glo_x=-1;
	}
	
	

}
