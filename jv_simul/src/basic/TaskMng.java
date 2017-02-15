package basic;



import util.Log;

public class TaskMng {
	private TaskSet g_tasks;
	private TaskSet g_hi_tasks;
	private TaskSet g_lo_tasks;
	private TaskSetInfo g_info;

	public TaskMng(Task[] tasks,Task[] hi_tasks,Task[] lo_tasks,TaskSetInfo info) {
		this.g_tasks = new TaskSet(tasks);
		this.g_hi_tasks = new TaskSet(hi_tasks);
		this.g_lo_tasks = new TaskSet(lo_tasks);
		this.g_info = info;
		g_lo_tasks.sortLo();
	}
	
	


	
	public void sort(){
//		Arrays.sort(g_hi_tasks,new TaskComparator());
//		Arrays.sort(g_tasks,new ComparatorTask());
//		Task t=g_hi_tasks[0];
//		t.prn();
	}












	public Task findDropTask() {
		for(Task t:g_lo_tasks.getArr()){
			if (!t.is_dropped)
				return t;
		}
		return null;
	}

	public Task findDropTask_shared() {
		for(Task t:g_lo_tasks.getArr()){
			if (!t.is_dropped&&!t.is_isol())
				return t;
		}
		return null;
	}

	
	
	// set
	public void setX(double x){
		g_info.setX(x);
		for(Task t:g_tasks.getArr())	{
			if (t.is_HI)
				t.setVD(t.period*x);
		}
		
	}
	
	public void setVD(int i, double vd){
		g_tasks.get(i).vd=vd;
	}
	
	// prn 
	
	public void prn() {
		g_tasks.prn();
		g_info.prn();
	}
	public void prnComp() {
		g_tasks.prnComp();
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

	// get
	public int getComp(int tid){
		return g_tasks.get(tid).getComp();
	}
	
	public TaskSetInfo getInfo() {
		return g_info;
	}

	public TaskSet getTasks() {
		return g_tasks;
	}


	public Task getTask(int i) {

		return g_tasks.get(i);
	}
	
	public Task[] getHiTasks(){
		return g_hi_tasks.getArr();
	}
	public Task[] getLoTasks(){
		return g_lo_tasks.getArr();
	}

	public double getRU() {
		double util=0;
		for(Task t:g_tasks.getArr())	{
			util+=g_info.computeRU(t);
//			Log.prn(1, "ru:"+util);
		}
		return util;
	}

	public double getRUtil() {
		double util=0;
		for(Task t:g_tasks.getArr())	{
			util+=g_info.computeRU(t);
		}
		return util;
	}

	public double getMCUtil() {
		return g_info.getMCUtil();
	}

	public double getReclaimUtil(Task t){
		return (1-g_info.getX())*t.getLoUtil();
	}



	public int size() {
		return g_tasks.size();
	}





	public TaskSet getTaskSet() {
		return g_tasks;
	}



















	

//	public int getPt(int i) {
//	return g_tasks[i].period;
//}
//
//public int[] getPeriods() {
//	int[] plst=new int[g_info.getSize()];
//	for(int i=0;i<g_info.getSize();i++)
//	{
//		plst[i]=getPt(i);
//	}
//	
//	return plst;
//}


}
