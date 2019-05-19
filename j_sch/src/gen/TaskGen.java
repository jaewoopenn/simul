package gen;

import java.util.Vector;

import task.Task;
import task.TaskSet;
import task.TaskVec;

public abstract class TaskGen {
	protected TaskGenParam g_param;
	protected Vector<Task> g_tasks;

	public TaskGen(TaskGenParam tgp) {
		g_param=tgp;
	}

	public void generate() {
		while(true){
			g_tasks=new Vector<Task>();
			genTaskSet();
			if(check()==1) break;
		}
	}
	private void genTaskSet()
	{
		int tid=0;
		Task t;
		while(getUtil()<=g_param.u_ub){
			t=genTask(tid);
			if (!t.check())
				continue;
			g_tasks.add(t);
			tid++;
		}
		g_tasks.remove(g_tasks.size()-1);
	}
	

	public abstract Task genTask(int tid);


	public int check(){
		return g_param.check(getUtil());
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
	
	public int size() {
		return g_tasks.size();
	}
	
	
	
}
