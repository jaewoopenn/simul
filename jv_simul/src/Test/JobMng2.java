package Test;

import Util.TEngine;
import Simul.JobMng;
import Simul.Job;

public class JobMng2 {
	public static int idx=-1;
//	public static int idx=8;
	public static int total=10;
	public static int gret[]={1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

	public JobMng ts1()
	{
		JobMng jm=new JobMng();
		jm.insertJob(0,3,1);
		jm.insertJob(1,4,1);
		return jm;
	}


	
	public int test1()
	{
		JobMng jm=ts1();
		jm.progress(0);
		return jm.size();
	}
	public int test2()
	{
		return -1;
	}
	public  int test3()
	{
		return -1;
	}
	public  int test4()
	{
		return -1;
	}
	public  int test5()
	{
		return -1;
	}
	public  int test6()
	{
		return -1;
	}
	public  int test7()
	{
		return -1;
	}
	public  int test8()
	{
		return -1;
	}
	public  int test9()
	{
		return -1;
	}
	public  int test10()
	{
		return -1;
	}
	public static void main(String[] args) throws Exception {
		Class c = JobMng2.class;
		JobMng2 m=new JobMng2();
		int[] aret=JobMng2.gret;
		int sz=JobMng2.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,1);
	}

}
