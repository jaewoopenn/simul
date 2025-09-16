package job;

import java.util.PriorityQueue;


public class JobMng {
	protected PriorityQueue<Job> g_jobs;
	public JobMng() {
		g_jobs=new PriorityQueue<Job>();
	}

	
	public boolean isIdle() {  // Real Idle
		return getCur()==null;
	}
	
	public int endDL(int et) {
		int dm=0;
		for(Job j:g_jobs){
			if(j.dl<=et){
				dm++;
			}
		}
		return dm;
	}
	
	public int endCheck(int et) {
		for(Job j:g_jobs){
			if(j.dl<=et){
				return 0; // dl miss
			}
		}
		return 1; // OK 
	}
	

	public void prn() {
		JobMisc.prn(g_jobs);
	}
	
	public void add(Job job) {
		g_jobs.add(job);
	}
	public Job getCur(){
		return g_jobs.peek();
	}
	public Job removeCur(){
		return g_jobs.poll();
	}
	

}
