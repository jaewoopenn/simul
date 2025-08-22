package task;

import java.util.Vector;


/*
 * Dynamic TaskVec

*/


public class DTaskVec {
	private TaskVec[] g_taskV;
	private Integer[] g_stageTime;
	private Integer[] g_stageClass; // 0: add , 1: remove
	private int g_num;
	private int g_stage=0;
	public DTaskVec(int num){
		g_num=num;
		g_taskV=new TaskVec[num];
		g_stageTime=new Integer[num];
		g_stageClass=new Integer[num];
		for(int i=0;i<num;i++) {
			g_taskV[i]=new TaskVec();
		}
	}
	// Make 
	public void add(int stage, Task t){
		g_taskV[stage].add(t);
		g_stageClass[stage]=0;
	}
	
	public void remove(int stage,int i){
		g_taskV[stage].remove(i);
		g_stageClass[stage]=1;
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

	public void nextStage() {
		g_stage++;
	}
	
	// get
	
	public int getTime(int stage) {
		return g_stageTime[stage];
	}
	public int getClass(int stage) {
		return g_stageClass[stage];
	}
	
	public int getNext() {
		if(g_stage==g_num-1) 
			return -1;
		return g_stageTime[g_stage+1];
	}
	public int getStage() {
		return g_stage;
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
	public void prn() {
		TaskMng tm=getTM(0);
		tm.prn();
	}
	

}
