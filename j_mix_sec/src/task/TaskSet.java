package task;


import java.util.Arrays;
import java.util.Vector;

import util.SLog;

/*
 * a set of tasks 
 * Vector --> Array
 * To make TaskMng, utilize TaskSetEx

*/


public class TaskSet {
	private Task[] g_tasks;
	
	public TaskSet(TaskVec v){
		Vector<Task> tv=v.getVec();
		int size=tv.size();
		g_tasks=new Task[size];
		tv.toArray(g_tasks);
	}

	public TaskSet(Task[] a) {
		g_tasks=a;
	}
	public void sortRM() {
		Arrays.sort(g_tasks,new ComparatorTask());
		
	}
	public void sortCM() {
		Arrays.sort(g_tasks,new ComparatorTaskCM());
		
	}
	
	
	public Task get(int i){
		return g_tasks[i];
	}
	public int size(){
		return g_tasks.length;
	}
	public Task[] getArr(){
		if(g_tasks==null) {
			SLog.err("g_task is null");
		}
		return g_tasks;
	}

	public void prn() {
		for(Task t:g_tasks){
			t.prn();
		}
		
	}
	public void prnPara() {
		for(Task t:g_tasks){
			t.prnPara();
		}
		
	}


}
