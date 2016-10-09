package testExp;

import exp.AbsJob;
import exp.JobMng;
import taskSetEx.Job_NonMC1;
import utilSim.TEngine;

public class JobMng2 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=2;
	public static int gret[]={1,1,-1,-1,-1, -1,-1,-1,-1,-1};

	public int test1()	{ // not ordering
		JobMng jm=Job_NonMC1.ts1();
		jm.prn();
		return 1;
	}
	public int test2()	{ // ordering 
		JobMng jm=Job_NonMC1.ts1();
		while(true){
			AbsJob j=jm.removeCur();
			if(j==null) break;
			j.prn();
			
		}
		return 1;
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
