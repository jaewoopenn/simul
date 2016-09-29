package TestExp;

import Util.TEngine;
import Exp.JobMng;
import Exp.JobSimul;

public class JobSimul1 {
//	public static int idx=-1;
	public static int idx=1;
	public static int gret[]={2,-1,-1,-1,-1, -1,-1,-1,-1,-1};

	public JobMng ts1()	{
		JobMng jm=new JobMng();
		jm.insertJob(0,3,1);
		jm.insertJob(1,4,1);
		return jm;
	}
	
	public JobMng ts2()	{
		JobMng jm=new JobMng();
		jm.insertJob(1,3,1);
		jm.insertJob(0,4,2);
		return jm;
	}

	public JobMng ts3()	{
		JobMng jm=new JobMng();
		jm.insertJob(1,3,1);
		jm.insertJob(2,4,2);
		jm.insertJob(0,5,1);
		return jm;
	}

	public int test1()	{
		JobSimul js=new JobSimul(ts3());
		js.simul(6);
		return -1;
	}
	public int test2() {
		return -1;
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
			TEngine.runOnce(m,c,aret,idx,1);
	}

}
