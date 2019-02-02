package gen;


import anal.Anal;
import anal.AnalEDF_VD;
import basic.TaskMng;
import basic.TaskSetEx;

public class SysGenMC extends SysGen {
	public SysGenMC(ConfigGen cfg) {
		super(cfg);
	}

	
	@Override
	protected int check() {
		if(!g_isCheck)
			return 1;
		TaskSetEx tsf=new TaskSetEx(g_tg.getAll()) ;
		TaskMng tm=tsf.getTM();
		Anal a=new AnalEDF_VD();
		a.init(tm);
		a.prepare();
		if(a.isScheduable()) {		
//			Log.prn(1, "check 1");
			return 1;
		}
//		Log.prn(1, "check 0");
		return 0;
	}



}
