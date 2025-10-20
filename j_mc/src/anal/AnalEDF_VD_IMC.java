package anal;

import task.SysInfo;
import task.Task;
import util.MCal;
import util.SLog;

public class AnalEDF_VD_IMC extends Anal {
	private double lctasks_acUtil;
	private double lctasks_deUtil;
	private double hctasks_loutil;
	private double hctasks_hiutil;
	private boolean isWCR=false;
	SysInfo g_info;
	public AnalEDF_VD_IMC() {
		super();
		g_name="EDF-VD-IMC";
	}

	@Override
	public void prepare_in() {
		g_info=g_tm.getInfo();
		lctasks_acUtil=g_info.getUtil_LC_AC();
		lctasks_deUtil=g_info.getUtil_LC_DE();
		hctasks_loutil=g_info.getUtil_HC_LO();
		hctasks_hiutil=g_info.getUtil_HC_HI();
//		g_info.prn();
		isUnsch=false;
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
	protected double getDtm_in() {
		if(isUnsch)
			return 2;
		double dtm=g_info.getMaxUtil();
		if (dtm<=1)
			return dtm;
		double util_max=Math.max(hctasks_hiutil, lctasks_acUtil);
		if (util_max>1) 
			return util_max;

		return getScore();
	}

	public double getScore() {
		double dtm_hi;
		
		
		dtm_hi=g_x*lctasks_acUtil+(1-g_x)*lctasks_deUtil+hctasks_hiutil;
		double dtm_lo=hctasks_loutil/g_x+lctasks_acUtil;
//		SLog.prn(2, "!! dtm:"+MCal.getStr(dtm_lo)+", "+MCal.getStr(dtm_hi));
		if(dtm_lo<=1+MCal.err)
			return dtm_hi;
		else 
			return dtm_lo;
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
	public void setX_in(double x) {
		isDone=true;

		g_x=x;
	}

	@Override
	public double getModX() {
		return -1;
	}

	@Override
	public void auto() {
		if(g_info.get_LO_util()>1||g_info.get_HI_util()>1) {
			isUnsch=true;
			return;
		}
		double x=computeX();
		setX(x);
	}



	
	

}
