package anal;

import basic.Task;
import basic.TaskMng;
import basic.TaskSetInfo;
import util.Log;
import util.MUtil;

public class AnalEDF_AD_E extends Anal {
	private double g_lt_lu;
	private double g_ht_lu;
	private double g_ht_hu;
	private double glo_x;
	private int n_hi_prefer;
	TaskSetInfo g_info;
	public AnalEDF_AD_E() {
		super();
		name="AD-E";
	}
	@Override
	public void prepare() {
		load();
		comp_X();
		comp_hi_prefer();
	}
	
	private void comp_X() {
//		Log.prn(2, g_ht_hu+","+g_lt_lu);
		double cal_x=(1-g_ht_hu)/g_lt_lu;
		glo_x=Math.min(1,cal_x);
	}

	private void load() {
		g_info=g_tm.getInfo();
		g_lt_lu=g_info.getLo_util();
		g_ht_lu=g_info.getHi_util_lm();
		g_ht_hu=g_info.getHi_util_hm();
	}

	
	private void comp_hi_prefer() {
		n_hi_prefer=0;
		for(Task t:g_tm.getHiTasks()){
			double v_util=t.getLoUtil()/glo_x;
			double h_util=t.getHiUtil();
//			Log.prn(1, v_util+","+h_util);
			if(v_util>=h_util)
				n_hi_prefer++;
		}
	}
	
	@Override
	public double getDtm() {
		if(g_ht_hu>1){
			return g_ht_hu;
		}
		if(g_lt_lu>1){
			return g_lt_lu;
		}
		double dtm=g_lt_lu;
		for(Task t:g_tm.getHiTasks()){
			double v_util=t.getLoUtil()/glo_x;
			double h_util=t.getHiUtil();
			dtm+=Math.min(v_util,h_util);
		}
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
	@Override
	public void prn() {
		Log.prnc(1, "ll:"+MUtil.getStr(g_lt_lu));
		Log.prnc(1, " hl:"+MUtil.getStr(g_ht_lu));
		Log.prn(1, " hh:"+MUtil.getStr(g_ht_hu));
		Log.prnc(1, "x:"+glo_x);
		Log.prn(1, "hi_prefer:"+n_hi_prefer);
		
	}
}
