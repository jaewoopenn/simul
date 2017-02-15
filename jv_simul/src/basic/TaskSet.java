package basic;

import java.util.Arrays;


public class TaskSet {
	private Task[] g_tasks;

	public TaskSet(Task[] a) {
		g_tasks=a;
	}
//	public void set(Task[] a){
//		g_tasks=a;
//	}
	public Task get(int i){
		return g_tasks[i];
	}
	public int size(){
		return g_tasks.length;
	}
	public Task[] getArr(){
		return g_tasks;
	}
	public void sortLo() {
		Arrays.sort(g_tasks,new ComparatorTask());
		
	}

	public void sortHi() {
		Arrays.sort(g_tasks,new ComparatorHighTask());
		
	}
	public void prn() {
		for(Task t:g_tasks){
			t.prnShort();
		}
		
	}
	public void prnComp() {
		for(Task t:g_tasks){
			t.prnComp();
		}
	}
	public void prnStat() {
		for(Task t:g_tasks){
			t.prnStat();
		}
	}

}
