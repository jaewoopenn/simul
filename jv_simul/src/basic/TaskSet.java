package basic;

import java.util.Arrays;
import java.util.Vector;



public class TaskSet {
	private Vector<Task> g_taskV;
	private Task[] g_tasks;
	
	public TaskSet(){
		g_taskV=new Vector<Task>();
	}

	public TaskSet(Task[] a) {
		g_tasks=a;
	}
	public void add(Task t){
		g_taskV.add(t);
	}
	public int v_size(){
		return g_taskV.size();
	}
	
	public void removeLast() {
		g_taskV.remove(v_size()-1);
		
	}
	public void transform_Array(){
		int size=g_taskV.size();
		g_tasks=new Task[size];
		g_taskV.toArray(g_tasks);
	}
	
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
