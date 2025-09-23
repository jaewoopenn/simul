package job;

import java.util.PriorityQueue;

import util.SLog;

public class JobMisc {
	public static void prn(PriorityQueue<Job> jobs){
		if(jobs.size()==0){
			SLog.prn("none");
			return;
		}
		for(Job j:jobs){
			SLog.prn(j.info());
		}
	}
	public static void prn_ji(PriorityQueue<JobInput> jobs) {
		if(jobs.size()==0){
			SLog.prn("none");
			return;
		}
		for(JobInput j:jobs){
			SLog.prn(j.info());
		}
		
	}
	public static void prn_den(PriorityQueue<DenItem> g_den) {
		if(g_den.size()==0){
			SLog.prn("none");
			return;
		}
		for(DenItem d:g_den){
//			SLog.prn(d.den+" ");
			SLog.prn(d.j.info());
		}
		
	}

}
