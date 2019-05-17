package anal;

import task.TaskMng;
import util.SLog;

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
	
	public boolean is_sch()
	{
		double dtm=getDtm();
		if (dtm <=1) {
			//Log.prn(1, "Sch OK");
			return true;
		}
		return false;
	}
	
	public void proceed_if_sch() {
		if(!is_sch()) {
			SLog.err("not schedulable");
		}

	}

	// abs method
	public abstract void prn();
	public abstract void prepare();
	public abstract double getDtm();
	public abstract double computeX();
	public abstract double getExtra(int i);
	
}
