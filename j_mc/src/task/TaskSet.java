package task;

import java.util.Arrays;
import java.util.Vector;

//import util.MCal;
import util.SLog;

/*
 * a set of tasks 
 * Vector --> Array
 * To make TaskMng, utilize TaskSetEx

*/


public class TaskSet {
	private Task[] g_tasks;
	
	public TaskSet(Vector<Task> g_taskV){
		Vector<Task> tv=g_taskV;
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

	public void sortHi() {
		Arrays.sort(g_tasks,new ComparatorHighTask());
		
	}

	public void setVD(double x) {
		for(Task t:g_tasks){
			t.setVD(x);
		}
	}
	public void set_HI_only(double x) {
		for(Task t:g_tasks){
//			String s1=MCal.getStr(t.getHiUtil());
//			String s2=MCal.getStr(t.getLoVdUtil());
//			SLog.prn(t.tid+","+t.vd+","+s1+", "+s2);
			if(t.getHiUtil()<=t.getLoVdUtil())
				t.setHI_only();
			else
				t.setNormal(x);
		}
	}

	public void prn() {
		for(Task t:g_tasks){
//			if(t.removed())
//				continue;
			TaskUtil.prn(t);
		}
	}
	public void prnRuntime() {
		for(Task t:g_tasks){
			TaskUtil.prnRuntime(t);
		}
		
	}
	
	public void prnStat() {
		for(Task t:g_tasks){
			TaskUtil.prnStat(t);
		}
		
	}

	public void prnOffline() {
		for(Task t:g_tasks){
			TaskUtil.prnOffline(t);
		}
		
	}

	public void prnPara() {
		for(Task t:g_tasks){
			TaskUtil.prnPara(t);
		}
		
	}

	public void prnTxt() {
		for(Task t:g_tasks){
			TaskUtil.prnTxt(t);
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
				
		return new TaskMng(g_tasks,ts_hi_tasks,ts_lo_tasks);
	}




}
