package Simul;

import Util.Log;
// 
import Util.MUtil;
public class Analysis {
	public static int analEDF(TaskMng mng) {
		if(!mng.isFinal()) {
			Log.prn(1,"task set is not finalized");
			return -1;
		}
		AnalEDF a=new AnalEDF();
		a.init(mng);
		a.prepare();
		return MUtil.btoi(a.isScheduable());
	}
	public static int analEDF_VD(TaskMng mng) {
		AnalEDF_VD a=new AnalEDF_VD();
		a.init(mng);
		a.prepare();
		return MUtil.btoi(a.isScheduable());
	}
	public static int analEDF_TM(TaskMng mng) {
		AnalEDF_TM a=new AnalEDF_TM();
		a.init(mng);
		a.prepare();
		return MUtil.btoi(a.isScheduable());
	}



}
