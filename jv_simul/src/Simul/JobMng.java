package Simul;

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
	public boolean progress(int cur_t,int dur){
		while(dur>0)
		{
			Job j=getCur();
			String out_type="idle";
			if(j==null)
			{
				prnJob(cur_t,null,out_type);
				break;
			}
			int out_dur=0;
			if(dur>=j.exec) {
				out_dur=j.exec;
				out_type="compl";
				dur-=j.exec;
				if(cur_t+j.exec>j.dl){
					Log.prn(1,"deadline miss tid:"+j.tid+" compl:"+(cur_t+j.exec)+" dl:"+j.dl);
					return false;
				}
				j.exec=0;
			} else {  // dur <j.exec
				out_dur=dur;
				out_type="continue";
				j.exec-=dur;
				dur=0;
				insert(j);
			}
			prnJob(cur_t,j,out_type);
//			Log.prn(1,"cur:"+cur_t+" dur:"+out_dur+" tid:"+j.tid+" exec_type:"+out_type);
		}
		return true;
		
	}
	public void prnJob(int cur_t,Job j,String out_type)
	{
		Log.prnc(1, "cur:"+cur_t+" ");
		if (j==null){
			for (int i=0;i<g_task_num;i++)
				Log.prnc(1, "-");
		} else{
			if (j.tid+1>g_task_num)
				g_task_num=j.tid+1;
			for (int i=0;i<g_task_num;i++)
			{
				if(i==j.tid)
					Log.prnc(1, "+");
				else
					Log.prnc(1, "-");
			}
		}
		Log.prn(1, "  \t exec_type:"+out_type);
	}
	public Job getCur(){
		if(jobs.size()!=0)
			return jobs.poll();
		else 
			return null;
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
		Vector<Job> v=new Vector();
		for(int i=0;i<sz;i++){
			Job j=getCur();
			Log.prn(1," dl:"+j.dl+", exec:"+j.exec);
			v.add(j);
		}
		for(Job j:v){
			insert(j);
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

}
