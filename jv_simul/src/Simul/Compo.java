package Simul;


import Util.Log;

public class Compo  {
	TaskMng tm;
	private double gamma;
	private double lotasks_loutil;
	private double hitasks_loutil;
	private double hitasks_hiutil;
	private double glo_x;

	public void init(TaskMng mng) {
		tm=mng;
	}
	public void prepare(double g) {
		gamma=g;
		lotasks_loutil=tm.getLoUtil();
		hitasks_loutil=tm.getHiUtil_l();
		hitasks_hiutil=tm.getHiUtil_h();
		glo_x=hitasks_loutil/(1-lotasks_loutil);
		Log.prn(1, "util:"+lotasks_loutil+","+hitasks_loutil+","+hitasks_hiutil);
		Log.prn(1, "x:"+glo_x);
	}
	public void process(){
		double lo_det=lotasks_loutil+hitasks_loutil/glo_x;
		double hi_det=glo_x*lotasks_loutil+hitasks_hiutil;
		Log.prn(1, "lo_det:"+lo_det);
		Log.prn(1, "hi_det:"+hi_det);
		double beta=Math.max(lo_det,hi_det);
		Log.prn(1, "beta:"+beta);
	}
	public boolean isScheduable() {
		double dtm=lotasks_loutil;
		for(int i=0;i<tm.hi_size();i++){
			Task t=tm.getHiTask(i);
			double v_util=t.c_l*1.0/t.period/glo_x;
//			double h_util=t.c_h*1.0/t.period;
//			Log.prn(1,"v h:"+v_util+","+h_util);
//			dtm+=Math.min(v_util,h_util);
			dtm+=v_util;
		}
		Log.prn(1,"det:"+dtm);
		if (dtm <=1) {
			//Log.prn(1, "Sch OK");
			return true;
		}
		return false;
	}

	
	public double getX() {
		return glo_x;
	}


}
