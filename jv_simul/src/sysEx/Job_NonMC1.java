package sysEx;

import exp.JobD;
import exp.JobMng;

// Task Set MC
public class Job_NonMC1 {
	public static JobMng ts1()
	{
		JobMng jm=new JobMng();
		jm.add(new JobD(0,3,1));
		jm.add(new JobD(1,4,1));
		return jm;
	}
	

}
