package anal;

import basic.TaskMng;
import basic.SysInfo;
import util.Log;

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
	public void prepare() {
		g_info=g_tm.getInfo();
		lotasks_loutil=g_info.getLo_util();
		hitasks_loutil=g_info.getHi_util_lm();
		hitasks_hiutil=g_info.getHi_util_hm();
		glo_x=hitasks_loutil/(1-lotasks_loutil);
	}
	
	@Override
	public double getDtm() {
		return getScore();
	}

	public double getScore() {
		if (hitasks_hiutil>1) return 2;
		if (lotasks_loutil>1) return 2;
		
		double dtm=glo_x*lotasks_loutil+hitasks_hiutil;
		return dtm;
	}
	



	@Override
	public double computeX() {
		return glo_x;
	}
	
	public static double computeX(TaskMng tm) {
		AnalEDF_VD a=new AnalEDF_VD();
		a.init(tm);
		a.prepare();
		return a.computeX();
	}

	@Override
	public void prn() {
		Log.prn(1, "util:"+lotasks_loutil+","+hitasks_loutil+","+hitasks_hiutil);
		Log.prn(1, "x:"+glo_x);
		Log.prn(1, "det:"+getDtm());
		
	}

	@Override
	public double getExtra(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

}
