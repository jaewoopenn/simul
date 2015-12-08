package Simul;

// 
import Util.MUtil;
public class Analysis {
	public static int analEDF(TaskMng mng) {
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



}
