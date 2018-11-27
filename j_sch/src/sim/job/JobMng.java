package sim.job;

import java.util.PriorityQueue;
import java.util.Vector;

import util.Log;

public class JobMng {
	protected PriorityQueue<Job> g_jobs;
	protected int g_task_num=0;
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
				Log.prnc(1, "-");
			return;
		} 
		if (j.tid+1>g_task_num)
			g_task_num=j.tid+1;
		for (int i=0;i<g_task_num;i++)
		{
			if(i==j.tid){
				if(out_type==1)
					Log.prnc(1, "+"); // end
				else
					Log.prnc(1, "|"); // continue
			} else {
				Log.prnc(1, "-"); // empty
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
		if(g_jobs.size()==0){
			Log.prn(1, "none");
			return;
		}
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

}
