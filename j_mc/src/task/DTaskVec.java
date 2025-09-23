package task;

import java.util.Vector;

import util.MLoop;


/*
 * Dynamic TaskVec

*/


public class DTaskVec {
	private TaskVec[] g_taskV;
	private TaskVec g_addV;
	private Integer[] g_stageTime;
	private Integer[] g_stageClass; // 0: add , 1: remove
	private int g_num;
	private int g_stage=0;
	public DTaskVec(int num){
		g_num=num;
		g_taskV=new TaskVec[num];
		g_stageTime=new Integer[num];
		g_stageClass=new Integer[num];
		for(int i:MLoop.on(num)) {
			g_taskV[i]=new TaskVec();
		}
		g_addV=new TaskVec();
	}
	
	// Make 
	public void add(Task t) {
		g_addV.add(t);
	}
	public void addTasks(int stage){
		for(Task t:g_addV.getVec()) {
			g_taskV[stage].add(t);
		}
		g_stageClass[stage]=0;
		g_addV=new TaskVec();
	}
	
	public void remove(int stage,int i){
		g_taskV[stage].mark_removed(i);
		g_stageClass[stage]=1;
	}
	


	public void addTime(int stage, int value) {
		g_stageTime[stage]=value;
	}

	public void nextStage() {
		g_stage++;
	}
	
	
	public int getNextTime() {
		if(g_stage==g_num-1) 
			return -1;
		return g_stageTime[g_stage+1];
	}

	

	public int getCurSt() {
		return g_stage;
	}

	// get
	
	public int getTime(int stage) {
		return g_stageTime[stage];
	}
	public int getClass(int stage) {
		return g_stageClass[stage];
	}

	public int getStageNum() {
		return g_num;
	}
	
	public Vector<Task> getVec(int stage){
		return g_taskV[stage].getVec();
	}
	public void setVec(int stage,TaskVec tv){
		g_taskV[stage]=tv;
	}

}
