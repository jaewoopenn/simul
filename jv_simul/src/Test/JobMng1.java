package Test;

import Test.TEngine;

import Simul.JobMng;
import Simul.Job;

public class JobMng1 {
	public static int total=10;
	public static int gret[]={1,2,2,1,0,0,1,0,0,0};

	public JobMng ts1()
	{
		JobMng jm=new JobMng();
		jm.insert(new Job(0,3,1));
		jm.insert(new Job(1,4,1));
		return jm;
	}
	public JobMng ts2()
	{
		JobMng jm=new JobMng();
		jm.insert(new Job(0,3,2));
		jm.insert(new Job(1,4,1));
		return jm;
	}
	public JobMng ts3()
	{
		JobMng jm=new JobMng();
		jm.insert(new Job(0,3,1));
		jm.insert(new Job(1,4,2));
		jm.insert(new Job(2,5,1));
		return jm;
	}
	public JobMng ts4()
	{
		JobMng jm=new JobMng();
		jm.insert(new Job(0,3,1));
		jm.insert(new Job(1,4,2));
		return jm;
	}
	public JobMng ts5()
	{
		JobMng jm=new JobMng();
		jm.insert(new Job(0,3,1));
		jm.insert(new Job(1,6,4));
		return jm;
	}

	
	public int test1()
	{
		JobMng jm=ts1();
		jm.progress(0,1);
		return jm.size();
	}
	public int test2()
	{
		JobMng jm=ts2();
		jm.progress(0,1);
		return jm.size();
	}
	public  int test3()
	{
		JobMng jm=ts3();
		jm.progress(0,2);
//		jm.prn();
		return jm.size();
	}
	public  int test4()
	{
		JobMng jm=ts3();
		jm.progress(0,3);
//		jm.prn();
		return jm.size();
	}
	public  int test5()
	{
		JobMng jm=ts1();
		jm.progress(0,3);
		return 0;
	}
	public  int test6()
	{
		JobMng jm=ts4();
		jm.progress(0,3);
		jm.insert(new Job(0,6,1));
		jm.progress(0,1);
		jm.insert(new Job(1,8,2));
		jm.progress(0,2);
		jm.insert(new Job(0,9,1));
		jm.prn();
		return 0;
	}
	public  int test7()
	{
		JobMng jm=ts5();
		jm.progress(0,3);
		jm.insert(new Job(0,6,1));
		jm.progress(0,2);
		return jm.size();
	}
	public  int test8()
	{
		return 0;
	}
	public  int test9()
	{
		return 0;
	}
	public  int test10()
	{
		return 0;
	}
	public static void main(String[] args) throws Exception {
		Class c = JobMng1.class;
		JobMng1 m=new JobMng1();
		int[] aret=JobMng1.gret;
		int sz=JobMng1.total;
		TEngine.run(m,c,aret,sz);
//		TEngine.runOnce(m,c,aret,7,1);
	}

}
