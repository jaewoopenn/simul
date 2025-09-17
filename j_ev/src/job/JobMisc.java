package job;

import java.util.PriorityQueue;
import java.util.Vector;

import util.SLog;

public class JobMisc {
	public static void prn(PriorityQueue<Job> jobs){
		if(jobs.size()==0){
			SLog.prn(1, "none");
			return;
		}
		for(Job j:jobs){
			SLog.prn(1, j.info());
		}
	}
	public static void prnPre(PriorityQueue<JobInput> jobs) {
		if(jobs.size()==0){
			SLog.prn(1, "none");
			return;
		}
		for(JobInput j:jobs){
			SLog.prn(1, j.info());
		}
		
	}

}
