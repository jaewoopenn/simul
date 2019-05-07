package basic;

import util.S_Log;

public class TaskMng {
	private TaskSet g_tasks;

	public TaskMng(TaskSet ts) {
		if(ts.isEnd())
			g_tasks=ts;
		else
			S_Log.err("cannot create taskmng from taskset which is not ended");
	}
	

	public Task[] getArr() {
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
