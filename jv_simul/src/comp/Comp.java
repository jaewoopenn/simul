package comp;


import util.Log;
import basic.Task;
import basic.TaskMng;
import basic.TaskSet;


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
		double cu=g_tm.getInfo().getLo_util();
		double tu=cu*(alpha);
//		Log.prn(1,"cu:"+cu+" tu:"+tu+", alpha:"+alpha);
		cu=0;
		Task[] tasks=g_tm.getLoTasks();
		for(int i=tasks.length-1;i>=0;i--){
//		for(int i=0;i<tasks.length;i++){
			Task t=tasks[i];
//			t.prn();
			if(cu>=tu)
				break;
			cu+=t.getLoUtil();
			t.setIsolate(true);
		}
	}
	// prn
	public void prn() {
		Log.prn(1, "cid:"+cid+", alpha:"+alpha);
		Log.prn(1, "maxRes:"+maxRes);
//		g_tm.prnComp();
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
	public TaskSet getTasks() {
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
		for(Task t:g_tm.getTasks().getArr()){
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
	public double getNa_U(){
		double u=0;
		for(Task t:g_tm.getTasks().getArr()){
			if(t.is_HI)
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
		for(Task t:g_tm.getTasks().getArr()){
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
		double u=0;
		for(Task t:g_tm.getTasks().getArr()){
			if(t.is_HI)
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
	public double getMaxRes() {
		return maxRes;
	}
	
}

