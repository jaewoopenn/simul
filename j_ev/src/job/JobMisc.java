package job;

import java.util.PriorityQueue;

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

}
