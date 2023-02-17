package testExp;
import java.util.Vector;

import exp.Job;
import util.SEngineT;

public class Job2 {
	public static int idx=1;
//	public static int idx=-1;
	public static int gret[]={2,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	public static int log_level=1;
	public int test1()
	{
		Vector<Job> jobs=new Vector<Job>();
		
//		jobs.add(new Job(new Task(0),3,1));
//		jobs.add(new Job(new Task(1),4,1));
		return jobs.size();
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

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = Job2.class;
		Job2 m=new Job2();
		int[] aret=Job2.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}

}
