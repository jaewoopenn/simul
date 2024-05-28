package task;
/*
 * hi task set & lo task set 

*/


public class TaskMng {
	private TaskSet g_tasks;
	private TaskSet g_hi_tasks;
	private TaskSet g_lo_tasks;

	public TaskMng(TaskSet tasks,TaskSet hi_tasks,TaskSet lo_tasks) {
		this.g_tasks=tasks;
		this.g_hi_tasks = hi_tasks;
		this.g_lo_tasks = lo_tasks;
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
	

	public int size() {
		return g_tasks.size();
	}


	public double getMaxUtil() {
		double lo_util=0;
		double hi_util=0;
		for(Task t:g_lo_tasks.getArr())	{
			lo_util+=t.getLoUtil();
			hi_util+=t.getLoUtil();
		}
		for(Task t:g_hi_tasks.getArr())	{
			hi_util+=t.getHiUtil();
		}
		return Math.max(lo_util, hi_util);
	}

	public double getLC_LoUtil() {
		double lo_util=0;
		for(Task t:g_lo_tasks.getArr())	{
			lo_util+=t.getLoUtil();
		}
		return lo_util;
		
	}

	public double getHC_LoUtil() {
		double lo_util=0;
		for(Task t:g_hi_tasks.getArr())	{
			lo_util+=t.getLoUtil();
		}
		return lo_util;
	}

	public double getHC_HiUtil() {
		double util=0;
		for(Task t:g_hi_tasks.getArr())	{
			util+=t.getHiUtil();
		}
		return util;		
	}








	public void prn() {
		g_tasks.prn();
	}
	public void prnPara() {
		g_tasks.prnPara();
		
	}


}
