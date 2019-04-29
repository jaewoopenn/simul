package basic;



public class TaskMng {
	private TaskSet g_tasks;

	public TaskMng(TaskSet tasks) {
		this.g_tasks=tasks;
	}
	
	
	public TaskSet getTaskSet(){
		return g_tasks;
	}
	public Task[] getTasks() {
		return g_tasks.getArr();
	}


	public Task getTask(int i) {

		return g_tasks.get(i);
	}
	


	public double getUtil() {
		double util=0;
		for(Task t:g_tasks.getArr())	{
			util+=t.getUtil();
		}
		return util;
	}



	public int size() {
		return g_tasks.size();
	}














	public boolean check() {
		return true;
	}

	// prn 


	public void prn() {
		g_tasks.prn();
	}
	
	

}
