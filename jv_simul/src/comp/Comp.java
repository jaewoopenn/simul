package comp;


import utilSim.Log;
import basic.Task;
import basic.TaskMng;


public class Comp {
	private int cid;
	private double alpha;
	private double maxRes;
	private TaskMng g_tm;
	
	public Comp(double alpha) {
		super();
		this.alpha = alpha;
	}
	public void setTM(TaskMng tm){
		g_tm=tm;
	}

	public void prn() {
		Log.prn(1, "cid:"+cid+", alpha:"+alpha);
		g_tm.prn();
	}
	public void setID(int cid) {
		this.cid = cid;
	}

	public Task[] getTasks() {
		return g_tm.getTasks();
	}

	public int size() {
		return g_tm.size();
	}

	public int getID() {
		return cid;
	}
	public double getAlpha() {
		return alpha;
	}
}

