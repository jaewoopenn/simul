package anal;

import task.SysInfo;
import task.Task;
import util.SLog;

public class AnalEDF_VD_IMC extends Anal {
	private double lctasks_acUtil;
	private double lctasks_deUtil;
	private double hctasks_loutil;
	private double hctasks_hiutil;
	private double g_x=-1;
	private boolean isWCR=false;
	SysInfo g_info;
	public AnalEDF_VD_IMC() {
		super();
		g_name="EDF-VD-IMC";
	}

	@Override
	public void prepare() {
		g_info=g_tm.getInfo();
		lctasks_acUtil=g_info.getUtil_LC_AC();
		lctasks_deUtil=g_info.getUtil_LC_DE();
		hctasks_loutil=g_info.getUtil_HC_LO();
		hctasks_hiutil=g_info.getUtil_HC_HI();
//		g_info.prn();
		if(g_x==-1)
			g_x=computeX();
		if(g_info.getMaxUtil()<=1)
			setWCR();
	}
	private void setWCR() {
		g_x=1;
		for(Task t:g_tm.get_HC_Tasks()){
			t.setHI_only();
		}
		isWCR=true;
//		SLog.prn(1, "WCR: "+g_info.getMaxUtil());
		
	}

	@Override
	public void reset() {
		g_x=-1;
	}
	
	@Override
	public double getDtm() {
		double dtm=g_info.getMaxUtil();
		if (dtm<=1)
			return dtm;
		double util_max=Math.max(hctasks_hiutil, lctasks_acUtil);
		if (util_max>1) 
			return util_max;

		return getScore();
	}

	public double getScore() {
		double dtm;
		
		
		dtm=g_x*lctasks_acUtil+(1-g_x)*lctasks_deUtil+hctasks_hiutil;
		double dtm2=hctasks_loutil/g_x+lctasks_acUtil;
		return Math.max(dtm, dtm2);
	}
	



	@Override
	public double computeX() {
		if(isWCR)
			return 1;
		double x=hctasks_loutil/(1-lctasks_acUtil);
		return x;
	}
	

	@Override
	public void prn() {
//		SLog.prn(1, "lotask util:"+lotasks_loutil+","+lotasks_hiutil);
//		SLog.prn(1, "hitask util:"+hitasks_loutil+","+hitasks_hiutil);
		SLog.prn(1, "x:"+g_x);
//		SLog.prn(1, "det:"+getDtm());
		
	}

	

	@Override
	public void setX(double x) {
		g_x=x;
	}

	
	

}
