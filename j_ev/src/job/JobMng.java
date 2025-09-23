package job;

import java.util.PriorityQueue;


public class JobMng {
	private PriorityQueue<Job> g_jobs;
	private PriorityQueue<Density> g_den;
	public JobMng() {
		g_jobs=new PriorityQueue<Job>();
		g_den=new PriorityQueue<Density>();
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
		g_den.add(new Density(job,job.den));
	}
	public Job getCur(){
		return g_jobs.peek();
	}
	public Job removeCur(){
		Job j=g_jobs.poll();
		Density dd=null;
		for(Density d:g_den) {
			if(d.j==j)
				dd=d;
		}
		g_den.remove(dd);
		return j;
	}






	public Job pickDenBelow(double den) {
		Job o=null;
		for(Density d:g_den) {
			if(d.j.opt!=0&&d.den<den) {
				o=d.j;
				break;
			}
		}
		if(o!=null) {
			return o;
		}
		return null;
		
	}
//	public boolean remove(int et, int e) {
//	Job o=null;
//	for(Job j:g_jobs) {
//		if(et==j.dl&&e==j.exec) {
//			o=j;
//			break;
//		}
//	}
//	if(o!=null) {
//		g_jobs.remove(o);
//		return true;
//	}
//	return false;
//	
//}


	public void prn_den() {
		JobMisc.prn_den(g_den);
		
	}
	

}
