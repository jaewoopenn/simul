package anal;

import task.TaskMng;
import util.SLog;

public abstract class Anal {
	protected String g_name="";
	protected TaskMng g_tm;
	protected int g_limit=10000;
	protected boolean isDone=false;
	protected double g_x=-1;

	public void init(TaskMng mng) {
//		Log.prn(1, "Algo:"+g_name);
		g_tm=mng;
		prepare();
	}
	public String getName() {
		return g_name;
	}
	
	public double getDtm() {
		if(!isDone) {
			SLog.err("Anal: not ready");
		}
		return getDtm_in();
	}
	public boolean is_sch()
	{
		double dtm=getDtm_in();
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
	public  double getX() {
		return g_x;
	}

	// abs method
	public abstract void prn();
	public abstract void reset();
	public abstract double getModX();
	protected abstract void prepare();
	public abstract void auto();
	protected abstract double getDtm_in();
	public abstract double computeX();
	public abstract void setX(double x);
	
}
