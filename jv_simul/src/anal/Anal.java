package anal;

import basic.TaskMng;
import util.Log;

public abstract class Anal {
	protected String g_name="";
	protected TaskMng g_tm;
	protected int g_limit=10000;
	public void init(TaskMng mng) {
//		Log.prn(1, "Algo:"+g_name);
		g_tm=mng;
	}
	public String getName() {
		return g_name;
	}
	public boolean isScheduable()
	{
		double dtm=getDtm();
		if (dtm <=1) {
			//Log.prn(1, "Sch OK");
			return true;
		}
		return false;
	}

	// abs method
	public abstract void prn();
	public abstract void prepare();
	public abstract double getDtm();
	public abstract double computeX();
	public abstract double getExtra(int i);
	
}
