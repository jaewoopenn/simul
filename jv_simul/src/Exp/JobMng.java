package Exp;

import java.util.PriorityQueue;

import Util.Log;

public class JobMng {
	PriorityQueue<Job> g_jobs;
	int g_task_num=0;
	public JobMng() {
		g_jobs=new PriorityQueue<Job>();
	}
	public void add(Job job) {
		g_jobs.add(job);
	}

	public boolean dlCheck(int cur_t){
		Job j=getCur();
		if(j==null) return true;
		if(cur_t>=j.dl){
			Log.prn(1,"deadline miss tid:"+j.tid+" compl:"+(cur_t+j.exec)+" dl:"+j.dl);
			return false;
		}
		return true;
	}
	public boolean progress(int cur_t){
		int out_type=0; // idle
		Job j=getCur(); 
		if(j==null)	{
			prnJob(null,out_type);
			return true;
		}
		
		if(j.exec<=1) {
			out_type=1; // complete
			j.exec=0;
			removeCur();
		} else {  // j.exec>1
			out_type=2; // rem
			j.exec-=1;
		}
		prnJob(j,out_type);
//			Log.prn(1,"cur:"+cur_t+" dur:"+out_dur+" tid:"+j.tid+" exec_type:"+out_type);
		return true;
		
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
		int sz=g_jobs.size();
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

}
