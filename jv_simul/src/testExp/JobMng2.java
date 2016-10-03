package testExp;

import exp.Job;
import exp.JobMng;
import utilSim.TEngine;

public class JobMng2 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=2;
	public static int gret[]={0,0,-1,-1,-1, -1,-1,-1,-1,-1};

	public JobMng ts1()
	{
		JobMng jm=new JobMng();
		jm.add(new Job(0,3,1));
		jm.add(new Job(1,4,1));
		return jm;
	}


	
	public int test1()
	{
		JobMng jm=ts1();
		Job j;
		j=jm.removeCur();
		j=jm.removeCur();
		j=jm.removeCur();
		if (j!=null)
			System.out.println(j.dl);
		else
			System.out.println(j);
		return jm.size();
	}
	public int test2()
	{
		JobMng jm=ts1();
		Job j;
		j=jm.removeCur();
		j=jm.removeCur();
		j=jm.getCur();
		if (j!=null)
			System.out.println(j.dl);
		else
			System.out.println(j);
		return jm.size();
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
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = JobMng2.class;
		JobMng2 m=new JobMng2();
		int[] aret=JobMng2.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
