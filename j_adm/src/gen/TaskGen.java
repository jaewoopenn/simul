package gen;

import java.util.Vector;

import task.Task;
import task.TaskSet;
import task.TaskVec;

public abstract class TaskGen {
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

	public abstract Task genTask(int tid);


	public boolean chkUtil() {
		return g_param.chkUtil(getUtil());
	}
	
	public abstract void prn(int lv) ;


	protected abstract double getUtil(); 


	public TaskSet getTS() {
		TaskVec ts=new TaskVec();
		for(Task t:g_tasks) {
			ts.add(t);
		}
		return new TaskSet(ts);
	}
	
	
	
	
}
