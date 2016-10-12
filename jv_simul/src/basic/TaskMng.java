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
	
	


	
	public void sort(){
//		Arrays.sort(g_hi_tasks,new TaskComparator());
//		Arrays.sort(g_tasks,new ComparatorTask());
//		Task t=g_hi_tasks[0];
//		t.prn();
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
	public int findDropTask_shared() {
		for(Task t:g_lo_tasks){
			if (!t.is_dropped&&!t.is_isol())
				return t.tid;
		}
		return -1;
	}

	
	
	// set
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
	
	// prn 
	
	public void prn() {
		for(Task t:g_tasks)
			t.prn();
		prnUtil();
	}
	public void prnComp() {
		for(Task t:g_tasks)
			t.prnComp();
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

	public void prnLoTasks() {
		for(Task t:g_lo_tasks){
			t.prn();
		}
	}
	public void prnStat() {
		for(Task t:g_tasks)
			t.prnStat();
	}

	// get
	public int getComp(int tid){
		return g_tasks[tid].getComp();
	}
	
	public TaskSetInfo getInfo() {
		return g_info;
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
	public Task[] getLoTasks(){
		return g_lo_tasks;
	}

	public double getRU() {
		double util=0;
		for(Task t:g_tasks)	{
			util+=g_info.computeRU(t);
//			Log.prn(1, "ru:"+util);
		}
		return util;
	}

	public double getRUtil() {
		double util=0;
		for(Task t:g_tasks)	{
			util+=g_info.computeRU(t);
		}
		return util;
	}

	public double getMCUtil() {
		return g_info.getMCUtil();
	}

	public double getReclaimUtil(int tid){
		Task t=g_tasks[tid];
		return (1-g_info.getX())*t.getLoUtil();
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
