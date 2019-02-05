package basic;

import java.util.Arrays;
import java.util.Vector;

import util.Log;

/*
 * a set of tasks 
 * Vector --> Array
 * To make TaskMng, utilize TaskSetEx

*/


public class TaskSet {
	private Vector<Task> g_taskV;
	private Task[] g_tasks;
	
	public TaskSet(){
		g_taskV=new Vector<Task>();
	}
	public TaskSet( Vector<Task> a) {
		g_taskV=a;
	}

	public TaskSet(Task[] a) {
		g_tasks=a;
	}
	public void add(Task t){
		g_taskV.add(t);
	}
	public int v_size(){
		return g_taskV.size();
	}
	
	public void remove(Task t) {
		g_taskV.remove(t);
	}
	public void removeLast(){
		g_taskV.remove(g_taskV.size()-1);
	}
	
	public void transform_Array(){
		int size=g_taskV.size();
		g_tasks=new Task[size];
		g_taskV.toArray(g_tasks);
	}
	
	public Task get(int i){
		return g_tasks[i];
	}
	public int size(){
		return g_tasks.length;
	}
	public Task[] getArr(){
		if(g_tasks==null)
			Log.prnErr("g_task is null");
		return g_tasks;
	}
	public Vector<Task> getVec(){
		return g_taskV;
	}
	public void sortLo() {
		Arrays.sort(g_tasks,new ComparatorTask());
		
	}

	public void sortHi() {
		Arrays.sort(g_tasks,new ComparatorHighTask());
		
	}

	public void setX(double x) {
		for(Task t:g_tasks){
			t.setVD(t.period*x);
			if(t.getHiUtil()<=t.getLoVdUtil())
				t.setHI_only();
		}
		
	}

	public void prn() {
		for(Task t:g_tasks){
			t.prnShort();
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






}
