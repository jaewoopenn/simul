package basic;

import java.util.Arrays;

import utilSim.FUtil;
import utilSim.Log;
import utilSim.MUtil;

public class TaskMng {
	private Task[] g_tasks;
	private Task[] g_hi_tasks;
	private Task[] g_lo_tasks;
	private TaskSetInfo g_info;

	public TaskMng(Task[] g_tasks,Task[] g_hi_tasks,Task[] g_lo_tasks,TaskSetInfo g_info) {
		this.g_tasks = g_tasks;
		this.g_hi_tasks = g_hi_tasks;
		this.g_lo_tasks = g_lo_tasks;
		this.g_info = g_info;
		Arrays.sort(g_lo_tasks,new ComparatorTask());
	}
	
	

	public void setX(double x){
		g_info.setX(x);
		for(Task t:g_tasks)	{
			if (t.is_HI)
				t.setVD(t.period*x);
		}
		
	}
	
	public void setVD(int i, double vd){
		g_tasks[i].vd=vd;
	}

	public void prn() {
		for(Task t:g_tasks)
			t.prn();
		prnUtil();
	}
	public void prnUtil() {
		Log.prnc(2, "lo_mode_util:"+MUtil.getStr(g_info.getLo_util()+g_info.getHi_util_lm()));
		Log.prnc(2, " ll_util:"+MUtil.getStr(g_info.getLo_util()));
		Log.prn(2, " hl_util:"+MUtil.getStr(g_info.getHi_util_lm()));
		Log.prn(2, "hi_mode_util:"+MUtil.getStr(g_info.getHi_util_hm()));
		
	}
	
	public void prnHI() {
		for(Task t:g_hi_tasks)
			t.prn();
		Log.prn(2, "hi_mode_util:"+g_info.getHi_util_hm());
		
	}
	
	public void sort(){
//		Arrays.sort(g_hi_tasks,new TaskComparator());
		Arrays.sort(g_tasks,new ComparatorTask());
//		Task t=g_hi_tasks[0];
//		t.prn();
	}





	public TaskSetInfo getInfo() {
		return g_info;
	}



	public void modeswitch(int tid) {
		g_tasks[tid].ms();
	}



	public void drop(int tid) {
		g_tasks[tid].drop();
		
	}




	public int findDropTask() {
		for(Task t:g_lo_tasks){
			if (!t.is_dropped)
				return t.tid;
		}
		return -1;
	}



	public void prnLoTasks() {
		for(Task t:g_lo_tasks){
			t.prn();
		}
	}



	public Task[] getTasks() {
		return g_tasks;
	}


	public Task getTask(int i) {

		return g_tasks[i];
	}
	
	public Task[] getHiTasks(){
		return g_hi_tasks;
	}

	public double getRU() {
		double util=0;
		for(Task t:g_tasks)	{
			util+=getRU(t);
		}
		return util;
	}

	public double getUtil() {
		double util=0;
		for(Task t:g_tasks)	{
			util+=getRU(t);
		}
		return util;
	}


	private double getRU(Task t) {
		if(t.is_HI){
			if(t.is_HM)
				return t.getHiUtil();
			else
				return t.getLoRUtil();
		} 
		if(t.is_dropped)
			return g_info.getX()*t.getLoUtil();
		else
			return t.getLoUtil();
	}
	public double getReclaimUtil(int tid){
		Task t=g_tasks[tid];
		return (1-g_info.getX())*t.getLoUtil();
	}
	public void writeFile(String fn){
		FUtil fu=new FUtil(fn);
		for(Task t:g_tasks)
			TaskFile.writeTask(fu,t);
		
		fu.save();
	}



	public int size() {
		return g_tasks.length;
	}




	

//	public int getPt(int i) {
//	return g_tasks[i].period;
//}
//
//public int[] getPeriods() {
//	int[] plst=new int[g_info.getSize()];
//	for(int i=0;i<g_info.getSize();i++)
//	{
//		plst[i]=getPt(i);
//	}
//	
//	return plst;
//}


}
