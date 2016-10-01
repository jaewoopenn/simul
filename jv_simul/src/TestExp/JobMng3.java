package TestExp;

import Util.TEngine;
import Exp.Job;
import Exp.JobMng;

public class JobMng3 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=2;
	public static int gret[]={1,1,-1,-1,-1, -1,-1,-1,-1,-1};

	public JobMng ts1()
	{
		JobMng jm=new JobMng();
		jm.add(new Job(0,6,1,4.5,4));
		jm.add(new Job(1,6,1,3.5,2));
		jm.add(new Job(2,4,1));
		return jm;
	}


	
	public int test1()	{ // not ordering
		JobMng jm=ts1();
		jm.prn();
		return 1;
	}
	public int test2()	{ // ordering 
		JobMng jm=ts1();
		while(true){
			Job j=jm.removeCur();
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
		Class c = JobMng3.class;
		JobMng3 m=new JobMng3();
		int[] aret=JobMng3.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
