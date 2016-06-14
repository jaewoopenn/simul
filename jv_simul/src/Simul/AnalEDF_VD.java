package Simul;

import Basic.Task;
import Util.Log;

public class AnalEDF_VD extends Anal {
	private double lotasks_loutil;
	private double hitasks_loutil;
	private double hitasks_hiutil;
	private double glo_x;

	@Override
	public void prepare() {
		lotasks_loutil=tm.getLoUtil();
		hitasks_loutil=tm.getHiUtil_l();
		hitasks_hiutil=tm.getHiUtil_h();
		glo_x=hitasks_loutil/(1-lotasks_loutil);
		Log.prn(1, "util:"+lotasks_loutil+","+hitasks_loutil+","+hitasks_hiutil);
		Log.prn(1, "x:"+glo_x);
	}
	
	@Override
	public boolean isScheduable() {
		if (hitasks_hiutil>1) return false;
		double dtm=glo_x*lotasks_loutil+hitasks_hiutil;
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


	// no ratio / nl 
	@Override
	public double getDropRate(double p) {
		int size=tm.hi_size();
		double v=1-Math.pow(1-p,size);
		Log.prn(1, v+" "+Math.pow(1-p,size)+" "+size);
		return v;
	}


}
