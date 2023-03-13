package task;

import java.util.Arrays;
import java.util.Vector;

import util.SLog;

/*
 * a set of tasks 
 * Vector --> Array
 * To make TaskMng, utilize TaskSetEx

*/


public class TaskSet {
	private Task[] g_tasks;
	
	public TaskSet(Vector<Task> tv){
		int size=tv.size();
		g_tasks=new Task[size];
		tv.toArray(g_tasks);
	}	
	public TaskSet(TaskVec v){
		this(v.getVec());
	}

	public TaskSet(Task[] a) {
		g_tasks=a;
	}
	
	public Task get(int i){
		return g_tasks[i];
	}
	public int size(){
		return g_tasks.length;
	}
	public Task[] getArr(){
		if(g_tasks==null) {
			SLog.err("g_task is null");
		}
		return g_tasks;
	}
	public void sortLo() {
		Arrays.sort(g_tasks,new ComparatorTask());
		
	}
	public void sortLo2() {
		Arrays.sort(g_tasks,new ComparatorTask2());
		
	}

	public void sortHi() {
		Arrays.sort(g_tasks,new ComparatorHighTask());
		
	}

	public void setX(double x) {
		for(Task t:g_tasks){
			t.setX(x);
			if(t.getHiUtil()<=t.getLoVdUtil())
				t.setHI_only();
		}
	}
	public void setX2(double x) {
		for(Task t:g_tasks){
			t.setX(x);
		}
	}
	public void prn() {
		for(Task t:g_tasks){
			t.prnTxt();
		}
		
	}
	public void prnRuntime() {
		for(Task t:g_tasks){
			t.prnRuntime();
		}
		
	}
	
	public void prnStat() {
		for(Task t:g_tasks){
			t.prnStat();
		}
		
	}

	public void prnOffline() {
		for(Task t:g_tasks){
			t.prnOffline();
		}
		
	}

	public void prnPara() {
		for(Task t:g_tasks){
			t.prnPara();
		}
		
	}

	public void prnTxt() {
		for(Task t:g_tasks){
			t.prnTxt();
		}
		
	}
	public  TaskMng getTM() {
		TaskSet ts_hi_tasks;
		TaskSet ts_lo_tasks;

		TaskVec hi_tasks=new TaskVec();
		TaskVec lo_tasks=new TaskVec();
		for(Task t:g_tasks){
			if(t.isHC())
				hi_tasks.add(t);
			else
				lo_tasks.add(t);
		}
		ts_hi_tasks=new TaskSet(hi_tasks);
		ts_lo_tasks=new TaskSet(lo_tasks);
				
		double loutil=0;
		double loutil_de=0;
		double hiutil_lm=0;
		double hiutil_hm=0;
		for(Task t:g_tasks)
		{
			if(t.isHC()){
				hiutil_lm+=t.getLoUtil();
				hiutil_hm+=t.getHiUtil();
			} else {
				loutil+=t.getLoUtil();
				loutil_de+=t.getHiUtil();
			}
		}
		SysInfo info=new SysInfo();
		info.setLo_util(loutil);
		info.setLo_de_util(loutil_de);
		info.setUtil_HC_HI(hiutil_hm);
		info.setUtil_HC_LO(hiutil_lm);
		return new TaskMng(g_tasks,ts_hi_tasks,ts_lo_tasks,info);
	}






}
