package anal;

import task.SysInfo;
import task.Task;
import task.TaskMng;
import util.MCal;
import util.SLog;

public class AnalFMC extends Anal {
	private double lotasks_loutil;
	private double hitasks_loutil;
	private double hitasks_hiutil;
	private double glo_x;
	SysInfo g_info;
	public AnalFMC() {
		super();
		g_name="FMC";
	}

	@Override
	public void prepare() {
		g_info=g_tm.getInfo();
		lotasks_loutil=g_info.getUtil_LC();
		hitasks_loutil=g_info.getUtil_HC_LO();
		hitasks_hiutil=g_info.getUtil_HC_HI();
		glo_x=hitasks_loutil/(1-lotasks_loutil);
	}
	
	@Override
	public double getDtm() {
		if(getScore()>=0)
			return 1;
		else 
			return 2;
	}

	public double getScore() {
		if (hitasks_hiutil>1) return 2;
		if (lotasks_loutil>1) return 2;
		
		
		double dtm=0;
		for(Task t:g_tm.getHiTasks()){
			double pi=t.getLoUtil()/hitasks_loutil*(1-lotasks_loutil)-t.getHiUtil();
			if(pi<0)
				dtm+=pi;
//			SLog.prn(1, "pi:"+pi);
		}
		dtm+=(1-glo_x)*lotasks_loutil;
		return dtm+MCal.err;
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
		SLog.prn(1, "score:"+getScore());
		SLog.prn(1, "det:"+getDtm());
		
	}

	@Override
	public double getExtra(int i) {
		return 0;
	}

}
