package basic;

import util.SLog;

public class TaskMng {
	private TaskSet g_tasks;

	public TaskMng(TaskSet ts) {
		if(ts.isEnd())
			g_tasks=ts;
		else
			SLog.err("cannot create taskmng from taskset which is not ended");
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
	
	public void prnInfo() {
		String st="num:"+g_tasks.size();
		st+=" util:"+getUtil();
		SLog.prn(1, st);
	}
	
	

}
