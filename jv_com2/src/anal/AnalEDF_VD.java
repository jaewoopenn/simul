package anal;

import basic.TaskMng;
import basic.TaskSetInfo;
import util.Log;

public class AnalEDF_VD extends Anal {
	private double lotasks_loutil;
	private double hitasks_loutil;
	private double hitasks_hiutil;
	private double glo_x;
	TaskSetInfo g_info;
	public AnalEDF_VD() {
		super();
		g_name="VD";
	}
	@Override
	public void prepare() {
		g_info=g_tm.getInfo();
		lotasks_loutil=g_info.getLo_util();
		hitasks_loutil=g_info.getHi_util_lm();
		hitasks_hiutil=g_info.getHi_util_hm();
		glo_x=hitasks_loutil/(1-lotasks_loutil);
		Log.prn(1, "util:"+lotasks_loutil+","+hitasks_loutil+","+hitasks_hiutil);
		Log.prn(1, "x:"+glo_x);
	}
	
	@Override
	public double getDtm() {
		return getScore();
	}

	public double getScore() {
		if (hitasks_hiutil>1) return 2;
		if (lotasks_loutil>1) return 2;
		
		double dtm=glo_x*lotasks_loutil+hitasks_hiutil;
//		Log.prnc(2," x:");
//		Log.prnDblc(2, glo_x);
//		Log.prnc(2," lt_lu:");
//		Log.prnDblc(2, lotasks_loutil);
//		Log.prnc(2," ht_lu:");
//		Log.prnDbl(2, hitasks_hiutil);
		return dtm;
	}
	



	public double getX() {
		return glo_x;
	}
	public static double computeX(TaskMng tm) {
		AnalEDF_VD a=new AnalEDF_VD();
		a.init(tm);
		a.prepare();
		return a.getX();
	}

}
