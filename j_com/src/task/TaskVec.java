package task;


import java.util.Vector;


/*
 * a set of tasks 
 * Vector --> Array
 * To make TaskMng, utilize TaskSetEx

*/


public class TaskVec {
	private Vector<Task> g_taskV;
	
	public TaskVec(){
		g_taskV=new Vector<Task>();
	}
	public TaskVec(Vector<Task> all_tasks) {
		g_taskV=new Vector<Task>();
		for(Task t:all_tasks) {
			add(t);
		}
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
	
	public Vector<Task> getVec(){
		return g_taskV;
	}


	public void prn() {
		for(Task t:g_taskV){
			t.prn();
		}
		
	}
	public void addTask(int i, int j) {
		g_taskV.add(new Task(i,j));
		
	}

}
