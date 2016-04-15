package Simul;

import Util.Log;
import Util.MUtil;

public class AnalEDF_TM_S extends Anal {
	private double lotasks_loutil;
	private double hitasks_loutil;
	private double hitasks_hiutil;
	private double glo_x;

	@Override
	public void prepare() {
		lotasks_loutil=tm.getLoUtil();
		hitasks_loutil=tm.getHiUtil_l();
		hitasks_hiutil=tm.getHiUtil_h();
		double cal_x=(1-hitasks_hiutil)/lotasks_loutil;
		glo_x=Math.min(1,cal_x);
		Log.prn(1, "util:"+lotasks_loutil+","+hitasks_loutil+","+hitasks_hiutil);
		Log.prn(1, "x:"+glo_x);
	}
	
	@Override
	public boolean isScheduable() {
		double dtm=lotasks_loutil;
		for(int i=0;i<tm.size();i++){
			Task t=tm.getTask(i);
			if (!t.is_HI)
				continue;
			double v_util=t.c_l*1.0/t.period/glo_x;
			double h_util=t.c_h*1.0/t.period;
//			Log.prn(1,"v h:"+v_util+","+h_util);
			dtm+=Math.min(v_util,h_util);
		}
		Log.prn(1,"det:"+dtm);
		if (dtm <=1) {
			//Log.prn(1, "Sch OK");
			return true;
		}
		return false;
	}

	@Override
	public int getResp() {
		int maxV=0;
		for(int i=0;i<tm.size();i++){
			Task t=tm.getTask(i);
			maxV=Math.max(maxV,t.period);
		}
		return maxV;
	}

	private int getNum(double u){
		double ul=0;
		double ud=0;
		int num=0;
		for(int j=0;j<tm.size();j++){
			Task t=tm.getTask(j);
			if(t.is_HI) 
				continue;
			double add=t.c_l*1.0/t.period;
			if (ul+add<=1-u+MUtil.err) {
				ul+=add;
			} else {
				ud+=add;
				num++;
			}
		}
		return num;
		
	}
	private int maxDrop(int k){
		return 1;
	}
	
	@Override
	public double getDropRate(double p) {
		int hi_size=tm.hi_size();
		double sum_drop=0;
		int drop=0;
		for(int i=0;i<=hi_size;i++){
			drop=MUtil.combi(hi_size, i)*maxDrop(i);
			Log.prn(1, i+" "+drop);
			sum_drop+=drop;
		}
		int num=tm.lo_size();
		return sum_drop/num;
	}
}
