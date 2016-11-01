package sysEx;

import exp.Job;
import exp.JobMng;

// Task Set MC
public class Job_NonMC1 {
	public static JobMng ts1()
	{
		JobMng jm=new JobMng();
		jm.add(new Job(0,3,1));
		jm.add(new Job(1,4,1));
		return jm;
	}
	

}
