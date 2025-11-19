package job;

import java.util.LinkedList;
import java.util.Queue;

import util.SLog;


public class JobMng_FIFO extends JobMng {
	private Queue<Job> g_jobs;
	private int dem=0;
	public JobMng_FIFO() {
		g_jobs=new LinkedList<>();
	}

	
	public boolean add(Job job) {
		g_jobs.add(job);
		dem+=job.exec;
		return true;
	}
	public Job getCur(){
		return g_jobs.peek();
	}
	public Job removeCur(){
		Job j=g_jobs.poll();
		return j;
	}




	@Override
	protected void prn() {
		int d=g_jobs.size();
		SLog.prn("size:"+d);
		SLog.prn("demand:"+dem);
	}







	protected int getDemand() {
		return dem;
	}

	public void reset() {
		dem=0;
	}






}
