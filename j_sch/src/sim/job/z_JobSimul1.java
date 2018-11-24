package sim.job;

import basic.Task;
import util.TEngine;
import z_ex.Job_MC1;

public class z_JobSimul1 {
	public static int idx=1;
	public static int log_level=1;
	public static int gret[]={2,0,-1,-1,-1, -1,-1,-1,-1,-1};


	public int test1()	{
		JobSimul js=new JobSimul();
		js.setJM(Job_MC1.ts3());
		js.simul(6);
		return -1;
	}
	public int test2() {
		JobSimul js=new JobSimul();
		js.add(new Job(1,3,1));
		js.add(new Job(2,4,2));
		js.add(new Job(0,5,1));
//		js.simulDur(0,6);
		js.add(new Job(0,8,1));
//		js.simulDur(6,10);
		js.simulEnd(10);
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
		Class c = z_JobSimul1.class;
		z_JobSimul1 m=new z_JobSimul1();
		int[] aret=z_JobSimul1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
