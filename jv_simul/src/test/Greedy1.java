package test;

import basic.Task;
import basic.TaskMng;
import basic.TaskMngPre;
import simul.Analysis;
import utilSim.Log;
import utilSim.TEngine;

public class Greedy1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=1;
	public static int gret[]={1,0,1,1,1,1,0,0,0,0};

	public int test1() 
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,8,3));
		tm.add(new Task(0,12,2,8));
		tm.add(new Task(0,40,4,5));
		TaskMng m=tm.freezeTasks();
		return Analysis.anal_EDF_VD(m); // det:0.855
	}

	public int test2() 
	{
		return 0;
	}

	public int test3() 
	{
		return 0;
	}

	public  int test4()
	{
		return 0;
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
		Class c = Greedy1.class;
		Greedy1 m=new Greedy1();
		int[] aret=Greedy1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
