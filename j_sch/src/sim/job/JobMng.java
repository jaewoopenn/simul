package sim.job;

import java.util.PriorityQueue;

import util.FLog;
import util.Log;

public class JobMng {
	protected PriorityQueue<Job> g_jobs;
	protected int g_task_num=0;
	public JobMng(int n) {
		g_jobs=new PriorityQueue<Job>();
		g_task_num=n;
	}
	public void add(Job job) {
		g_jobs.add(job);
	}

	public String getJobArrow(Job j,int out_type)
	{
		String s="";
		if (j==null){
			for (int i=0;i<g_task_num;i++)
				s+="-";
			return s;
		} 
		for (int i=0;i<g_task_num;i++)
		{
			if(i==j.tid){
				if(out_type==1)
					s+="+"; // end
				else
					s+= "|"; // continue
			} else {
				s+= "-"; // empty
			}
		}
		return s;
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
		if(g_jobs.size()==0){
			Log.prn(1, "none");
			return;
		}
		for(Job j:g_jobs){
			Log.prn(1, j.info());
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
	public void f_prn() {
		if(g_jobs.size()==0){
			FLog.prn("none");
			return;
		}
		for(Job j:g_jobs){
			FLog.prn(j.info());
		}
				
	}

}
