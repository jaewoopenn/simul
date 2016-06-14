package Simul;

import java.util.Collections;
import java.util.Vector;

import Util.Log;

public class CompMng {
	private Vector<Comp> g_comp;
	private double g_lt_LU;
	private double g_ht_LU;
	private double g_ht_HU;
	public CompMng() {
		g_comp=new Vector<Comp>();
	}
	public CompMng(CompMng core) {
		g_comp=core.cloneCore();
	}

	@SuppressWarnings("unchecked")
	private Vector<Comp> cloneCore() {
		return (Vector<Comp>)g_comp.clone();
	}
	
	public void load(TaskMng tm) {
		Vector<TaskMng> tms=new Vector<TaskMng>();
		int max_com=3;
		for(int i=0;i<max_com;i++){
			tms.addElement(new TaskMng());
		}
		
		for(int i=0;i<tm.size();i++){
			Task tsk=tm.getTask(i);
			TaskMng com=tms.elementAt(tsk.cid);
			com.addTask(tsk);
//			Log.prn(2, i+","+tsk.cid);
		}
		for(int i=0;i<max_com;i++){
			TaskMng com=tms.elementAt(i);
			com.freezeTasks();
			addComp(com);
		}
//		prn();
	}
	
	
	public void addComp(TaskMng tm) {
		Comp c=new Comp(tm.get_ID(),tm.getLoUtil(),
				tm.getHiUtil_l(),tm.getHiUtil_h());
		
		g_comp.addElement(c);
	}
	public Comp getComp(int i) {
		return g_comp.elementAt(i);
	}
	public void computeUtils(){
		double u=0;
		for(Comp c:g_comp){
			u+=c.get_lt_lu();
		}
		g_lt_LU=u;

		u=0;
		for(Comp c:g_comp){
			u+=c.get_ht_lu();
		}
		g_ht_LU=u;

		u=0;
		for(Comp c:g_comp){
			u+=c.get_ht_lu();
		}
		g_ht_HU=u;
	}
	public double get_lt_LU(){
		return g_lt_LU;
	}
	public double get_ht_LU(){
		return g_ht_LU;
	}
	public double get_ht_HU(){
		return g_ht_LU;
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
	public void prn2(){
		
		for(TaskMng tm:g_comp){
			Log.prnc(2, "comp "+tm.get_ID());
			Log.prnc(2, " lo "+(tm.getLoUtil()+tm.getHiUtil_l()));
			Log.prn(2, " hi "+tm.getHiUtil_h());
		}
	}
	public void sort() {
		Collections.sort(g_comp,new CompComparator());
		
	}
	public double get_max_util() {
		return Math.max(g_lt_LU+g_ht_LU, g_ht_HU);
	}



}
