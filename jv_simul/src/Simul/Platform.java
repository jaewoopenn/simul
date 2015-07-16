package Simul;

import java.util.Vector;

import Util.Log;

public class Platform {
	JobMng jm;
	Object[] tasks;
	int tasks_size;
	int cur_t;
	int[] plst;
	
	public void init(Vector<Task> tasks) {
		this.tasks=tasks.toArray();
		tasks_size=tasks.size();
		getPeriods();
		jm=new JobMng();
		cur_t=0;
	}
	public void simul(int et){
		while(cur_t<et){
			Log.prn(2,cur_t);
			relCheck();
			jm.progress(cur_t,1);
			cur_t++;
		}
		jm.prn();
	}

	
	private Task getTask(int idx)
	{
		return (Task)tasks[idx];
	}
	private void getPeriods() {
		this.plst=new int[tasks_size];
		for(int i=0;i<tasks_size;i++)
		{
			plst[i]=getTask(i).period;
		}
		
	}
	private void relCheck(){
		for(int i=0;i<tasks_size;i++){
			if (cur_t%plst[i]==0){
//				Log.prn(2,"rel "+i);
				Task tsk=getTask(i);
//				Log.prn(2, "p:"+tsk.period+" e:"+tsk.exec);
				Job j=new Job(tsk.tid,cur_t+tsk.period,tsk.exec);
//				j.prn();
				jm.insert(j);
			}
				
		}
	}

//	private int computeLCM() {
//		
//		for(Task t:tasks)
//		{
//			Log.prn(t.period);
//		}
//		return 0;
//	}

}
