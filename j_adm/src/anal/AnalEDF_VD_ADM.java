package anal;

import task.SysInfo;
import task.Task;
import task.TaskMng;
import util.SLog;

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
		computeX();
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
		if (hctasks_hiutil>1) return hctasks_hiutil;
		if (lctasks_acUtil>1) return lctasks_acUtil;
		double dtm=hctasks_hiutil+lctasks_acUtil;
		if (dtm<=1)
			return dtm;
		dtm=glo_x*lctasks_acUtil+(1-glo_x)*lctasks_deUtil+hctasks_hiutil;
//		dtm=glo_x*lctasks_acUtil+(1-glo_x)*lctasks_deUtil;
//		for(Task t:g_tm.get_HC_Tasks()){
//			double v_util=t.getLoUtil()/glo_x;
//			double h_util=t.getHiUtil();
//			dtm+=Math.min(v_util,h_util);
//			SLog.prn(2, t.getLoUtil()+","+v_util+", "+h_util);
//		}
		double dtm2=0;
		return Math.max(dtm, dtm2);
	}
	



	@Override
	public double computeX() {
		glo_x=hctasks_loutil/(1-lctasks_acUtil);
		if(glo_x==0)
			glo_x=1;
		return glo_x;
	}
	

	@Override
	public void prn() {
//		SLog.prn(1, "lotask util:"+lotasks_loutil+","+lotasks_hiutil);
//		SLog.prn(1, "hitask util:"+hitasks_loutil+","+hitasks_hiutil);
		SLog.prn(1, "x:"+glo_x);
//		SLog.prn(1, "det:"+getDtm());
		
	}

	@Override
	public double getExtra(int i) {
		return 0;
	}
	

	@Override
	public void reset() {
		
	}
	@Override
	public void setX(double x) {
		glo_x=x;
	}

	
	

}
