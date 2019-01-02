package sim.mc;

import sim.job.Job;
import util.TEngine;

public class z_JobSimulMC1 {
	public static int idx=1;
	public static int log_level=1;
	public static int gret[]={2,0,-1,-1,-1, -1,-1,-1,-1,-1};


	public int test1()	{
		JobSimulMCEx js=new JobSimulMCEx(3);
		js.add(new Job(0,5,2));
		js.add(new Job(1,6,1,2,2));
		js.add(new Job(2,7,1,3,2));
		js.simulBy(6);
		return -1;
	}
	public int test2() {
		return 0;
	}
	
	public  int test3()	{
		return -1;
	}
	
	public  int test4()	{
		return -1;
	}
	
	public  int test5() {
		return -1;
	}
	public  int test6()	{
		return -1;
	}
	public  int test7()	{
		return -1;
	}
	public  int test8()	{
		return -1;
	}
	public  int test9() {
		return -1;
	}
	public  int test10() {
		return -1;
	}

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = z_JobSimulMC1.class;
		z_JobSimulMC1 m=new z_JobSimulMC1();
		int[] aret=z_JobSimulMC1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
