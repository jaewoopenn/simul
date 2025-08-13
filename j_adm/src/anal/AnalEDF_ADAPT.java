package anal;

import task.SysInfo;
import task.Task;
import task.TaskMng;
import util.MCal;
import util.SLog;

// todo EDF로 스케줄 가능할때, 전부 HI-only로 맞추기



public class AnalEDF_ADAPT extends Anal {
	private double lc_ac;
	private double lc_de;
	private double hc_lo;
	private double hc_hi;
	private double g_x;
	SysInfo g_info;
	public AnalEDF_ADAPT() {
		super();
		g_name="MC-ADAPT";
	}

	@Override
	public void prepare() {
		g_info=g_tm.getInfo();
		lc_ac=g_info.getUtil_LC_AC();
		lc_de=g_info.getUtil_LC_DE();
		hc_lo=g_info.getUtil_HC_LO();
		hc_hi=g_info.getUtil_HC_HI();
		g_x=computeX();
		comp_hi_prefer();
	}
	
	private void comp_hi_prefer() {
		int n=0;
		for(Task t:g_tm.get_HC_Tasks()){
			double v_util=t.getLoUtil()/g_x;
			double h_util=t.getHiUtil();
//			Log.prn(1, v_util+","+h_util);
			if(v_util>=h_util){
				t.setHI_only();
				n++;
			}
		}
//		SLog.prn(2, "no:"+n);
	}

		
	@Override
	public double getDtm() {
		double dtm=getDtm2();
		return dtm;
	}
	public double getDtm2() {
		double dtm=hc_hi+lc_ac;
		double dtm2;
		if (dtm<=1)
			return dtm;
		
		double util_max=Math.max(hc_hi, lc_ac);
		if (util_max>1) 
			return util_max;
		
		dtm=lc_ac;
		for(Task t:g_tm.get_HC_Tasks()){
			double v_util=t.getLoUtil()/g_x;
			double h_util=t.getHiUtil();
			dtm+=Math.min(v_util,h_util);
		}
//		SLog.prn(2, "dtm:"+MCal.getStr(dtm)+", "+MCal.getStr(dtm2));
		dtm2=(hc_hi)+g_x*lc_ac;
		return Math.max(dtm, dtm2);
	}
	



	@Override
	public double computeX() {
		double x=(1-hc_hi)/lc_ac;

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
