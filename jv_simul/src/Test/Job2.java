package Test;
import java.util.PriorityQueue;
import java.util.Vector;

import Util.TEngine;
import Simul.Job;

public class Job2 {
//	public static int idx=-1;
	public static int idx=1;
	public static int total=10;
	public static int gret[]={2,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	public int test1()
	{
		Vector<Job> jobs=new Vector<Job>();
		
		jobs.add(new Job(0,3,1));
		jobs.add(new Job(1,4,1));
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
	public static void main(String[] args) throws Exception {
		Class c = Job2.class;
		Job2 m=new Job2();
		int[] aret=Job2.gret;
		int sz=Job2.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,1);
	}

}
