package exp;

import java.util.PriorityQueue;
import java.util.Vector;

import task.Task;
import util.SLog;

public class JobMng {
	PriorityQueue<Job> g_jobs;
	int g_task_num=0;
	public JobMng() {
		g_jobs=new PriorityQueue<Job>();
	}
	public void add(Job job) {
		g_jobs.add(job);
	}

	public void prnJob(boolean b,Job j,int out_type)
	{
		// out_type=0
		if(!b)
			return;
//		Log.prnc(1, "cur:"+cur_t+" ");
		if (j==null){
			for (int i=0;i<g_task_num;i++)
				SLog.prnc(1, "-");
			return;
		} 
		if (j.tsk.tid+1>g_task_num)
			g_task_num=j.tsk.tid+1;
		for (int i=0;i<g_task_num;i++)
		{
			if(i==j.tsk.tid){
				if(out_type==1)
					SLog.prnc(1, "+"); // end
				else
					SLog.prnc(1, "|"); // continue
			} else {
				SLog.prnc(1, "-"); // empty
			}
		}
//		Log.prn(1, " ");
//		Log.prn(1, "  \t exec_type:"+out_type);
	}
	public Job getCur(){
		return g_jobs.peek();
	}
	public Job removeCur(){
		return g_jobs.poll();
	}
	public int size(){
		return g_jobs.size();
	}
	public void prn(){
		for(Job j:g_jobs){
			j.prn();
		}
	}
	public int endCheck(int et) {
		for(Job j:g_jobs){
			if(j.dl<=et){
				return 0; // dl miss
			}
		}
		return 1; // OK 
	}
	public void modeswitch() {
		for(Job j:g_jobs){
			j.exec=j.exec+j.add_exec;
			j.add_exec=0;
		}
		
	}
	public void modeswitch(Task tsk) {
		Vector<Job> v=new Vector<Job>();
		for(Job j:g_jobs){
			if(j.tsk==tsk) {
				j.exec=j.exec+j.add_exec;
				((Job)j).vd=j.dl;
				j.add_exec=0;
				v.add(j);
			}
		}
		for(Job j:v){
			g_jobs.remove(j);
		}
		for(Job j:v){
			g_jobs.add(j);
		}
		
	}
	public int drop(Task tsk) {
		int d_num=0;
		for(Job j:g_jobs){
			if(j.tsk==tsk&&j.exec>0) {
				j.exec=0;
				d_num++;
			}
		}
//		Log.prn(1, d_num);
		return d_num;
		
	}

}
