package job;

import java.util.PriorityQueue;


public class JobMng_DEM extends JobMng {
	private PriorityQueue<Job> g_jobs;
	private PriorityQueue<Density> g_den;
	public JobMng_DEM() {
		g_jobs=new PriorityQueue<Job>();
		g_den=new PriorityQueue<Density>();
	}

	
	
	

	public void prn() {
		JobMisc.prn(g_jobs);
	}
	
	public boolean add(Job job) {
		g_jobs.add(job);
		g_den.add(new Density(job,job.den));
		return true;
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



	public int getOpt(double den) {
		int tot=0;
		for(Density d:g_den) {
			if(d.j.opt!=0&&d.den<den) {
				tot+=d.j.opt;
			}
		}
		
		return tot;
	}



	

}
