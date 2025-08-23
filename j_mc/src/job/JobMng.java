package job;

import java.util.PriorityQueue;
import java.util.Vector;


public class JobMng {
	protected PriorityQueue<Job> g_jobs;
	protected int g_task_num=0;
	public JobMng(int n) {
		g_jobs=new PriorityQueue<Job>();
		g_task_num=n;
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
	
	// MC related
	public void modeswitch(int tid) {
		Vector<Job> v=new Vector<Job>();
		for(Job j:g_jobs){
			if(j.tid==tid) {
//				j.prn();
				j.ms();
				j.setVD(j.dl);
//				j.prn();
				v.add(j);
			}
		}
		
		// because java.util.ConcurrentModificationException
		for(Job j:v){
			g_jobs.remove(j);
		}
		for(Job j:v){
			g_jobs.add(j);
		}
		
	}
	
	public void drop(int tid) {
		while(true) {
			Job t_j=null;
			for(Job j:g_jobs){
				if(j.isDrop())
					continue;
				if(j.tid==tid&&j.exec>0) {
					t_j=j;
					
				}
			}
			if(t_j==null)
				break;
			t_j.drop();
			g_jobs.remove(t_j);
			g_jobs.add(t_j);
		}
		
	}
	public int degrade(int tid) { // for IMC
		int d=0;
		while(true) {
			Job t_j=null;
			for(Job j:g_jobs){
				if(j.isDrop())
					continue;
				if(j.tid==tid&&j.exec>0) {
					t_j=j;
					
				}
			}
			if(t_j==null)
				break;
			t_j.degrade();
			d++;
			if(t_j.exec==0) {
				g_jobs.remove(t_j);
				g_jobs.add(t_j);
			}
		}
		return d;
	}

	public void prn() {
		JobMisc.prn(g_jobs);
	}
	public String getJobArrow(Job j, int out_type) {
		return JobMisc.getJobArrow(j, out_type, g_task_num);
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
