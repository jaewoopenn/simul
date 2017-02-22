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
	
	public void remove(Task t) {
		g_taskV.remove(t);
	}
	public void removeLast(){
		g_taskV.remove(g_taskV.size()-1);
	}
	public void removeCore(int core) {
		Vector<Task> v=new Vector<Task>();
		for(Task t:g_taskV){
			if(t.getCPU()!=core)
				v.add(t);
		}
		g_taskV=v;
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
	

	public void prnStat() {
		for(Task t:g_tasks){
			t.prnStat();
		}
	}

	public void setCPU(int core) {
		for(Task t:g_tasks){
			t.setCPU(core);
		}
	}

	public void setX(double x) {
		for(Task t:g_tasks){
			t.setVD(t.period*x);
		}
		
	}




//	public void prnComp() {
//		for(Task t:g_tasks){
//			t.prnComp();
//		}
//	}

}
