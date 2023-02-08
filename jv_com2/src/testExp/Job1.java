package testExp;
import java.util.PriorityQueue;
import java.util.Vector;

import basic.Task;
import exp.Job;
import util.TEngine;

public class Job1 {
	public static int log_level=1;
	public static int idx=-1;
//	public static int idx=1;
	public static int gret[]={2,3,1,4,0,0,0,0,0,0};
	public int test1()
	{
		Vector<Job> jobs=new Vector<Job>();
		
		jobs.add(new Job(new Task(0),3,1));
		jobs.add(new Job(new Task(1),4,1));
		return jobs.size();
	}
	public int test2()
	{
		PriorityQueue<Job> pq=new PriorityQueue<Job>();
		pq.add(new Job(new Task(0),4,1));
		pq.add(new Job(new Task(1),3,1));
		Job j=pq.poll();
		return j.dl;
	}
	public  double test3()
	{
		PriorityQueue<Job> pq=new PriorityQueue<Job>();
		pq.add(new Job(new Task(0),4,1));
		pq.add(new Job(new Task(1),3,2));
		Job j=pq.poll();
		j.exec-=1;
		pq.add(j);
		j=pq.poll();
		return j.exec;
		
	}
	public  int test4()
	{
		PriorityQueue<Job> pq=new PriorityQueue<Job>();
		pq.add(new Job(new Task(0),4,2));
		pq.add(new Job(new Task(1),3,1));
		Job j=pq.poll();
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
