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
//		Log.prn(1,"cu:"+cu+" tu:"+tu+", alpha:"+alpha);
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
		drop_in(maxRes,false);
	}
	private double drop_in(double lim, boolean isShared) {
		int tid;
		while(true){
			double ru=g_tm.getRU();
//			Log.prn(1, "RU:"+ru);
			if(ru>lim){
				if(isShared)
					tid=g_tm.findDropTask_shared();
				else
					tid=g_tm.findDropTask();
				if(tid==-1) 
					return ru;
				Log.prn(1, "drop "+tid);
				g_tm.drop(tid);
			}
			else
				return ru;
			
		}
		
	}
	public double request(double d) {
		double ru=g_tm.getRU();
		double tu=ru-d;
		return drop_in(tu,true);
		
	}
	// prn
	public void prn() {
		Log.prn(1, "cid:"+cid+", alpha:"+alpha);
		Log.prn(1, "maxRes:"+maxRes);
		g_tm.prnComp();
	}
	public void prnOff() {
		Log.prn(1, "cid:"+cid+", alpha:"+alpha+", x:"+g_tm.getInfo().getX());
		Log.prn(1, "init:"+getST_U());
		Log.prn(1, "wc:"+getWC_U());
		Log.prn(1, "ext:"+getExt_U());
		Log.prn(1, "int:"+getInt_U());
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
	
	public double getST_U() {
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
	
	public double getWC_U(){
		return Math.max(getExt_U(), getInt_U());
	}
	public double getExt_U() {
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
	public double getInt_U() {
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
	public double getMaxRes() {
		return maxRes;
	}
	
}

