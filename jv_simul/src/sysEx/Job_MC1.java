package sysEx;

import basic.Task;
import exp.Job;
import exp.JobMng;

// Task Set MC
public class Job_MC1 {
	public static JobMng ts1()
	{
		JobMng jm=new JobMng();
		jm.add(new Job(new Task(0),6,1,4.5,4));
		jm.add(new Job(new Task(1),6,1,3.5,2));
		jm.add(new Job(new Task(2),4,1));
		return jm;
	}
	

}
