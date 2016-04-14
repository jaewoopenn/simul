package Simul;

import Util.Log;

public class AnalEDF extends Anal {
	private double u;
	@Override
	public void prepare() {
		u=tm.getUtil();
		Log.prn(1, "det:"+u);
	}
	
	@Override
	public boolean isScheduable() {
		if(u<=1)
			return true;
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
		return 0;
	}




}
