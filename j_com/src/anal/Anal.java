package anal;

import com.PRM;

import task.TaskSet;
import util.SLog;

public abstract class Anal {
	protected String g_name="";
	protected TaskSet g_tm;
	protected PRM g_prm;
	
	protected int g_limit=10000;
	protected double g_error=0.001;

	public String getName() {
		return g_name;
	}
	public void init(TaskSet mng) {
		g_tm=mng;
	}
	
	public void init(TaskSet mng, PRM p) {
		g_tm=mng;
		g_prm=p;
	}
	public void setPRM(PRM p) {
		g_prm=p;
		
	}
	
	
	public void proceed_if_sch() {
		if(!is_sch()) {
			SLog.err("not schedulable");
		}

	}

	// abs method
	public abstract boolean is_sch();
	public abstract double getExec(int p);
	
	
}
