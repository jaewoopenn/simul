package sim.job;

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
	public static String getJobArrow(Job j,int out_type,int num)
	{
		String s="";
		for (int i=0;i<num;i++)
		{
			if(j==null) {
				s+="-";
			} else if(i==j.tid){
				if(out_type==1)
					s+="*"; // end
				else
					s+= "|"; // continue
			} else {
				s+= "-"; // empty
			}
		}
		return s;
	}

}
