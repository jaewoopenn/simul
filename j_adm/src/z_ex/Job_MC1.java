package z_ex;

import sim.job.Job;
import sim.mc.JobMngMC;

// Task Set MC
public class Job_MC1 {
	public static JobMngMC ts1()
	{
		JobMngMC jm=new JobMngMC(3);
		jm.add(new Job(0,6,1,4.5,4));
		jm.add(new Job(1,6,1,3.5,2));
		jm.add(new Job(2,4,1));
		return jm;
	}
	public static JobMngMC ts2()
	{
		JobMngMC jm=new JobMngMC(3);
		jm.add(new Job(0,3,1));
		jm.add(new Job(1,4,1));
		return jm;
	}
	public static JobMngMC ts3()
	{
		JobMngMC jm=new JobMngMC(3);
		jm.add(new Job(1,3,1));
		jm.add(new Job(2,4,2));
		jm.add(new Job(0,5,1));
		return jm;
	}
	

}
