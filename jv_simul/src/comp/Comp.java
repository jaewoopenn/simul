package comp;


import utilSim.Log;
import basic.Task;
import basic.TaskMng;


public class Comp {
	private int cid;
	private double alpha;
	private double maxRes;
	private double curRes;
	private TaskMng g_tm;
	
	public Comp(double alpha) {
		super();
		this.alpha = alpha;
	}
	public void partition() {
		double tu=g_tm.getInfo().getLo_util()*alpha;
		Log.prn(1,"tu:"+tu);
		double cu=0;
		for(Task t:g_tm.getLoTasks()){
			t.prn();
			cu+=t.getLoUtil();
			t.setIsolate(true);
			if(cu>=tu)
				break;
		}
	}

	public void prn() {
		Log.prn(1, "cid:"+cid+", alpha:"+alpha);
		Log.prn(1, "maxRes:"+maxRes);
		g_tm.prnComp();
	}
	
	// set
	public void setTM(TaskMng tm){
		g_tm=tm;
	}
	public void setID(int cid) {
		this.cid = cid;
	}
	public void setMaxRes(double d) {
		maxRes=d;
	}
	
	// get
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
	public TaskMng getTM() {
		return g_tm;
	}
	public void drop() {
		while(true){
			double ru=g_tm.getRU();
//			Log.prn(1, "RU:"+ru);
			if(ru>maxRes){
				int tid=g_tm.findDropTask();
				g_tm.drop(tid);
			}
			else
				break;
			
		}
	}
}

