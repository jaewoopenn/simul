package task;

import java.util.Vector;


/*
 * Dynamic TaskVec

*/


public class DTaskVec {
	private TaskVec[] g_taskV;
	private Integer[] g_stageTime;
	private int g_num;
	private int g_stage=0;
	public DTaskVec(int num){
		g_num=num;
		g_taskV=new TaskVec[num];
		g_stageTime=new Integer[num];
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
	public TaskMng getTM(int stage){
		TaskSet ts=new TaskSet(g_taskV[stage].getVec());
		return ts.getTM();
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

	public void addTime(int stage, int value) {
		g_stageTime[stage]=value;
	}

	public int getTime(int stage) {
		return g_stageTime[stage];
	}
	public int getNext() {
		// TODO Auto-generated method stub
		return g_stageTime[g_stage];
	}
	public void nextStage() {
		g_stage++;
	}
	public int getStage() {
		return g_stage;
	}


}
