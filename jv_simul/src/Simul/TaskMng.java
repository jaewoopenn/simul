package Simul;

import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import Util.Log;

public class TaskMng {
	private boolean g_bAdd;
	private Vector<Task> g_taskV;
	private Vector<Task> g_hi_taskV;
	private Task[] g_tasks;
	private Task[] g_hi_tasks;
	private int g_size=0;
	private int g_hi_size=0;
	private int g_lo_size=0;
	private double g_util;
	private double g_lo_util;
	private double g_hi_util_l;
	private double g_hi_util_h;
	private double g_x=1;
	public TaskMng() {
		g_taskV=new Vector<Task>();
		g_hi_taskV=new Vector<Task>();
		g_bAdd=true;
	}
	
	
	public void addTask(int p, int e) {
		g_taskV.add(new Task(g_size,p,e));
	}
	
	public void addHiTask(int p, int c_l, int c_h) {
		Task t=new Task(g_size,p,c_l,c_h);
		g_taskV.add(t);
		g_hi_taskV.add(t);
	}
	public void setTasks(Vector<Task> all) {
		g_taskV=all;
		g_hi_taskV=new Vector<Task>();
		for(Task t:all){
			if(t.is_HI)
				g_hi_taskV.add(t);
		}
	}
	
	public void freezeTasks()
	{
		if(!g_bAdd){
			Log.prnc(1, "already freezed");
		}
		g_util=0;
		g_lo_util=0;
		g_hi_util_l=0;
		g_hi_util_h=0;
		g_lo_size=0;		
		g_bAdd=false;
		g_size=g_taskV.size();
		g_tasks=new Task[g_size];
		g_taskV.toArray(g_tasks);
		g_hi_size=g_hi_taskV.size();
		g_hi_tasks=new Task[g_hi_size];
		g_hi_taskV.toArray(g_hi_tasks);
		for(Task t:g_tasks)
		{
			if (t.is_HI)
				t.setVD(t.period*g_x);
			double tu=(double)(t.c_h)/t.period;
			g_util+=tu;
			if(t.is_HI){
				g_hi_util_l+=(double)(t.c_l)/t.period;
				g_hi_util_h+=tu;
			} else {
				g_lo_util+=tu;
				g_lo_size++;
			}
			
		}
	}
	public boolean isFinal(){
		return !g_bAdd;
	}
	public int size() {
		return g_size;
	}
	public int hi_size() {
		return g_hi_size;
	}
	public int lo_size() {
		return g_lo_size;
	}

	// get set
	public void setX(double x){
		this.g_x=x;
//		Log.prn(1, "x:"+x);
	}
	public void setVD(int i, double vd){
		if(g_bAdd) {
			Log.prn(1, "tasks are not finalized");
			return;
		}
		g_tasks[i].vd=vd;
	}

	public int getPt(int i) {
		if(g_bAdd) {
			Log.prn(1, "tasks are not finalized");
			return -1;
		}
		return g_tasks[i].period;
	}
	
	public int[] getPeriods() {
		if(g_bAdd) {
			Log.prn(1, "tasks are not finalized");
			return null;
		}
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
	public Task getHiTask(int i) {
		return g_hi_tasks[i];
	}

	public void prn() {
		if(g_bAdd) {
			Log.prn(2, "tasks are not finalized");
			return;
		}
		for(int i=0;i<g_size;i++)
		{
			Task t=g_tasks[i];
			Log.prn(2, "tid:"+t.tid+" "+t.is_HI+"(t,c_l,c_h):"+t.period+","+t.c_l+","+t.c_h);
		}
		Log.prn(2, "util:"+g_util);
		Log.prn(2, "lo_mode_util:"+(g_lo_util+g_hi_util_l));
	}
	public void prnHI() {
		if(g_bAdd) {
			Log.prn(1, "tasks are not finalized");
			return;
		}
		for(int i=0;i<g_hi_size;i++)
		{
			Task t=g_hi_tasks[i];
			Log.prn(1, "tid:"+t.tid+" period:"+t.period+" exec:"+t.c_l+" hi_e:"+t.c_h);
		}
		Log.prn(1, "util:"+g_util);
		
	}
	
	public void sort(){
		Arrays.sort(g_hi_tasks,new TaskComparator());
		Arrays.sort(g_tasks,new TaskComparatorLo());
//		Task t=g_hi_tasks[0];
//		t.prn();
	}


	public double getUtil(){
		if(g_bAdd) {
			Log.prn(1, "tasks are not finalized");
			return -1;
		}
		return g_util;
	}
	public double getMCUtil(){
		if(g_bAdd) {
			Log.prn(1, "tasks are not finalized");
			return -1;
		}
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
