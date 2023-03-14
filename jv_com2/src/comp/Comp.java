package comp;


import task.Task;
import task.TaskMng;
import util.SLog;


public class Comp {
	private int cid;
	private double alpha;
	private double maxRes;
//	private double curRes;
	private TaskMng g_tm;
	
	public Comp(double alpha) {
		super();
		this.alpha = alpha;
	}
	public void partition() {
		double cu=g_tm.getInfo().getUtil_LC();
		double tu=cu*(alpha);
		SLog.prnc(2,"cu:"+cu+" tu:"+tu+", alpha:"+alpha);
		cu=0;
		Task[] tasks=g_tm.getLoTasks();
		for(int i=tasks.length-1;i>=0;i--){
//		for(int i=0;i<tasks.length;i++){
			Task t=tasks[i];
//			t.prn();
			if(cu>=tu)
				break;
			cu+=t.getLoUtil();
			t.set_isol(true);
		}
		SLog.prn(2,", isol:"+cu);
	}
	// prn
	public void prn() {
		SLog.prn(1, "cid:"+cid+", alpha:"+alpha+", x:"+g_tm.getInfo().getX());
		SLog.prn(1, "maxRes:"+maxRes);
		g_tm.prn();
	}
	public void prnOff() {
		SLog.prn(1, "cid:"+cid+", alpha:"+alpha+", x:"+g_tm.getInfo().getX());
		SLog.prn(1, "init:"+getST_U());
		SLog.prn(1, "wc:"+getWC_U());
		SLog.prn(1, "ext:"+getExt_U());
		SLog.prn(1, "int:"+getInt_U());
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
//		SLog.prn(3, d+"");
		alpha=d;
	}
	
	// get
	public Task[] getTaskSet() {
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
		double u=0;
		for(Task t:g_tm.getTasks()){
			if(t.isHC())
				u+=Math.min(t.getLoVdUtil(), t.getHiUtil());
			else
				u+=t.getLoUtil();
		}
		return u;
	}
	public double getST_U2() {
		double u=0;
		for(Task t:g_tm.getTasks()){
			if(t.isHC())
				u+=t.getLoVdUtil();
			else
				u+=t.getLoUtil();
		}
		return u;
	}	
	public double getWC_U(){
		return Math.max(getExt_U(), getInt_U());
	}
	public double get_isol_U(){
		double u=0;
		for(Task t:g_tm.getTasks()){
			if(t.isHC())
				u+= t.getHiUtil();
			else{
				if(t.is_isol())
					u+=t.getLoUtil();
				else
					u+=g_tm.getInfo().getX()*t.getLoUtil();
			}
		}
		return u;
	}
	public double getExt_U() {
		double u=0;
		for(Task t:g_tm.getTasks()){
			if(t.isHC())
				u+=Math.min(t.getLoVdUtil(), t.getHiUtil());
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
		double u=0;
		for(Task t:g_tm.getTasks()){
			if(t.isHC())
				u+= t.getHiUtil();
			else{
				u+=g_tm.getInfo().getX()*t.getLoUtil();
			}
		}
		return u;
	}
	public double getRU() {
		return g_tm.getRUtil();
	}
	public double getVU() {
		return g_tm.getVUtil();
	}
	public double getMaxRes() {
		return maxRes;
	}
	
}

