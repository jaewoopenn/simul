package anal;

import task.SysInfo;
import task.TaskMng;
import util.SLog;

public class AnalEDF_VD extends Anal {
	private double lotasks_loutil;
	private double hitasks_loutil;
	private double hitasks_hiutil;
	private double glo_x;
	SysInfo g_info;
	public AnalEDF_VD() {
		super();
		g_name="VD";
	}
	@Override
	protected void prepare() {
		g_info=g_tm.getInfo();
		lotasks_loutil=g_info.getUtil_LC();
		hitasks_loutil=g_info.getUtil_HC_LO();
		hitasks_hiutil=g_info.getUtil_HC_HI();
		glo_x=hitasks_loutil/(1-lotasks_loutil);
		SLog.prn(1, "util:"+lotasks_loutil+","+hitasks_loutil+","+hitasks_hiutil);
		SLog.prn(1, "x:"+glo_x);
	}
	
	@Override
	public double getDtm() {
		return getScore();
	}

	public double getScore() {
		if (hitasks_hiutil>1) return 2;
		if (lotasks_loutil>1) return 2;
		if(lotasks_loutil+hitasks_hiutil<=1) {
			return 0;
		}		
		double dtm=glo_x*lotasks_loutil+hitasks_hiutil;
//		Log.prnc(2," x:");
//		Log.prnDblc(2, glo_x);
//		Log.prnc(2," lt_lu:");
//		Log.prnDblc(2, lotasks_loutil);
//		Log.prnc(2," ht_lu:");
//		Log.prnDbl(2, hitasks_hiutil);
		return dtm;
	}
	



	public static double computeX(TaskMng tm) {
		AnalEDF_VD a=new AnalEDF_VD();
		a.init(tm);
		return a.computeX();
	}
	public static double dtm(TaskMng tm) {
		AnalEDF_VD a=new AnalEDF_VD();
		a.init(tm);
		return a.getScore();
	}	
	@Override
	public void prn() {
		
	}
	@Override
	public double computeX() {
		return glo_x;
	}
	@Override
	public double getExtra(int i) {
		return 0;
	}

}
