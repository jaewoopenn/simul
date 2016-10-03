package test;

import basic.Task;
import basic.TaskMng;
import basic.TaskMngPre;
import simul.Analysis;
import utilSim.TEngine;

public class Analysis1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=6;
	public static int gret[]={1,0,1,1,1,1,0,0,0,0};

	public int test1() // less 1
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,2,1));
		tm.add(new Task(0,3,1));
		TaskMng m=tm.freezeTasks();
		return Analysis.anal_EDF(m);
	}

	public int test2() // over 1
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,2,1));
		tm.add(new Task(0,3,1));
		tm.add(new Task(0,4,1));
		TaskMng m=tm.freezeTasks();
		return Analysis.anal_EDF(m);
	}

	public int test3() // exact 1
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,2,1));
		tm.add(new Task(0,4,1));
		tm.add(new Task(0,8,1));
		tm.add(new Task(0,16,2));
		TaskMng m=tm.freezeTasks();
		return Analysis.anal_EDF(m);
	}

	public  int test4() // MC task
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,4,2));
		tm.add(new Task(1,6,1,5));
		TaskMng m=tm.freezeTasks();
		return Analysis.anal_EDF_VD(m);
	}

	public  int test5()
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,13,7));
		tm.add(new Task(1,12,4,8));
		TaskMng m=tm.freezeTasks();

		return Analysis.anal_EDF_VD(m);
	}
	
	public  int test6()
	{
		TaskMngPre tm=new TaskMngPre();
		tm.add(new Task(0,4,1));
		tm.add(new Task(1,6,1,5));
		tm.add(new Task(2,40,10,11));
		TaskMng m=tm.freezeTasks();
		return Analysis.anal_EDF_VD(m);
	}
	
	public  int test7()
	{
		return -1;
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
		Class c = Analysis1.class;
		Analysis1 m=new Analysis1();
		int[] aret=Analysis1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
