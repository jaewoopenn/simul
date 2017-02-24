package anal;

import basic.TaskMng;

public abstract class Anal {
	protected String name="";
	protected TaskMng g_tm;
	protected int g_limit=10000;
	public void init(TaskMng mng) {
		g_tm=mng;
	}
	public String getName() {
		return name;
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
	public abstract double getX();
}
