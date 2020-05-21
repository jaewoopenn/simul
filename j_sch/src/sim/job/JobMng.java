package sim.job;

import java.util.PriorityQueue;

import util.SLogF;
import util.SLog;

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
	public boolean isIdle(int t) {  // idle except dropped job
		Job j=getCur();
		if(j==null)
			return true;
		if(j.isDrop()) {
			if(t<j.dl)
				return false;
			else //t>=j.dl
				return true;
		}
		return false;
	}
	public boolean isIdle2() {  // Real Idle
		return getCur()==null;
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
			SLog.prn(1, "none");
			return;
		}
		for(Job j:g_jobs){
			SLog.prn(1, j.info());
		}
	}
	public int endDL(int et) {
		int dm=0;
		for(Job j:g_jobs){
			if(j.isHI)
				continue;
			if(j.dl<=et){
				dm++;
			}
		}
		return dm;
	}
	
	public int endCheck(int et) {
		for(Job j:g_jobs){
			if(!j.isHI)
				continue;
			if(j.dl<=et){
				return 0; // dl miss
			}
		}
		return 1; // OK 
	}
	public void f_prn() {
		if(g_jobs.size()==0){
			SLogF.prn("none");
			return;
		}
		for(Job j:g_jobs){
			SLogF.prn(j.info());
		}
				
	}

}
