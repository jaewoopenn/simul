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
		double cu=g_tm.getInfo().getLo_util();
		double tu=cu*(alpha);
		Log.prn(1,"cu:"+cu+" tu:"+tu+", alpha:"+alpha);
		cu=0;
		Task[] tasks=g_tm.getLoTasks();
		for(int i=tasks.length-1;i>=0;i--){
			Task t=tasks[i];
//			t.prn();
			if(cu>=tu)
				break;
			cu+=t.getLoUtil();
			t.setIsolate(true);
		}
	}
	public void drop() {
		drop_in(maxRes);
	}
	private void drop_in(double lim) {
		while(true){
			double ru=g_tm.getRU();
//			Log.prn(1, "RU:"+ru);
			if(ru>lim){
				int tid=g_tm.findDropTask();
				g_tm.drop(tid);
			}
			else
				break;
			
		}
		
	}
	public void request(double d) {
		double ru=g_tm.getRU();
		double tu=ru-d;
		drop_in(tu);
		
	}
	// prn
	public void prn() {
		Log.prn(1, "cid:"+cid+", alpha:"+alpha);
		Log.prn(1, "maxRes:"+maxRes);
		g_tm.prnComp();
	}
	public void prnOff() {
		Log.prn(1, "cid:"+cid+", alpha:"+alpha+", x:"+g_tm.getInfo().getX());
		Log.prn(1, "init:"+getInitU());
		Log.prn(1, "wc:"+getWCU());
//		Log.prn(1, "ext:"+getExtU());
//		Log.prn(1, "int:"+getIntU());
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
	public void setAlpha(double d) {
//		Log.prn(3, d+"");
		alpha=d;
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
	
	public double getInitU() {
		Task[] tasks=g_tm.getTasks();
		double u=0;
		for(Task t:tasks){
			if(t.is_HI)
				u+=Math.min(t.getLoRUtil(), t.getHiUtil());
			else
				u+=t.getLoUtil();
		}
		return u;
	}
	
	public double getWCU(){
		return Math.max(getExtU(), getIntU());
	}
	public double getExtU() {
		Task[] tasks=g_tm.getTasks();
		double u=0;
		for(Task t:tasks){
			if(t.is_HI)
				u+=Math.min(t.getLoRUtil(), t.getHiUtil());
			else{
				if(t.is_isol())
					u+=t.getLoUtil();
				else
					u+=g_tm.getInfo().getX()*t.getLoUtil();
			}
		}
		return u;
	}
	public double getIntU() {
		Task[] tasks=g_tm.getTasks();
		double u=0;
		for(Task t:tasks){
			if(t.is_HI)
				u+= t.getHiUtil();
			else{
				u+=g_tm.getInfo().getX()*t.getLoUtil();
			}
		}
		return u;
	}
	public double getRU() {
		return g_tm.getRU();
	}
	
}

