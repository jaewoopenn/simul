package anal;

import basic.Task;
import basic.TaskMng;
import basic.TaskSetInfo;
import util.Log;
import util.MUtil;

public class AnalEDF_AD_E extends Anal {
	private double lotasks_loutil;
	private double hitasks_loutil;
	private double hitasks_hiutil;
	private double glo_x;
	private int n_skip;
	TaskSetInfo g_info;
	public AnalEDF_AD_E() {
		super();
		name="AT-S";
	}
	@Override
	public void prepare() {
		g_info=tm.getInfo();
		lotasks_loutil=g_info.getLo_util();
		hitasks_loutil=g_info.getHi_util_lm();
		hitasks_hiutil=g_info.getHi_util_hm();
		double cal_x=(1-hitasks_hiutil)/lotasks_loutil;
		glo_x=Math.min(1,cal_x);
		n_skip=0;
		for(Task t:tm.getHiTasks()){
			double v_util=t.getLoUtil()/glo_x;
			double h_util=t.getHiUtil();
//			Log.prn(1, v_util+","+h_util);
			if(v_util>=h_util)
				n_skip++;
		}
		Log.prnc(1, "ll:"+MUtil.getStr(lotasks_loutil));
		Log.prnc(1, " hl:"+MUtil.getStr(hitasks_loutil));
		Log.prn(1, " hh:"+MUtil.getStr(hitasks_hiutil));
		Log.prnc(1, "x:"+glo_x);
		Log.prn(1, "n_skip:"+n_skip);
	}
	
	@Override
	public double getDtm() {
		double dtm=lotasks_loutil;
		for(Task t:tm.getHiTasks()){
			double v_util=t.getLoUtil()/glo_x;
			double h_util=t.getHiUtil();
//			Log.prn(1,"v h:"+v_util+","+h_util);
			dtm+=Math.min(v_util,h_util);
		}
		Log.prn(1,"det:"+dtm);
		return dtm;
	}




	

	public double getX() {
		return glo_x;
	}
	public static double computeX(TaskMng tm) {
		AnalEDF_AD_E a=new AnalEDF_AD_E();
		a.init(tm);
		a.prepare();
		return a.getX();
	}
}
