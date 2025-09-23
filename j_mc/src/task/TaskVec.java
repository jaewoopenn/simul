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
	
//	public void remove(int i){
//		g_taskV.remove(i);
//	}
	public void mark_removed(int i) {
		Task t=g_taskV.elementAt(i);
		t.markRemoved();
	}
	
	public Vector<Task> getVec(){
		return g_taskV;
	}

	public TaskMng getTM() {
		TaskSet ts=new TaskSet(g_taskV);
		return ts.getTM();
	}


}
