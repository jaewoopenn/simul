package task;

import java.util.Arrays;
import java.util.Vector;

import util.SLog;

public class TaskSet {
	private Task[] g_tasks;

	public TaskSet(TaskVec ts) {
		Vector<Task> task_V=ts.getVec();
		int size=task_V.size();
		g_tasks=new Task[size];
		task_V.toArray(g_tasks);
	}
	

	public Task[] getArr() {
		return g_tasks;
	}


	public Task getTask(int i) {
		return g_tasks[i];
	}
	


	public double getUtil() {
		double util=0;
		for(Task t:g_tasks)	{
			util+=t.getUtil();
		}
		return util;
	}


	public int size() {
		return g_tasks.length;
	}














	public boolean check() {
		return true;
	}

	// prn 


	public void prn() {
		for(Task t:g_tasks)	{
			t.prn();
		}
		
	}
	
	public void prnInfo() {
		String st="num:"+g_tasks.length;
		st+=" util:"+getUtil();
		SLog.prn(1, st);
	}
	
	
	public void sort() {
		Arrays.sort(g_tasks,new ComparatorTask());
		
	}

}
