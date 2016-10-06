package simul;

import basic.TaskMng;

public abstract class Anal {
	TaskMng tm;
	int g_limit=10000;
	public void init(TaskMng mng) {
		tm=mng;
	}
	public abstract void prepare();
	public abstract double getDtm();
	public boolean isScheduable()
	{
		double dtm=getDtm();
		if (dtm <=1) {
			//Log.prn(1, "Sch OK");
			return true;
		}
		return false;
	
	}
	public abstract double getDropRate(double p);

}
