package anal;

import task.SysInfo;
import task.TaskMng;
import util.SLog;

public class AnalEDF_VD_IMC extends Anal {
	private double lctasks_acUtil;
	private double lctasks_deUtil;
	private double hctasks_loutil;
	private double hctasks_hiutil;
	private double glo_x=-1;
	SysInfo g_info;
	public AnalEDF_VD_IMC() {
		super();
		g_name="EDF-VD-IMC";
	}

	@Override
	public void prepare() {
		g_info=g_tm.getInfo();
		lctasks_acUtil=g_info.getUtil_LC();
		lctasks_deUtil=g_info.getUtil_DeLC();
		hctasks_loutil=g_info.getUtil_HC_LO();
		hctasks_hiutil=g_info.getUtil_HC_HI();
		if(glo_x==-1)
			glo_x=hctasks_loutil/(1-lctasks_acUtil);
//		SLog.prn(1, glo_x);
		
	}

	@Override
	public void reset() {
		glo_x=-1;
	}
	
	@Override
	public double getDtm() {
		return getScore();
	}

	public double getScore() {
		double dtm=hctasks_hiutil+lctasks_acUtil;
		if (dtm<=1)
			return dtm;
		
		double util_max=Math.max(hctasks_hiutil, lctasks_acUtil);
		if (util_max>1) 
			return util_max;
		
		dtm=glo_x*lctasks_acUtil+(1-glo_x)*lctasks_deUtil+hctasks_hiutil;
		double dtm2=hctasks_loutil/glo_x+lctasks_acUtil;
		return Math.max(dtm, dtm2);
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

	
	public static double computeX(TaskMng tm) {
		AnalEDF_VD_IMC a=new AnalEDF_VD_IMC();
		a.init(tm);
		a.prepare();
		return a.computeX();
	}

	@Override
	public void setX(double x) {
		glo_x=x;
	}

	
	

}
