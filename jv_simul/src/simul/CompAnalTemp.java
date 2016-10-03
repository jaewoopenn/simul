package simul;


import basic.TaskMng;
import basic.TaskSetInfo;
import utilSim.Log;

public class CompAnalTemp  {
	TaskMng tm;
	private double lotasks_loutil;
	private double hitasks_loutil;
	private double hitasks_hiutil;
	private double glo_x;

	public void init(TaskMng mng) {
		tm=mng;
		TaskSetInfo info=tm.getInfo();
		lotasks_loutil=info.getLo_util();
		hitasks_loutil=info.getHi_util_lm();
		hitasks_hiutil=info.getHi_util_hm();
	}
	public void compute_X() {
//		glo_x=hitasks_loutil/(1-lotasks_loutil);
		glo_x=Math.min(1,  (1-hitasks_hiutil)/lotasks_loutil);

		Log.prn(1, "util:"+lotasks_loutil+","+hitasks_loutil+","+hitasks_hiutil);
		Log.prn(1, "x:"+glo_x);
	}
	public void help(){
//		Log.prn(1, "x:"+glo_x);
		double alpha=0.4;
		double lo_det=lotasks_loutil+hitasks_loutil/glo_x;
		double hi_det=glo_x*lotasks_loutil+hitasks_hiutil;
		Log.prn(1, "lo_det:"+lo_det);
		Log.prn(1, "hi_det:"+hi_det);
		double delta=Math.max(0, glo_x*hitasks_hiutil-hitasks_loutil
				-glo_x*(1-glo_x)*lotasks_loutil);
		Log.prn(1, "delta:"+delta);
		double re_lo_det=lotasks_loutil+(hitasks_loutil+delta)/glo_x;
		Log.prn(1, "re_lo_det:"+re_lo_det);
		double re_hitasks_loutil=hitasks_loutil+alpha*delta;
		Log.prn(1, "hitasks_loutil:"+hitasks_loutil);
		Log.prn(1, "re_hitasks_lo_util:"+re_hitasks_loutil
				+", alpha:"+alpha);
		Log.prn(1, "strong_hitasks_lo_util:"+(hitasks_loutil+delta));
	}
	
	public double get_X() {
		return glo_x;
	}
	public void set_X(double d) {
		glo_x=d;
	}


}
