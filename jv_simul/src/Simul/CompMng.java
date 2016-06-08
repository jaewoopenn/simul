package Simul;

import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import Util.Log;

public class CompMng {
	private Vector<TaskMng> g_comp;
	public CompMng() {
		g_comp=new Vector<TaskMng>();
	}
	
	
	public void addComp(TaskMng tm) {
		g_comp.add(tm);
	}
	public TaskMng getComp(int i) {
		// TODO Auto-generated method stub
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





}
