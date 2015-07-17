package Simul;

import java.util.Vector;

import Util.Log;

public class TaskMng {
	private boolean g_bAdd;
	private Vector<Task> g_taskV;
	private Task[] g_tasks;
	private int g_size;
	private double g_util;
	public TaskMng() {
		g_taskV=new Vector<Task>();
		g_size=0;
		g_util=0;
		g_bAdd=true;
	}

	public void addTask(int p, int e) {
		if(!g_bAdd) {
			System.out.println("Err:task set is finalized");
			return;
		}
		g_taskV.add(new Task(g_size,p,e));
		g_size++;
	}
	public void finalize()
	{
		g_bAdd=false;
		g_tasks=new Task[g_size];
		g_taskV.toArray(g_tasks);
		for(int i=0;i<g_size;i++)
		{
			Task t=g_tasks[i];
			g_util+=(double)(t.exec)/t.period;
		}
	}
	public int size() {
		return g_size;
	}

	public int getPt(int i) {
		return g_tasks[i].period;
	}
	public double getUtil(){
		return g_util;
	}
	public int[] getPeriods() {
		int[] plst=new int[g_size];
		for(int i=0;i<g_size;i++)
		{
			plst[i]=getPt(i);
		}
		
		return plst;
	}

	public Task getTask(int i) {
		return g_tasks[i];
	}

	public void prn() {
		
		for(int i=0;i<g_size;i++)
		{
			Task t=g_tasks[i];
			Log.prn(1, "tid:"+t.tid+" period:"+t.period+" exec:"+t.exec);
		}
		Log.prn(1, "util:"+g_util);
		
	}

	public void setTasks(Vector<Task> all) {
		g_taskV=all;
		g_size=all.size();
		finalize();
	}


}
