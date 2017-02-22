package basic;



import part.CoreMng;
import util.Log;

public class TaskMng {
	private TaskSet g_tasks;
	private TaskSet g_hi_tasks;
	private TaskSet g_lo_tasks;
	private TaskSetInfo g_info;
	private CoreMng g_cm;

	public TaskMng(TaskSet tasks,TaskSet hi_tasks,TaskSet lo_tasks,TaskSetInfo info) {
		this.g_tasks=tasks;
		this.g_hi_tasks = hi_tasks;
		this.g_lo_tasks = lo_tasks;
		this.g_info = info;
		g_lo_tasks.sortLo();
	}
	
	public Task findDropTask() {
		for(Task t:g_lo_tasks.getArr()){
			if (!t.is_dropped)
				return t;
		}
		return null;
	}


	
	// set


	public void set_cm(CoreMng g_cm) {
		this.g_cm = g_cm;
	}
	
	public void setX(double x){
		g_info.setX(x);
		g_hi_tasks.setX(x);
	}


	
	

	// get
	public CoreMng get_cm() {
		return g_cm;
	}
	
	public TaskSetInfo getInfo() {
		return g_info;
	}

	public TaskSet getTaskSet(){
		return g_tasks;
	}
	public Task[] getTasks() {
		return g_tasks.getArr();
	}
	public Task[] getHiTasks(){
		return g_hi_tasks.getArr();
	}
	public Task[] getLoTasks(){
		return g_lo_tasks.getArr();
	}


	public Task getTask(int i) {

		return g_tasks.get(i);
	}
	


	public double getRUtil() {
		double util=0;
		for(Task t:g_tasks.getArr())	{
			util+=g_info.computeRU(t);
		}
		return util;
	}

	public double getWCUtil() {
		double util=0;
		for(Task t:g_hi_tasks.getArr())	{
			util+=t.getHiUtil();
		}
		for(Task t:g_lo_tasks.getArr())	{
			util+=getDroppedUtil(t);
		}
		return util;
	}

	public double getReclaimUtil(Task t){
		return (1-g_info.getX())*t.getLoUtil();
	}

	public double getDroppedUtil(Task t){
		Log.prn(2, g_info.getX()+" "+t.getLoUtil());
		return g_info.getX()*t.getLoUtil();
	}


	public int size() {
		return g_tasks.size();
	}








	// prn 

	public void prnShort() {
		g_info.prnUtil();
	}

	public void prn() {
		g_tasks.prn();
		g_info.prn();
	}
	
	public void prnHI() {
		g_hi_tasks.prn();
		Log.prn(2, "hi_mode_util:"+g_info.getHi_util_hm());
		
	}

	public void prnLoTasks() {
		g_lo_tasks.prn();
	}
	public void prnStat() {
		g_tasks.prnStat();
	}
	public void prnInfo() {
		Log.prn(2, "prob:"+g_info.getProb_ms());
	}





	public static TaskMng getFile(String fn) {
		return TaskSetFix.loadFile(fn).getTM();
	}





















	
}
