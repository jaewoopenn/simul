package Simul;

import Basic.TaskMng;
import Util.Log;
import Util.MUtil;

public class Analysis {
	public static int anal_EDF(TaskMng mng) {
		if(checkErr(mng)) return -1;
		AnalEDF a=new AnalEDF();
		a.init(mng);
		a.prepare();
		return MUtil.btoi(a.isScheduable());
	}
	public static int anal_ICG(TaskMng mng) {
		if(checkErr(mng)) return -1;
		AnalICG a=new AnalICG();
		a.init(mng);
		a.prepare();
		return MUtil.btoi(a.isScheduable());
	}

	public static int anal_EDF_VD(TaskMng mng) {
		if(checkErr(mng)) return -1;
		AnalEDF_VD a=new AnalEDF_VD();
		a.init(mng);
		a.prepare();
		return MUtil.btoi(a.isScheduable());
	}
	public static int anal_EDF_TM(TaskMng mng) {
		if(checkErr(mng)) return -1;
		AnalEDF_TM a=new AnalEDF_TM();
		a.init(mng);
		a.prepare();
		return MUtil.btoi(a.isScheduable());
	}
	public static int anal_EDF_TM_S(TaskMng mng) {
		if(checkErr(mng)) return -1;
		AnalEDF_TM_S a=new AnalEDF_TM_S();
		a.init(mng);
		a.prepare();
		return MUtil.btoi(a.isScheduable());
	}
	public static double getScore_EDF_VD(TaskMng mng) {
		if(checkErr(mng)) return -1;
		AnalEDF_VD a=new AnalEDF_VD();
		a.init(mng);
		a.prepare();
		return a.getScore();
	}
	
	public static double getDrop_EDF_VD(TaskMng mng,double prob_hi) {
		if(checkErr(mng)) return -1;
		AnalEDF_VD a=new AnalEDF_VD();
		a.init(mng);
		double drop=a.getDropRate(prob_hi);
		Log.prn(1, "prob_hi:"+prob_hi+" drop:"+drop);
		return drop;
	}
	public static double getDrop_EDF_TM_E(TaskMng mng,double prob_hi) {
		if(checkErr(mng)) return -1;
		AnalEDF_TM_E a=new AnalEDF_TM_E();
		a.init(mng);
		a.prepare();
		double drop=0;
		if(a.getX()!=1)
			drop=a.getDropRate(prob_hi);
		Log.prn(1, "prob_hi:"+prob_hi+" drop:"+drop);
		return drop;
	}

	public static double getDrop_EDF_TM_S(TaskMng mng,double prob_hi) {
		if(checkErr(mng)) return -1;
		AnalEDF_TM_S a=new AnalEDF_TM_S();
		a.init(mng);
		a.prepare();
		double drop=0;
		if(a.getX()!=1)
			drop=a.getDropRate(prob_hi);
		Log.prn(1, "prob_hi:"+prob_hi+" drop:"+drop);
		return drop;
	}

	

	public static boolean checkErr(TaskMng mng){
		return false;
	}


}
