package exp;

import java.util.PriorityQueue;

import utilSim.Log;

public class JobMng {
	PriorityQueue<Job> g_jobs;
	int g_task_num=0;
	public JobMng() {
		g_jobs=new PriorityQueue<Job>();
	}
	public void add(Job job) {
		g_jobs.add(job);
	}

	public void prnJob(Job j,int out_type)
	{
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
					Log.prnc(1, "+");
				else
					Log.prnc(1, "|");
			} else {
				Log.prnc(1, "-");
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
	public void modeswitch(int tid) {
		for(Job j:g_jobs){
			if(j.tid==tid) {
				j.exec=j.exec+j.add_exec;
				j.add_exec=0;
			}
		}
		
	}
	public void drop(int tid) {
		for(Job j:g_jobs){
			if(j.tid==tid) {
				j.exec=0;
			}
		}
		
	}

}
