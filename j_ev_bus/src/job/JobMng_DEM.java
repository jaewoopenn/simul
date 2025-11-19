package job;

import java.util.PriorityQueue;


public class JobMng_DEM extends JobMng {
	private PriorityQueue<Job> g_jobs;
	public JobMng_DEM() {
		g_jobs=new PriorityQueue<Job>();
	}

	
	
	

	public void prn() {
		JobMisc.prn(g_jobs);
	}
	
	public boolean add(Job job) {
		g_jobs.add(job);
		return true;
	}
	public Job getCur(){
		return g_jobs.peek();
	}
	public Job removeCur(){
		Job j=g_jobs.poll();
		return j;
	}










	

}
