package anal;

import task.TaskMng;
import util.SLog;

public abstract class Anal {
	protected String g_name="";
	protected TaskMng g_tm;
	protected int g_limit=10000;
	protected boolean isDone=false;
	protected boolean isUnsch=false;
	protected double g_x=-1;

	public void init(TaskMng mng) {
//		Log.prn(1, "Algo:"+g_name);
		g_tm=mng;
		prepare();
	}
	public void prepare() {
		isUnsch=false;
		g_x=-1;
		prepare_in();
	}
	public String getName() {
		return g_name;
	}
	
	public double getDtm() {
		if(!isDone) {
			SLog.err("Anal: not ready");
		}
		if(isUnsch)
			return 2;
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
	public void setX(double x) {
		if(x<=0||x>1) {
			isUnsch=true;
		}
//		try {
//			if(x<=0||x>1) {
//				isUnsch=true;
//				throw new Exception();
//			}
//		} 
//		catch(Exception e) {
//			e.printStackTrace();
//			SLog.err("anal... x:"+x);
//		}
		if(!isUnsch)
			setX_in(x);

	}
	// abs method
	public abstract void prn();
	public abstract void reset();
	public abstract double getModX();
	protected abstract void prepare_in();
	public abstract void auto();
	protected abstract double getDtm_in();
	public abstract double computeX();
	
	public abstract void setX_in(double x);
	
}
