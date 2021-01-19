package anal;

import task.SysInfo;
import task.TaskMng;
import util.SLog;

public class AnalFMC extends Anal {
	private double lotasks_loutil;
	private double hitasks_loutil;
	private double hitasks_hiutil;
	private double glo_x;
	SysInfo g_info;
	public AnalFMC() {
		super();
		g_name="EDF-VD";
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
		
		double dtm=0;
		double lotasks_hiutil=lotasks_loutil*0.1;
		dtm+=glo_x*lotasks_loutil;
		dtm+=(1-glo_x)*lotasks_hiutil;
		dtm+=hitasks_hiutil;
		return dtm;
	}
	



	@Override
	public double computeX() {
		return glo_x;
	}
	
	public static double computeX(TaskMng tm) {
		AnalFMC a=new AnalFMC();
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

}
