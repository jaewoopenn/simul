package Simul;

import java.util.Vector;

import Util.Log;

public class TaskMng {
	private boolean g_bAdd;
	private Vector<Task> g_taskV;
	private Task[] g_tasks;
	private int g_size;
	private double g_util;
	private double g_lo_util;
	private double g_hi_util_l;
	private double g_hi_util_h;
	private double x;
	public TaskMng() {
		g_taskV=new Vector<Task>();
		g_size=0;
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
	
	public void addHiTask(int p, int c_l, int c_h) {
		if(!g_bAdd) {
			System.out.println("Err:task set is finalized");
			return;
		}
		g_taskV.add(new Task(g_size,p,c_l,c_h));
		g_size++;
	}
	
	public void freezeTasks()
	{
		g_util=0;
		g_lo_util=0;
		g_hi_util_l=0;
		g_hi_util_h=0;
		g_bAdd=false;
		g_tasks=new Task[g_size];
		g_taskV.toArray(g_tasks);
		for(int i=0;i<g_size;i++)
		{
			Task t=g_tasks[i];
			double tu=(double)(t.c_l)/t.period;
			g_util+=tu;
			if(t.is_HI){
				g_hi_util_l+=tu;
				g_hi_util_h+=(double)(t.c_h)/t.period;;
			} else {
				g_lo_util+=tu;
				
			}
			
		}
	}
	public boolean isFinal(){
		return !g_bAdd;
	}
	public int size() {
		return g_size;
	}

	// get set
	public void setX(int x){
		this.x=x;
	}
	public void setVD(int i, double vd){
		g_tasks[i].vd=vd;
	}

	public int getPt(int i) {
		return g_tasks[i].period;
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
			Log.prn(1, "tid:"+t.tid+" period:"+t.period+" exec:"+t.c_l+" vd:"+t.vd);
		}
		Log.prn(1, "util:"+g_util);
		
	}

	public void setTasks(Vector<Task> all) {
		g_taskV=all;
		g_size=all.size();
	}


	public double getUtil(){
		return g_util;
	}
	public double getMCUtil(){
		return Math.max(g_lo_util+g_hi_util_l, g_hi_util_h);
	}

	public double getLoUtil() {
		return g_lo_util;
	}

	public double getHiUtil_l() {
		return g_hi_util_l;
	}
	public double getHiUtil_h() {
		return g_hi_util_h;
	}

}
