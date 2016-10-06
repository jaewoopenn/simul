package testExp;
import java.util.PriorityQueue;
import java.util.Vector;

import exp.JobD;
import utilSim.TEngine;

public class Job1 {
	public static int log_level=1;
	public static int idx=-1;
//	public static int idx=1;
	public static int gret[]={2,3,1,4,0,0,0,0,0,0};
	public int test1()
	{
		Vector<JobD> jobs=new Vector<JobD>();
		
		jobs.add(new JobD(0,3,1));
		jobs.add(new JobD(1,4,1));
		return jobs.size();
	}
	public int test2()
	{
		PriorityQueue<JobD> pq=new PriorityQueue<JobD>();
		pq.add(new JobD(0,4,1));
		pq.add(new JobD(1,3,1));
		JobD j=pq.poll();
		return j.dl;
	}
	public  double test3()
	{
		PriorityQueue<JobD> pq=new PriorityQueue<JobD>();
		pq.add(new JobD(0,4,1));
		pq.add(new JobD(1,3,2));
		JobD j=pq.poll();
		j.exec-=1;
		pq.add(j);
		j=pq.poll();
		return j.exec;
		
	}
	public  int test4()
	{
		PriorityQueue<JobD> pq=new PriorityQueue<JobD>();
		pq.add(new JobD(0,4,2));
		pq.add(new JobD(1,3,1));
		JobD j=pq.poll();
		j.exec-=1;
		if(j.exec>0)
			pq.add(j);
		j=pq.poll();
		return j.dl;
	}
	public  int test5()
	{
		return 0;
	}
	public  int test6()
	{
		return 0;
	}
	public  int test7()
	{
		return 0;
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
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = Job1.class;
		Job1 m=new Job1();
		int[] aret=Job1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
