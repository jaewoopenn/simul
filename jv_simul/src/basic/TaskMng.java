package basic;

import java.util.Arrays;

import utilSim.Log;

public class TaskMng {
	private Task[] g_tasks;
	private Task[] g_hi_tasks;
	private TaskSetInfo g_info;
	public TaskMng(Task[] g_tasks,Task[] g_hi_tasks,TaskSetInfo g_info) {
		this.g_tasks = g_tasks;
		this.g_hi_tasks = g_hi_tasks;
		this.g_info = g_info;
	}
	
	

	public void setX(double x){
		for(Task t:g_tasks)
		{
			if (t.is_HI)
				t.setVD(t.period*x);
		}
		
	}
	
	public void setVD(int i, double vd){
		g_tasks[i].vd=vd;
	}

	public int getPt(int i) {
		return g_tasks[i].period;
	}
	
	public int[] getPeriods() {
		int[] plst=new int[g_info.getSize()];
		for(int i=0;i<g_info.getSize();i++)
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
		for(int i=0;i<g_info.getSize();i++)
		{
			Task t=g_tasks[i];
			t.prn();
		}
		Log.prn(2, "util:"+g_info.getUtil());
		Log.prn(2, "lo_mode_util:"+(g_info.getLo_util()+g_info.getHi_util_lm()));
		Log.prn(2, "hi_mode_util:"+g_info.getHi_util_hm());
	}
	public void prnHI() {
		for(int i=0;i<g_info.getHi_size();i++)
		{
			Task t=g_hi_tasks[i];
			Log.prn(1, "tid:"+t.tid+" period:"+t.period+" exec:"+t.c_l+" hi_e:"+t.c_h);
		}
		Log.prn(1, "util:"+g_info.getUtil());
		
	}
	
	public void sort(){
		Arrays.sort(g_hi_tasks,new TaskComparator());
		Arrays.sort(g_tasks,new TaskComparatorLo());
//		Task t=g_hi_tasks[0];
//		t.prn();
	}





	public TaskSetInfo getInfo() {
		return g_info;
	}





}
