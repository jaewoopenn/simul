package Simul;

import Basic.Task;
import Basic.TaskSetInfo;
import Util.Log;
import Util.MUtil;

public class AnalEDF_TM_E extends Anal {
	private double lotasks_loutil;
	private double hitasks_loutil;
	private double hitasks_hiutil;
	private double glo_x;
	private int n_skip;
	TaskSetInfo g_info;
	@Override
	public void prepare() {
		g_info=tm.getInfo();
		lotasks_loutil=g_info.getLo_util();
		hitasks_loutil=g_info.getHi_util_lm();
		hitasks_hiutil=g_info.getHi_util_hm();
		glo_x=hitasks_loutil/(1-lotasks_loutil);
		Log.prn(1, "util:"+lotasks_loutil+","+hitasks_loutil+","+hitasks_hiutil);
		n_skip=0;
		for(int i=0;i<g_info.getHi_size();i++){
			Task t=tm.getHiTask(i);
			double v_util=t.c_l*1.0/t.period/glo_x;
			double h_util=t.c_h*1.0/t.period;
//			Log.prn(1, v_util+","+h_util);
			if(v_util>=h_util)
				n_skip++;
		}
		Log.prn(1, "util:"+lotasks_loutil+","+hitasks_loutil+","+hitasks_hiutil);
		Log.prn(1, "x:"+glo_x);
		Log.prn(1, "n_skip:"+n_skip);
	}
	
	@Override
	public boolean isScheduable() {
		double dtm=lotasks_loutil;
		for(int i=0;i<g_info.getHi_size();i++){
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



	
	@Override
	public double getDropRate(double p) {
		if(lotasks_loutil==0) 
			return 0; 
		double exp_drop_sum=0;
		int drop=0;
		double exp_drop=0;
		int nf=g_info.getHi_size()-n_skip;
		for(int i=0;i<=nf;i++){
			drop=MUtil.combi(nf, i)*maxDrop(i);
			exp_drop=Math.pow(1-p, i)*Math.pow(p, nf-i)*drop;
//			Log.prn(1, i+" "+drop+" "+exp_drop);
			exp_drop_sum+=exp_drop;
		}
		int num=g_info.getLo_size();
		return exp_drop_sum/num;
	}
	private int maxDrop(int k){
		double req_util=getReq(k);
		double lo_util=lotasks_loutil;
		int drop=0;
		for(int i=0;i<g_info.getSize();i++){
			Task t=tm.getTask(i);
			if (t.is_HI)
				continue;
			if(req_util+lo_util>1){
				drop++;
				lo_util-=(1-glo_x)*t.c_l/t.period;
			}
			else
				break;
			
		}
//		Log.prn(1, "__ "+k+" "+drop+" "+req_util+" "+lo_util);
		
		return drop;
	}
	
	private double getReq(int k){ // Required utilization for k HI-behavior 
		int nf=g_info.getHi_size()-n_skip;
		double req_util=0;
		int cur=0;
		
		for(int i=0;i<g_info.getHi_size();i++){
			Task t=tm.getHiTask(i);
			double v_util=t.c_l*1.0/t.period/glo_x;
			double h_util=t.c_h*1.0/t.period;
			if(v_util>=h_util)
				req_util+=h_util;
			else {
				if (cur<nf-k) {
					req_util+=h_util;
					cur++;
				} else {
					req_util+=v_util;
				}
			}
		}
		
		
		return req_util;
	}

	public double getX() {
		return glo_x;
	}
}
