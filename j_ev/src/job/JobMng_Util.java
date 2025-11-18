package job;

import java.util.PriorityQueue;

import util.SLog;


public class JobMng_Util extends JobMng {
	private PriorityQueue<Job> g_jobs;
	private PriorityQueue<Job> g_jobs2;
	private double util=0;
	public JobMng_Util() {
		g_jobs=new PriorityQueue<Job>();
		g_jobs2=new PriorityQueue<Job>();
	}

	
	
	@Override
	protected boolean add(Job j) {
		g_jobs.add(j);
		g_jobs2.add(j);
		j.util=(double)(j.exec+j.opt)/j.rel_dl;
		util+=j.util;
		return true;
	}
	
	@Override
	public void prn() {
		JobMisc.prn(g_jobs);
	}

	@Override
	public Job getCur(){
		return g_jobs.peek();
	}
	public Job getCur2(){
		return g_jobs.peek();
	}
	@Override
	public Job removeCur(){
		Job j=g_jobs.poll();
		return j;
	}
	public Job removeCur2(){
		Job j=g_jobs2.poll();
		util-=j.util;
		SLog.prn("util: "+util);
		return j;
	}

	protected double getUtil() {
		return util; 
	}

	public void reset() {
		util=0;
	}










	

}
