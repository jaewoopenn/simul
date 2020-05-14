package gen;


import anal.Anal;
import anal.AnalEDF_AD_E;
import task.TaskMng;
import task.TaskSetMC;

public class SysGenMC extends SysGen {
	public SysGenMC(ConfigGen cfg) {
		super(cfg);
	}

	
	@Override
	protected int check() {
		if(!g_isCheck)
			return 1;
		TaskSetMC tsf=new TaskSetMC(g_tg.getTS()) ;
		TaskMng tm=tsf.getTM();
		Anal a=new AnalEDF_AD_E();
		a.init(tm);
		a.prepare();
		if(a.is_sch()) {		
//			Log.prn(1, "check 1");
			return 1;
		}
//		Log.prn(1, "check 0");
		return 0;
	}



}
