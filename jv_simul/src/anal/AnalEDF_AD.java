package anal;

import basic.Task;
import basic.TaskSetInfo;
import util.Log;
import util.MUtil;

public class AnalEDF_AD extends Anal {
	private double g_lt_lu;
	private double g_ht_lu;
	private double g_ht_hu;
	private double glo_x;
	private int n_hi_prefer;
	TaskSetInfo g_info;
	public AnalEDF_AD() {
		super();
		name="AD";
	}
	public void prepare() {
		load();
		comp_X();
	}
	
	private void comp_X() {
		glo_x=g_ht_lu/(1-g_lt_lu);
	}

	private void load() {
		g_info=g_tm.getInfo();
		g_lt_lu=g_info.getLo_util();
		g_ht_lu=g_info.getHi_util_lm();
		g_ht_hu=g_info.getHi_util_hm();
	}
	
	
	@Override
	public double getDtm() {
		double dtm=glo_x*g_lt_lu;
		for(Task t:g_tm.getTasks()){
			if (!t.is_HI)
				continue;
			double v_util=t.getLoUtil()/glo_x;
			double h_util=t.getHiUtil();
//			Log.prn(1,"v h:"+v_util+","+h_util);
			dtm+=Math.max(v_util,h_util);
		}
		Log.prn(1,"det:"+dtm);
		return dtm;
	}


	
	

	public double getX() {
		return glo_x;
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
