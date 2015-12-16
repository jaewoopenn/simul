package Exp;

import java.util.PriorityQueue;
import java.util.Vector;

import Util.Log;

public class JobMng {
	PriorityQueue<Job> jobs;
	int g_task_num=0;
	public JobMng() {
		jobs=new PriorityQueue<Job>();
	}
	public void insert(Job job) {
		jobs.add(job);
	}

	public void insertJob(int tid,int dl,int et) {
		Job j=new Job(tid,dl,et);
		jobs.add(j);
	}
	public void insertJob(int tid, int dl, int et, int add) {
		Job j=new Job(tid,dl,et,add);
		jobs.add(j);
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
		Job j=getCur();
		int out_type=0;
		if(j==null)
		{
			prnJob(null,out_type);
			return true;
		}
		int out_dur=0;
		if(j.exec<=1) {
			out_type=1;
			j.exec=0;
			pollCur();
		} else {  // j.exec>1
			out_type=2;
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
		} else{
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
		}
		Log.prn(1, " ");
//		Log.prn(1, "  \t exec_type:"+out_type);
	}
	public Job getCur(){
		return jobs.peek();
	}
	public Job pollCur(){
		return jobs.poll();
	}
	public int size(){
		return jobs.size();
	}
	public void prn(){
		int sz=jobs.size();
		if(sz==0)
		{
			Log.prn(1,"empty");
			return;
		}
		for(Job j:jobs){
			Log.prn(1,"tid:"+j.tid+" dl:"+j.dl+", exec:"+j.exec);
		}
	}
	public int endCheck(int et) {
		for(Job j:jobs){
			if(j.dl<=et){
				return 0;
			}
		}
		return 1;
	}
	public void modeswitch() {
		for(Job j:jobs){
			j.exec=j.exec+j.add_exec;
			j.add_exec=0;
		}
		
	}

}
