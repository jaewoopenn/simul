package anal;

import task.SysInfo;
import task.Task;
import task.TaskMng;
import util.SLog;

// todo EDF로 스케줄 가능할때, 전부 HI-only로 맞추기



public class AnalEDF_VD_ADM extends Anal {
	private double lc_ac;
	private double lc_de;
	private double hc_lo;
	private double hc_hi;
	private double g_x;
	SysInfo g_info;
	public AnalEDF_VD_ADM() {
		super();
		g_name="EDF-VD-ADM";
	}

	@Override
	public void prepare() {
		g_info=g_tm.getInfo();
		lc_ac=g_info.getUtil_LC();
		lc_de=g_info.getUtil_DeLC();
		hc_lo=g_info.getUtil_HC_LO();
		hc_hi=g_info.getUtil_HC_HI();
		g_x=computeX();
	}

		
	@Override
	public double getDtm() {
		double dtm=hc_hi+lc_ac;
		double dtm2;
		if (dtm<=1)
			return dtm;
		
		double util_max=Math.max(hc_hi, lc_ac);
		if (util_max>1) 
			return util_max;
		
		dtm=g_x*lc_ac+(1-g_x)*lc_de+hc_hi;
		dtm2=hc_lo/g_x+lc_ac;
		return Math.max(dtm, dtm2);
	}
	



	@Override
	public double computeX() {
		double x=hc_lo/(1-lc_ac);
		if(x==0)
			x=1;
		return x;
	}
	

	@Override
	public void prn() {
//		SLog.prn(1, "lotask util:"+lotasks_loutil+","+lotasks_hiutil);
//		SLog.prn(1, "hitask util:"+hitasks_loutil+","+hitasks_hiutil);
		SLog.prn(1, "x:"+g_x);
		double dtm=g_x*lc_ac+(1-g_x)*lc_de+hc_hi;
		double dtm2=hc_lo/g_x+lc_ac; // LO mode 
		SLog.prn(1, "det:"+dtm2+","+dtm);
		
	}

	

	@Override
	public void reset() {
		
	}
	@Override
	public void setX(double x) {
		g_x=x;
	}

	
	

}
