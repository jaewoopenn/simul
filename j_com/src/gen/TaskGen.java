package gen;

import java.util.Vector;

import task.Task;
import task.TaskVec;

public class TaskGen {
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
//		SLog.prn(2, "===");
		while(getUtil()<=g_param.u_ub){
			t=genTask(tid);
			g_tasks.add(t);
			tid++;
//			SLog.prn(2, ""+getUtil());
		}
		g_tasks.remove(g_tasks.size()-1);
	}
	

	public  Task genTask(int tid) {
		Task tsk=g_param.genTask(tid,true);
		if(!g_param.chkTask(tsk))
			return null;
		return tsk;		
	}


	public int check(){
		return g_param.check(getUtil());
	}
	
	public  void prn(int lv) {
		
	}


	protected  double getUtil() {
		double util=0;
		for(Task t:g_tasks){
			util+=t.getUtil();
		}
		return util;
	}

	public Vector<Task> getAll() {
		return g_tasks;
	}


	public int size() {
		return g_tasks.size();
	}

	public TaskVec getTS() {
		TaskVec ts=new TaskVec();
		for(Task t:g_tasks) {
			ts.add(t);
		}
		return ts;
	}
	
	
	
}
