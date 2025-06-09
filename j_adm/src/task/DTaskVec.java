package task;

import java.util.Vector;


/*
 * Dynamic TaskVec

*/


public class DTaskVec {
	private TaskVec[] g_taskV;
	private int g_num;
	public DTaskVec(int num){
		g_num=num;
		g_taskV=new TaskVec[num];
		for(int i=0;i<num;i++) {
			g_taskV[i]=new TaskVec();
		}
	}

	public void add(int stage, Task t){
		g_taskV[stage].add(t);
	}
	
	public void remove(int stage,int i){
		g_taskV[stage].remove(i);
	}
	
	public Vector<Task> getVec(int stage){
		return g_taskV[stage].getVec();
	}

	public int getNum() {
		return g_num;
	}

	public void copy(int src, int dst) {
		Vector<Task> srcV=g_taskV[src].getVec();
		for(Task t:srcV) {
			g_taskV[dst].add(t);
		}
	}



}
