package task;

import java.util.Arrays;
import java.util.Vector;

import util.MCal;
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
	public  double computeRBF(int i, double t) {
		double r=0;
		for(int j=0;j<=i;j++) {
			if(j==i) {
				r+=g_tasks[j].exec;
			} else {
				r+=Math.ceil((t+MCal.err)/g_tasks[j].period)*g_tasks[j].exec;
			}
		}
		
		return r;
	}
	public  double computeDBF(double t) {
		double d=0;
		for(Task tsk:g_tasks)	{
			d+=Math.floor((t+MCal.err)/tsk.period)*tsk.exec;
		}
		
		return d;
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
	
	public void prnInfo(int l) {
		String st="num:"+g_tasks.length;
		st+=" util:"+getUtil();
		SLog.prn(l, st);
	}
	
	
	public void sort() {
		Arrays.sort(g_tasks,new ComparatorTask());
		
	}


	public int[] getPeriods() {
		int[] arr=new int[g_tasks.length];
		int c=0;
		for(Task t:g_tasks)	{
			arr[c]=t.period;
			c++;
		}
		return arr;

	}

}
