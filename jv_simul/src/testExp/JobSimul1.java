package testExp;

import basic.Task;
import exp.Job;
import exp.JobMng;
import exp.JobSimul;
import util.TEngine;

public class JobSimul1 {
	public static int log_level=1;
	public static int idx=1;
	public static int gret[]={2,0,-1,-1,-1, -1,-1,-1,-1,-1};

	public JobMng ts1()	{
		JobMng jm=new JobMng();
		jm.add(new Job(new Task(0),3,1));
		jm.add(new Job(new Task(1),4,1));
		return jm;
	}
	
	public JobMng ts2()	{
		JobMng jm=new JobMng();
		jm.add(new Job(new Task(1),3,1));
		jm.add(new Job(new Task(0),4,2));
		return jm;
	}

	public JobMng ts3()	{
		JobMng jm=new JobMng();
		jm.add(new Job(new Task(1),3,1));
		jm.add(new Job(new Task(2),4,2));
		jm.add(new Job(new Task(0),5,1));
		return jm;
	}

	public int test1()	{
		JobSimul js=new JobSimul(ts3());
		js.simul(6);
		return -1;
	}
	public int test2() {
		JobSimul js=new JobSimul();
		js.add(new Job(new Task(1),3,1));
		js.add(new Job(new Task(2),4,2));
		js.add(new Job(new Task(0),5,1));
//		js.simulDur(0,6);
		js.add(new Job(new Task(0),8,1));
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
		Class c = JobSimul1.class;
		JobSimul1 m=new JobSimul1();
		int[] aret=JobSimul1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
