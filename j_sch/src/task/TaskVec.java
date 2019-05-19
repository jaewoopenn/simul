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


}
