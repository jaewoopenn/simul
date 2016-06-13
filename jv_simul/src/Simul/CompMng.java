package Simul;

import java.util.Collections;
import java.util.Vector;

import Util.Log;

public class CompMng {
	private Vector<TaskMng> g_comp;
	public CompMng() {
		g_comp=new Vector<TaskMng>();
	}
	public CompMng(CompMng core) {
		// TODO Auto-generated constructor stub
		g_comp=core.cloneCore();
	}

	private Vector<TaskMng> cloneCore() {
		return (Vector<TaskMng>)g_comp.clone();
	}
	
	public void load(TaskMng tm) {
		for(int i=0;i<3;i++){
			addComp(new TaskMng());
		}
		
		for(int i=0;i<tm.size();i++){
			Task tsk=tm.getTask(i);
			TaskMng com=getComp(tsk.cid);
			com.addTask(tsk);
//			Log.prn(2, i+","+tsk.cid);
		}
		for(int i=0;i<3;i++){
			TaskMng com=getComp(i);
			com.freezeTasks();
		}
//		prn();
	}
	
	
	public void addComp(TaskMng tm) {
		g_comp.addElement(tm);
	}
	public TaskMng getComp(int i) {
		return g_comp.elementAt(i);
	}
	
	public double get_lt_LU(){
		double u=0;
		for(TaskMng tm:g_comp){
			u+=tm.getLoUtil();
		}
		return u;
	}
	public double get_ht_LU(){
		double u=0;
		for(TaskMng tm:g_comp){
			u+=tm.getHiUtil_l();
		}
		return u;
	}
	public double get_ht_HU(){
		double u=0;
		for(TaskMng tm:g_comp){
			u+=tm.getHiUtil_h();
		}
		return u;
	}


	public int getSize() {
		return g_comp.size();
	}



	public void prn(){
		int no=1;
		for(TaskMng tm:g_comp){
			Log.prn(2, "comp "+no);
			tm.prn();
			no++;
		}
	}
	public void sort() {
		Collections.sort(g_comp,new CompComparator());
		
	}



}
