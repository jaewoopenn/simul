package z_ex;

import basic.Task;
import sim.job.Job;
import sim.job.JobMng;

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
	public static JobMng ts2()
	{
		JobMng jm=new JobMng();
		jm.add(new Job(new Task(0),3,1));
		jm.add(new Job(new Task(1),4,1));
		return jm;
	}
	public static JobMng ts3()
	{
		JobMng jm=new JobMng();
		jm.add(new Job(1,3,1));
		jm.add(new Job(2,4,2));
		jm.add(new Job(0,5,1));
		return jm;
	}
	

}
