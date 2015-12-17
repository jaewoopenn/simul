package Simul;

import Util.Log;
import Util.MUtil;

public class Analysis {
	public static int analEDF(TaskMng mng) {
		if(checkErr(mng)) return -1;
		AnalEDF a=new AnalEDF();
		a.init(mng);
		a.prepare();
		return MUtil.btoi(a.isScheduable());
	}
	public static int analEDF_VD(TaskMng mng) {
		if(checkErr(mng)) return -1;
		AnalEDF_VD a=new AnalEDF_VD();
		a.init(mng);
		a.prepare();
		return MUtil.btoi(a.isScheduable());
	}
	public static int analEDF_TM(TaskMng mng) {
		if(checkErr(mng)) return -1;
		AnalEDF_TM a=new AnalEDF_TM();
		a.init(mng);
		a.prepare();
		return MUtil.btoi(a.isScheduable());
	}
	public static int getRespEDF(TaskMng mng) {
		if(checkErr(mng)) return -1;
		AnalEDF a=new AnalEDF();
		a.init(mng);
		return a.getResp();
	}
	public static double getDrop_EDF_VD(TaskMng mng,double prob_hi) {
		if(checkErr(mng)) return -1;
		AnalEDF_VD a=new AnalEDF_VD();
		a.init(mng);
		double drop=a.getDropRate(prob_hi);
		Log.prn(1, "prob_hi:"+prob_hi+" drop:"+drop);
		return drop;
	}
	public static double getDrop_EDF_TM(TaskMng mng,double prob_hi) {
		if(checkErr(mng)) return -1;
		AnalEDF_TM a=new AnalEDF_TM();
		a.init(mng);
		a.prepare();
		double drop=a.getDropRate(prob_hi);
		Log.prn(1, "prob_hi:"+prob_hi+" drop:"+drop);
		return drop;
	}

	
	public static int getRespEDF_VD(TaskMng mng) {
		if(checkErr(mng)) return -1;
		AnalEDF_VD a=new AnalEDF_VD();
		a.init(mng);
		return a.getResp();
	}
	public static int getRespEDF_TM(TaskMng mng) {
		if(checkErr(mng)) return -1;
		AnalEDF_VD a=new AnalEDF_VD();
		a.init(mng);
		int VD_v= a.getResp();
		
		AnalEDF_TM b=new AnalEDF_TM();
		b.init(mng);
		int TM_v=b.getResp();
		Log.prn(2, "v:"+VD_v+","+TM_v);

		return Math.min(VD_v, TM_v);
	}
	
	public static boolean checkErr(TaskMng mng){
		if(!mng.isFinal()) {
			Log.prn(1,"task set is not finalized");
			return true;
		}
		return false;
	}


}
