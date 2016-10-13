package testExp;

import exp.AbsJob;
import exp.JobMng;
import sysEx.Job_NonMC1;
import utill.TEngine;

public class JobMng1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=2;
	public static int gret[]={0,0,-1,-1,-1, -1,-1,-1,-1,-1};



	
	public int test1()
	{
		JobMng jm=Job_NonMC1.ts1();
		AbsJob j;
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
		JobMng jm=Job_NonMC1.ts1();
		AbsJob j;
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
		Class c = JobMng1.class;
		JobMng1 m=new JobMng1();
		int[] aret=JobMng1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
