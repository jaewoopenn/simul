package gen;

import java.util.Vector;

import task.Task;
import task.TaskSet;
import task.TaskVec;
import util.SLog;

public  class TaskGen {
	protected TaskGenParam g_param;
	protected Vector<Task> g_tasks;
	private int g_tid=0;
	public TaskGen(TaskGenParam tgp) {
		g_param=tgp;
	}

	public void genTS() {
		while(true){
			genTS_One();
			if(chkUtil()) break;
		}
	}
	
	private void genTS_One()
	{
		g_tasks=new Vector<Task>();
		g_tid=0;
		Task t;
		while(getUtil()<=g_param.u_ub){
			t=genTask(g_tid);
			if(t==null) 
				continue;
			if (!t.check())
				continue;
			g_tasks.add(t);
			g_tid++;
		}
		g_tasks.remove(g_tasks.size()-1);
	}
	public Task genTaskOne() {
		Task t=null;
		while(t==null) {
			t=genTask(g_tid);
		}
		g_tid++;
		return t;
	}



	public boolean chkUtil() {
		return g_param.chkUtil(getUtil());
	}
	




	public TaskSet getTS() {
		TaskVec ts=new TaskVec();
		for(Task t:g_tasks) {
			ts.add(t);
		}
		return new TaskSet(ts);
	}
	
	
	

	public Task genTask(int tid){
		Task tsk=g_param.genTaskIMC(tid);
		if(!g_param.chkTask(tsk))
			return null;
//		if(!g_param.chkMCTask(tsk))
//			return null;
		return tsk;
	}



	
	public void prn(int lv) {
		for(Task t:g_tasks) {
			SLog.prn(1, "tid:"+t.tid+
					", p:"+t.period+
					", l:"+t.c_l+
					", h:"+t.c_h+
					", Xi:"+t.isHC());
		}
		SLog.prn(lv, "MC util:"+getUtil());
			
	}


	protected double getUtil(){
		double loutil=0;
		double hiutil=0;
		for(Task t:g_tasks){
			loutil+=t.getLoUtil();
			hiutil+=t.getHiUtil();
		}
		return Math.max(loutil, hiutil);
	}



	
}
