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
	public void sortLo() {
		Arrays.sort(g_tasks,new ComparatorTask());
		
	}
	public void sortLo2() {
		Arrays.sort(g_tasks,new ComparatorTask2());
		
	}

	public void sortHi() {
		Arrays.sort(g_tasks,new ComparatorHighTask());
		
	}

	public void setX(double x) {
		for(Task t:g_tasks){
			t.setVD(t.period*x);
			if(t.getHiUtil()<=t.getLoVdUtil())
				t.setHI_only();
		}
		
	}

	public void prn() {
		for(Task t:g_tasks){
			t.prnShort();
		}
		
	}
	public void prnRuntime() {
		for(Task t:g_tasks){
			t.prnRuntime();
		}
		
	}
	
	public void prnStat() {
		for(Task t:g_tasks){
			t.prnStat();
		}
		
	}

	public void prnOffline() {
		for(Task t:g_tasks){
			t.prnOffline();
		}
		
	}

	public void prnPara() {
		for(Task t:g_tasks){
			t.prnPara();
		}
		
	}

	public void prnTxt() {
		for(Task t:g_tasks){
			t.prnTxt();
		}
		
	}






}
