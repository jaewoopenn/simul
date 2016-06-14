package Simul;

import Basic.Task;
import Util.Log;

public class AnalEDF_Greedy extends Anal {
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
		glo_x=Math.min(1, cal_x);
		Log.prn(1, "util:"+lotasks_loutil+","+hitasks_loutil+","+hitasks_hiutil);
		Log.prn(1, "x:"+glo_x);
	}
	
	@Override
	public boolean isScheduable() {
		if (hitasks_hiutil>1) return false;
		double dtm=lotasks_loutil+hitasks_loutil/glo_x;
		Log.prn(1,"det:"+dtm);
		if (dtm <=1) {
			//Log.prn(1, "Sch OK");
			return true;
		}
		return false;
	}

	@Override
	public int getResp() {
		double old=0;
		double v=0;
		while(true)
		{
			v=0;
			for(int i=0;i<tm.size();i++){
				Task t=tm.getTask(i);
				if(!t.is_HI) continue;
				v+=Math.ceil((old+1)/t.period)*t.c_h;
//				Log.prn(1, "v,"+i+":"+v);
			}
//			Log.prn(1, "v:"+v);
			if(v==old) break;
			if(v>g_limit) break;
			old=v;
		}
		return (int)v;
	}


	@Override
	public double getDropRate(double prob_hi) {
		int size=tm.hi_size();
		double v=1-Math.pow(1-prob_hi,size);
		//Log.prn(1, v+" "+Math.pow(1-prob_hi,size)+" "+size);
		return v;
	}


}
