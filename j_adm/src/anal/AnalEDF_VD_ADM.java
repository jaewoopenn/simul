package anal;

import task.SysInfo;
import task.Task;
import task.TaskMng;
import util.SLog;

// todo EDF로 스케줄 가능할때, 전부 HI-only로 맞추기



public class AnalEDF_VD_ADM extends Anal {
	private double lctasks_acUtil;
	private double lctasks_deUtil;
	private double hctasks_loutil;
	private double hctasks_hiutil;
	private double glo_x;
	SysInfo g_info;
	public AnalEDF_VD_ADM() {
		super();
		g_name="EDF-VD-ADM";
	}

	@Override
	public void prepare() {
		g_info=g_tm.getInfo();
		lctasks_acUtil=g_info.getUtil_LC();
		lctasks_deUtil=g_info.getUtil_DeLC();
		hctasks_loutil=g_info.getUtil_HC_LO();
		hctasks_hiutil=g_info.getUtil_HC_HI();
		glo_x=computeX();
//		comp_hi_prefer();
	}

//	private void comp_hi_prefer() {
//		for(Task t:g_tm.get_HC_Tasks()){
//			double v_util=t.getLoUtil()/glo_x;
//			double h_util=t.getHiUtil();
////			Log.prn(1, v_util+","+h_util);
//			if(v_util>=h_util){
//				t.setHI_only();
//			}
//		}
//	}
		
	@Override
	public double getDtm() {
		double dtm=hctasks_hiutil+lctasks_acUtil;
		if (dtm<=1)
			return dtm;
		
		double util_max=Math.max(hctasks_hiutil, lctasks_acUtil);
		if (util_max>1) 
			return util_max;
		
		dtm=glo_x*lctasks_acUtil+(1-glo_x)*lctasks_deUtil+hctasks_hiutil;
//		dtm=glo_x*lctasks_acUtil+(1-glo_x)*lctasks_deUtil;
//		for(Task t:g_tm.get_HC_Tasks()){
//			double v_util=t.getLoUtil()/glo_x;
//			double h_util=t.getHiUtil();
//			dtm+=Math.min(v_util,h_util);
//			SLog.prn(2, t.getLoUtil()+","+v_util+", "+h_util);
//		}
		double dtm2=hctasks_loutil/glo_x+lctasks_acUtil;
		return Math.max(dtm, dtm2);
	}
	



	@Override
	public double computeX() {
		double x=hctasks_loutil/(1-lctasks_acUtil);
		if(x==0)
			x=1;
		return x;
	}
	

	@Override
	public void prn() {
//		SLog.prn(1, "lotask util:"+lotasks_loutil+","+lotasks_hiutil);
//		SLog.prn(1, "hitask util:"+hitasks_loutil+","+hitasks_hiutil);
		SLog.prn(1, "x:"+glo_x);
		double dtm=glo_x*lctasks_acUtil+(1-glo_x)*lctasks_deUtil+hctasks_hiutil;
		double dtm2=hctasks_loutil/glo_x+lctasks_acUtil; // LO mode 
		SLog.prn(1, "det:"+dtm2+","+dtm);
		
	}

	

	@Override
	public void reset() {
		
	}
	@Override
	public void setX(double x) {
		glo_x=x;
	}

	
	

}
